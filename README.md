### **Purchasing Service**

Steps to run project on docker :

0 - Go to Purchasing Service **project folder** in terminal cd /Users/muratsimsek/github/PurchasingService

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

### Postman Collection

you can find under **/docs/postman** directory

### cURL Commands Examples

    GET BEARER TOKEN

	curl -L 'http://localhost:8080/api/auth/login' -H 'Content-Type: application/json' --data-raw '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@doe.com",
    "password": "pwd123"}'

<br/>

    ADD PRODUCT

    curl -L 'http://localhost:8080/api/products' -H 'Content-Type: application/json' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006' 
    -d '{"name": "Laptop 2","description": "laptop 2"}'

<br/>

    UPDATE PRODUCT

    curl -L -X PUT 'http://localhost:8080/api/products/Laptop' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006'

<br/>

    FIND PRODUCT

    curl -L 'http://localhost:8080/api/products/Laptop' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006'

<br/>

    REMOVE PRODUCT

	curl -L -X DELETE 'http://localhost:8080/api/products/Laptop' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006'

<br/>

    FIND ALL PRODUCTS

	curl -L 'http://localhost:8080/api/products' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006'

<br/>

    SUBMIT INVOICE

	curl -L 'http://localhost:8080/api/invoices' 
    -H 'Content-Type: application/json' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    --data-raw '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@doe.com",
    "amount": 500,
    "productName": "Laptop",
    "billNo": "TR000123"
    }'

<br/>

    FIND ALL INVOICES

	curl -L 'http://localhost:8080/api/invoices' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006'

<br/>

    FIND ALL APPROVED INVOICES

	curl -L 'http://localhost:8080/api/invoices/approved' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006' -d ''

<br/>

    FIND ALL REJECTED INVOICES

	curl -L 'http://localhost:8080/api/invoices/rejected' 
    -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MjgyNzg2NDU4NzAsImV4cCI6MTcyODMxNDY0NTg3MH0=.E4cMP6rsMbtdoSBWX8eLeD-uhLrYrxXKQgNk2dQAqCDzrNn1qkNBP9djGAebJHvcOtdmK-ZkYAUPbdZfQ88X90sT6z8bV0HPKy3NFjzRrSG66T5n8Q2sGCbiLsDsKC9nUTQYD-ZcGaHeJWcRaZXD5DciOWn8kxcdaUwYKwpSm8H6hVhm268ct38JSJ-rTfbL9Uuy6EGCTM_wJU3SpcX_oi-rhxJqZ1dK5EmL1ED0COAN2GU1JbvdMxxLGM3lwa7baS7NxeBUfMfCbHKyAnI4r8dkLcwRiZLkZuFxisbgCHguMaYf2Ug8nYBH_7pOhWcVqJMhn7Zh1NlZEgn_d6nheg' 
    -H 'Cookie: JSESSIONID=A65500D6471D762D3F8F6A4B19BF8006'
