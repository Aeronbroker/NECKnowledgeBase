# NEC IoT Knowledge
NEC IoT Knowledge server for enabling semantic functionalities.

Steps to use the IoT Knowledge Server:
---

1. Download the latest version of Apache Fuseki (tested with Fuseki 2.4.0)
2. Run the executable jar file (fuseki-server.jar) in the Fuseki directory.
3. Open browser and open the Fuseki page. (http://localhost:3030/)
4. Create a new dataset named "\Subscription"
5. Upload the file "SmartSantanderNGSI-RDF.owl" (or any other ontologies)
(included in \KnowledgeBaseServer\NGSI_Sparql_Examples folder)
6. Open SystemParameters.txt file and set the configurations as you wish
7. Set the server port number in the "application.properties" file under /src/main/resources/
8. Clean and install KnowledgeBaseServer with Maven
9. Run it as a SpringBoot app (Command Line: mvn spring-boot:run / Eclipse: Run KnowledgeBaseApplication.java class --> main spring boot class)
10. Check the ExampleQueries.txt file to try sample HTTP queries6- Open SystemParameters.txt file and set the configurations as you wish 