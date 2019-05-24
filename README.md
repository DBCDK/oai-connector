oai-connector
=============

A Java library providing a OAI-PMH client.

### usage

Add the dependency to your Maven pom.xml

```xml
<dependency>
  <groupId>dk.dbc</groupId>
  <artifactId>oai-connector</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
 In your Java code

```java
import dk.dbc.oai.OaiConnector;
import javax.inject.Inject;
...

// Assumes environment variable OAI_SERVICE_URL
// is set to the base URL of the OAI provider service.
@Inject OaiConnector oaiConnector;

Identify identify = oaiConnector.identify();

ListRecords listRecords = oaiConnector.listRecords(
        new OaiConnector.Params()
                .withSet("mySet")
                .withFrom(Instant.now().atZone(ZoneId.of("Europe/Copenhagen"))));

for(Record record : listRecords.getRecords()) {
    record.getMetadata().getBytes();
}
```

### development

**Requirements**

To build this project JDK 1.8 or higher and Apache Maven is required.

### License

Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3.
See license text in LICENSE.txt