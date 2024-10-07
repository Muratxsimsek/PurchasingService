Purchasing Service

Steps to run project on docker :

0 - Go to Purchasing Service project folder in terminal
  cd /Users/muratsimsek/github/PurchasingService 

1 - All tests will run 
  docker compose build

2 - Postgres DB, Kafka, Zookeeper and Purchasing_Service containers will be up silently
  docker compose up -d

3- Shut down containers
  docker compose down


Swagger Documantation Address
  http://localhost:8080/swagger-ui/index.html

Java Doc Files
  mvn javadoc:javadoc , after execution of the command (It was run by me)
  you can find the generated java doc file on **/docs/javadoc** directory
