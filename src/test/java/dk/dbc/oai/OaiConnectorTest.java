/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.oai;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openarchives.oai.Identify;
import org.openarchives.oai.ListRecords;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OaiConnectorTest {
    private static WireMockServer wireMockServer;
    private static String wireMockServerBaseurl;

    @BeforeClass
    public static void startWireMockServer() {
        wireMockServer = new WireMockServer(
                options().dynamicPort().dynamicHttpsPort());
        wireMockServer.start();
        wireMockServerBaseurl = "http://localhost:" + wireMockServer.port();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterClass
    public static void tearDown() {
        wireMockServer.stop();
    }

    private static final String IDENTIFY_RESPONSE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" \n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/\n" +
            "         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">\n" +
            "  <responseDate>2002-02-08T12:00:01Z</responseDate>\n" +
            "  <request verb=\"Identify\">http://memory.loc.gov/cgi-bin/oai</request>\n" +
            "  <Identify>\n" +
            "    <repositoryName>Library of Congress Open Archive Initiative \n" +
            "                    Repository 1</repositoryName>\n" +
            "    <baseURL>http://memory.loc.gov/cgi-bin/oai</baseURL>\n" +
            "    <protocolVersion>2.0</protocolVersion>\n" +
            "    <adminEmail>somebody@loc.gov</adminEmail>\n" +
            "    <adminEmail>anybody@loc.gov</adminEmail>\n" +
            "    <earliestDatestamp>1990-02-01T12:00:00Z</earliestDatestamp>\n" +
            "    <deletedRecord>transient</deletedRecord>\n" +
            "    <granularity>YYYY-MM-DDThh:mm:ssZ</granularity>\n" +
            "    <compression>deflate</compression>\n" +
            "    <description>\n" +
            "      <oai-identifier \n" +
            "        xmlns=\"http://www.openarchives.org/OAI/2.0/oai-identifier\"\n" +
            "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "        xsi:schemaLocation=\n" +
            "            \"http://www.openarchives.org/OAI/2.0/oai-identifier\n" +
            "        http://www.openarchives.org/OAI/2.0/oai-identifier.xsd\">\n" +
            "        <scheme>oai</scheme>\n" +
            "        <repositoryIdentifier>lcoa1.loc.gov</repositoryIdentifier>\n" +
            "        <delimiter>:</delimiter>\n" +
            "        <sampleIdentifier>oai:lcoa1.loc.gov:loc.music/musdi.002</sampleIdentifier>\n" +
            "      </oai-identifier>\n" +
            "    </description>\n" +
            "    <description>\n" +
            "      <eprints \n" +
            "         xmlns=\"http://www.openarchives.org/OAI/1.1/eprints\"\n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://www.openarchives.org/OAI/1.1/eprints \n" +
            "         http://www.openarchives.org/OAI/1.1/eprints.xsd\">\n" +
            "        <content>\n" +
            "          <URL>http://memory.loc.gov/ammem/oamh/lcoa1_content.html</URL>\n" +
            "          <text>Selected collections from American Memory at the Library \n" +
            "                of Congress</text>\n" +
            "        </content>\n" +
            "        <metadataPolicy/>\n" +
            "        <dataPolicy/>\n" +
            "      </eprints>\n" +
            "    </description>\n" +
            "    <description>\n" +
            "      <friends \n" +
            "          xmlns=\"http://www.openarchives.org/OAI/2.0/friends/\" \n" +
            "          xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "          xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/friends/\n" +
            "         http://www.openarchives.org/OAI/2.0/friends.xsd\">\n" +
            "       <baseURL>http://oai.east.org/foo/</baseURL>\n" +
            "       <baseURL>http://oai.hq.org/bar/</baseURL>\n" +
            "       <baseURL>http://oai.south.org/repo.cgi</baseURL>\n" +
            "     </friends>\n" +
            "   </description>\n" +
            " </Identify>\n" +
            "</OAI-PMH>";

    private static final String LIST_RECORDS_RESPONSE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" \n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/\n" +
            "         http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">\n" +
            " <responseDate>2002-06-01T19:20:30Z</responseDate> \n" +
            " <request verb=\"ListRecords\" from=\"2002-02-08T11:00:01Z\"/>\n" +
            " <ListRecords>\n" +
            "  <record>\n" +
            "    <header>\n" +
            "      <identifier>one</identifier>\n" +
            "    </header>\n" +
            "    <metadata>\n" +
            "      <anyThing>1</anyThing>\n" +
            "    </metadata>\n" +
            "  </record>\n" +
            "  <record>\n" +
            "    <header status=\"deleted\">\n" +
            "      <identifier>two</identifier>\n" +
            "    </header>\n" +
            "  </record>\n" +
            "  <record>\n" +
            "    <header>\n" +
            "      <identifier>three</identifier>\n" +
            "    </header>\n" +
            "    <metadata>\n" +
            "      <test:anyThing xmlns:test=\"uri:test\">3</test:anyThing>\n" +
            "    </metadata>\n" +
            "  </record>\n" +
            "  <resumptionToken completeListSize=\"3201\" cursor=\"0\">XYZ</resumptionToken>\n" +
            " </ListRecords>\n" +
            "</OAI-PMH>";

    private static final String ERROR_RESPONSE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<oai:OAI-PMH xmlns:oai=\"http://www.openarchives.org/OAI/2.0/\" " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ " +
                    "http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">\n" +
            "  <oai:responseDate>2019-05-23T13:01:38Z</oai:responseDate>\n" +
            "  <oai:request>http://memory.loc.gov/cgi-bin/oai</oai:request>\n" +
            "  <oai:error code=\"badVerb\">Illegal OAI verb</oai:error>\n" +
            "</oai:OAI-PMH>";

    private static final ZonedDateTime LOCAL_TIME =
            ZonedDateTime.of(2002, 02, 8, 12, 0, 1, 0, ZoneId.of("Europe/Copenhagen"));

    private final OaiConnector oaiConnector = createOaiConnector();

    @Test
    public void invalidResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/?verb=Identify"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("not XML")
            ));

        try {
            oaiConnector.identify();
            fail("No OaiConnectorException thrown");
        } catch (OaiConnectorException e) {
            assertThat(e.getMessage(), is("Unable to extract OAI-PMH element from entity"));
        }
    }

    @Test
    public void protocolErrorResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/?verb=Identify"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(ERROR_RESPONSE)
            ));

        try {
            oaiConnector.identify();
            fail("No OaiConnectorException thrown");
        } catch (OaiConnectorException e) {
            assertThat(e.getMessage(), is("Illegal OAI verb"));
        }
    }

    @Test
    public void unexpectedStatusCode() throws OaiConnectorException {
        wireMockServer.stubFor(get(urlEqualTo("/?verb=Identify"))
            .willReturn(aResponse()
                .withStatus(201)
                .withBody(IDENTIFY_RESPONSE)
            ));

        try {
            oaiConnector.identify();
            fail("No OaiConnectorUnexpectedStatusCodeException thrown");
        } catch (OaiConnectorUnexpectedStatusCodeException e) {
            assertThat(e.getStatusCode(), is(201));
        }
    }

    @Test
    public void identify() throws OaiConnectorException {
        wireMockServer.stubFor(get(urlEqualTo("/?verb=Identify"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(IDENTIFY_RESPONSE)
            ));

        final Identify identify = oaiConnector.identify();
        assertThat(identify.getBaseURL(), is("http://memory.loc.gov/cgi-bin/oai"));
    }

    @Test
    public void listRecords() throws OaiConnectorException {
        wireMockServer.stubFor(get(urlEqualTo("/?verb=ListRecords"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(LIST_RECORDS_RESPONSE)
                ));

        final OaiConnector.Params params = new OaiConnector.Params();
        final ListRecords listRecords = oaiConnector.listRecords(params);

        assertThat("resumption token",
                listRecords.getResumptionToken().getValue(), is("XYZ"));
        assertThat("number of records",
                listRecords.getRecords().size(), is(3));
        assertThat("3rd record",
                new String(listRecords.getRecords().get(2).getMetadata().getBytes(), StandardCharsets.UTF_8),
                is("<test:anyThing xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:test=\"uri:test\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">3</test:anyThing>"));
    }

    @Test
    public void getServerCurrentTime() throws OaiConnectorException {
        wireMockServer.stubFor(get(urlEqualTo("/?verb=Identify"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(IDENTIFY_RESPONSE)
            ));

        final ZonedDateTime serverCurrentTime = oaiConnector.getServerCurrentTime();
        assertThat(serverCurrentTime,
                is(ZonedDateTime.of(2002, 02, 8, 12, 0, 1, 0, ZoneId.of("UTC"))));
    }

    @Test
    public void fromParam() {
        final OaiConnector.Params params = new OaiConnector.Params().withFrom(LOCAL_TIME);
        assertThat(params.getFrom().get().toString(), is("2002-02-08T11:00:01Z[UTC]"));
    }

    @Test
    public void untilParam() {
        final OaiConnector.Params params = new OaiConnector.Params().withUntil(LOCAL_TIME);
        assertThat(params.getUntil().get().toString(), is("2002-02-08T11:00:01Z[UTC]"));
    }

    private OaiConnector createOaiConnector() {
        try {
            return OaiConnectorFactory.create(wireMockServerBaseurl);
        } catch (OaiConnectorException e) {
            throw new IllegalStateException(e);
        }
    }
}