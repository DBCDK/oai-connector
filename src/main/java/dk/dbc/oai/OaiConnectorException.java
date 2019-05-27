/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.oai;

public class OaiConnectorException extends Exception {
    public OaiConnectorException(String message) {
        super(message);
    }

    public OaiConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
