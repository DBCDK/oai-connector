/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.oai;

import dk.dbc.httpclient.HttpClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

/**
 * OaiConnector factory
 * <p>
 * Synopsis:
 * </p>
 * <pre>
 *    // New instance
 *    OaiConnector connector = OaiConnectorFactory.create("http://oai-service");
 *
 *    // Singleton instance in CDI enabled environment
 *    {@literal @}Inject
 *    OaiConnectorFactory factory;
 *    ...
 *    OaiConnector connector = factory.getInstance();
 *
 *    // or simply
 *    {@literal @}Inject
 *    OaiConnector connector;
 * </pre>
 * <p>
 * The CDI case depends on the OAI service base-url being defined as
 * the value of either a system property or environment variable
 * named OAI_SERVICE_URL.
 * </p>
 */
@ApplicationScoped
public class OaiConnectorFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(OaiConnectorFactory.class);

    public static OaiConnector create(String baseUrl) throws OaiConnectorException {
        final Client client = HttpClient.newClient();
        LOGGER.info("Creating OaiConnector for: {}", baseUrl);
        return new OaiConnector(client, baseUrl);
    }

    @Inject
    @ConfigProperty(name = "OAI_SERVICE_URL")
    private String baseUrl;

    OaiConnector oaiConnector;

    @PostConstruct
    public void initializeConnector() {
        try {
            oaiConnector = OaiConnectorFactory.create(baseUrl);
        } catch (OaiConnectorException e) {
            throw new IllegalStateException(e);
        }
    }

    @Produces
    public OaiConnector getInstance() {
        return oaiConnector;
    }

    @PreDestroy
    public void tearDownConnector() {
        oaiConnector.close();
    }
}
