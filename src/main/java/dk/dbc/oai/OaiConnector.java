/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.oai;

import dk.dbc.httpclient.FailSafeHttpClient;
import dk.dbc.httpclient.HttpGet;
import dk.dbc.invariant.InvariantUtil;
import net.jodah.failsafe.RetryPolicy;
import org.openarchives.oai.Identify;
import org.openarchives.oai.ListRecords;
import org.openarchives.oai.OAIPMH;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

 /**
 * OaiConnector - OAI-PMH client
 * <p>
 * To use this class, you construct an instance, specifying a web resources client as well as
 * a base URL for the OAI service endpoint you will be communicating with.
 * </p>
 * <p>
 * This class is thread safe, as long as the given web resources client remains thread safe.
 * </p>
 */
public class OaiConnector {
    private static final RetryPolicy RETRY_POLICY = new RetryPolicy()
            .retryOn(Collections.singletonList(ProcessingException.class))
            .retryIf((Response response) ->
                    response.getStatus() == 404
                            || response.getStatus() == 500
                            || response.getStatus() == 502)
            .withDelay(10, TimeUnit.SECONDS)
            .withMaxRetries(6);

    private final FailSafeHttpClient failSafeHttpClient;
    private final String baseUrl;
    private final JAXBContext jaxbContext;

    public OaiConnector(Client httpClient, String baseUrl)
            throws OaiConnectorException {
        this(FailSafeHttpClient.create(httpClient, RETRY_POLICY), baseUrl);
    }

    /**
     * Returns new instance with custom retry policy
     * @param failSafeHttpClient web resources client with custom retry policy
     * @param baseUrl base URL for record service endpoint
     * @throws OaiConnectorException on failure to create {@link JAXBContext}
     */
    public OaiConnector(FailSafeHttpClient failSafeHttpClient, String baseUrl)
            throws OaiConnectorException {
        this.failSafeHttpClient = InvariantUtil.checkNotNullOrThrow(
                failSafeHttpClient, "failSafeHttpClient");
        this.baseUrl = InvariantUtil.checkNotNullNotEmptyOrThrow(
                baseUrl, "baseUrl");
        try {
            this.jaxbContext = JAXBContext.newInstance(OAIPMH.class);
        } catch (JAXBException e) {
            throw new OaiConnectorException("Unable to create JAXB context", e);
        }
    }

    public void close() {
        failSafeHttpClient.getClient().close();
    }

    /* http://www.openarchives.org/OAI/openarchivesprotocol.html#Identify */
    public Identify identify() throws OaiConnectorException {
        final Params params = new Params().withVerb(Params.Verb.Identify);
        final OAIPMH oaipmh = executeRequest(params);
        return oaipmh.getIdentify();
    }

    /* http://www.openarchives.org/OAI/openarchivesprotocol.html#ListRecords */
    public ListRecords listRecords(Params params) throws OaiConnectorException {
        params.withVerb(Params.Verb.ListRecords);
        final OAIPMH oaipmh = executeRequest(params);
        return oaipmh.getListRecords();
    }

    public ZonedDateTime getServerCurrentTime() throws OaiConnectorException {
        final Params params = new Params().withVerb(Params.Verb.Identify);
        final OAIPMH oaipmh = executeRequest(params);
        return oaipmh.getResponseDate()
                .toGregorianCalendar()
                .toZonedDateTime()
                .withZoneSameLocal(ZoneId.of("UTC"));
    }

