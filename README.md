### **Purchasing Service**

Steps to run project on docker :

0 - Go to Purchasing Service **project folder** in terminal
  cd /Users/muratsimsek/github/PurchasingService 

1 - All tests will run <br/>
  **docker compose build**

2 - Postgres DB, Kafka, Zookeeper and Purchasing_Service containers will be up silently <br/>
  **docker compose up -d**

3- Shut down containers <br/>
  **docker compose down**


### Swagger Documentation Address
  http://localhost:8080/swagger-ui/index.html

### Java Doc Files
  mvn javadoc:javadoc , after execution of the command (It was run by me)
  you can find the generated java doc file on **/docs/javadoc** directory

### CURL Commands

### Postman Collection
  you can find under **/docs/postman** directory
