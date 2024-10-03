# write curl commands here

## Register

curl -X POST http://localhost:8080/register \
 -H "Content-Type: application/json" \
 -d '{
"username": "testuser",
"password": "testpassword"
}'

## Login

curl -X POST http://localhost:8080/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "testuser",
"password": "testpassword"
}'

## JWT Token

curl -X GET http://localhost:8080/quotes \
 -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwMTY3NDB9.TdGQjAL87iQwZ7j1Yyko9JMGFR7RwRB2N3NeMzshz98"

## Create Quote

creating a quote (this should fail for a regular user):

curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwMTY3NDB9.TdGQjAL87iQwZ7j1Yyko9JMGFR7RwRB2N3NeMzshz98" \
-H "Content-Type: application/json" \
-d '{
"content": "This is a test quote",
"author": "Test Author"
}'

## Login as an admin user

curl -X POST http://localhost:8080/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "admin",
"password": "admin123"
}'

## Create Quote as an admin user

curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzI4MDE3MDg4fQ.xK85OBOuS-8J9S3rsmStojEczlBeqD6IGQaoGJnVKmU" \
-H "Content-Type: application/json" \
-d '{
"content": "This is an admin quote",
"author": "Admin"
}'