    private OAIPMH executeRequest(Params params) throws OaiConnectorException {
        final HttpGet httpGet = new HttpGet(failSafeHttpClient)
                .withBaseUrl(baseUrl);
        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                httpGet.withQueryParameter(param.getKey(), param.getValue());
            }
        }
        final Response response = httpGet.execute();
        assertResponseStatus(response, Response.Status.OK);
        final OAIPMH oaipmh = readResponseEntity(response);
        if (oaipmh.getError() != null && !oaipmh.getError().isEmpty()) {
            throw new OaiConnectorException(oaipmh.getError().get(0).getValue());
        }
        return oaipmh;
    }

    private OAIPMH readResponseEntity(Response response) throws OaiConnectorException {
        final String entity = response.readEntity(String.class);
        if (entity == null) {
            throw new OaiConnectorException("OAI service returned with null-valued entity");
        }
        try {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final JAXBElement<OAIPMH> jaxbElement =
                    (JAXBElement<OAIPMH>) unmarshaller.unmarshal(new StringReader(entity));
            if (jaxbElement == null) {
                throw new JAXBException("JAXB element is null");
            }
            return jaxbElement.getValue();
        } catch (JAXBException e) {
            throw new OaiConnectorException("Unable to extract OAI-PMH element from entity", e);
        }
    }

    private void assertResponseStatus(Response response, Response.Status expectedStatus)
            throws OaiConnectorUnexpectedStatusCodeException {
        final Response.Status actualStatus =
                Response.Status.fromStatusCode(response.getStatus());
        if (actualStatus != expectedStatus) {
            throw new OaiConnectorUnexpectedStatusCodeException(
                    String.format("OAI service returned with unexpected status code: %s",
                            actualStatus), actualStatus.getStatusCode());
        }
    }

    /**
     * OAI protocol parameters
     */
    public static class Params extends HashMap<String, Object> {
        public enum Key {
            /**
             * UTC datetime value, which specifies a lower bound for datestamp-based selective harvesting
             */
            FROM("from"),
            /**
             * the format that should be included in the metadata part of the returned records
             */
            METADATA_PREFIX("metadataPrefix"),
            /**
             * flow control token returned by a previous request that issued an incomplete list
             */
            RESUMPTION_TOKEN("resumptionToken"),
            /**
             * setSpec value specifying set criteria for selective harvesting
             */
            SET("set"),
            /**
             * UTC datetime value, which specifies a upper bound for datestamp-based selective harvesting
             */
            UNTIL("until"),
            /**
             * Verb identifying one of the supported OAI-PMH request types
             */
            VERB("verb");

            private final String keyName;

            Key(String keyName) {
                this.keyName = keyName;
            }
            public String getKeyName() {
                return keyName;
            }
        }

        public enum Verb {
            Identify, ListRecords
        }

        /* the given datetime will be converted to UTC internally */
        public Params withFrom(ZonedDateTime zonedDateTime) {
            if (zonedDateTime != null) {
                final ZonedDateTime utc =
                        zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
                putOrRemoveOnNull(Key.FROM, DateTimeFormatter.UTC_DATE_TIME.format(utc));
            }
            return this;
        }

        /* the from time will always be returned in UTC */
        public Optional<ZonedDateTime> getFrom() {
            final String utcDateTimeString =
                    (String) this.get(Key.FROM);
            if (utcDateTimeString == null || utcDateTimeString.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(ZonedDateTime.parse(
                    utcDateTimeString, DateTimeFormatter.UTC_DATE_TIME.withZone(ZoneId.of("UTC"))));
        }

        public Params withMetadataPrefix(String metadataPrefix) {
            putOrRemoveOnNull(Key.METADATA_PREFIX, metadataPrefix);
            return this;
        }

        public Optional<String> getMetadataPrefix() {
            return Optional.ofNullable((String) this.get(Key.METADATA_PREFIX));
        }

        public Params withResumptionToken(String token) {
            putOrRemoveOnNull(Key.RESUMPTION_TOKEN, token);
            return this;
        }

        public Optional<String> getResumptionToken() {
            return Optional.ofNullable((String) this.get(Key.RESUMPTION_TOKEN));
        }

        public Params withSet(String setSpec) {
            putOrRemoveOnNull(Key.SET, setSpec);
            return this;
        }

        public Optional<String> getSet() {
            return Optional.ofNullable((String) this.get(Key.SET));
        }

        public Params withVerb(Verb verb) {
            putOrRemoveOnNull(Key.VERB, verb);
            return this;
        }

        public Optional<Verb> getVerb() {
            return Optional.ofNullable((Verb) this.get(Key.VERB));
        }

        /* the given datetime will be converted to UTC internally */
        public Params withUntil(ZonedDateTime zonedDateTime) {
            if (zonedDateTime != null) {
                final ZonedDateTime utc =
                        zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
                putOrRemoveOnNull(Key.UNTIL, DateTimeFormatter.UTC_DATE_TIME.format(utc));
            }
            return this;
        }

        /* the from time will always be returned in UTC */
        public Optional<ZonedDateTime> getUntil() {
            final String utcDateTimeString =
                    (String) this.get(Key.UNTIL);
            if (utcDateTimeString == null || utcDateTimeString.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(ZonedDateTime.parse(
                    utcDateTimeString, DateTimeFormatter.UTC_DATE_TIME.withZone(ZoneId.of("UTC"))));
        }

        private void putOrRemoveOnNull(Key param, Object value) {
            if (value == null) {
                this.remove(param.keyName);
            } else {
                this.put(param.keyName, value);
            }
        }

        private Object get(Key param) {
            return get(param.keyName);
        }
    }
}
