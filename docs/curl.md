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

## Create Quote

creating a quote (this should fail for a regular user):

curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU" \
-H "Content-Type: application/json" \
-d '{
"content": "This is a test quote",
"author": "Test Author"
}'

## Create Quote with Image
path: /Users/ask/PROJECTS/ASSETS/IMAGE/image.jpg
curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU" \
-H "Content-Type: multipart/form-data" \
-F "content=This is a test quote" \
-F "author=Test Author" \
-F "image=@/Users/ask/PROJECTS/ASSETS/IMAGE/image.jpg"

## Update Quote

curl -X PUT http://localhost:8080/quotes/32007 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU" \
-H "Content-Type: application/json" \
-d '{
"content": "This is an updated test quote",
"author": "Updated Test Author"
}'

## Get Quotes
c
curl -X GET http://localhost:8080/quotes

# Get Quotes with pagination

curl -X GET "http://localhost:8080/quotes?page=1&pageSize=10"

## Get Quote by ID

Requires auth:

curl -X GET http://localhost:8080/quotes/2008 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU"

## Login as an admin user

curl -X POST http://localhost:8080/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "admin",
"password": "admin123"
}'

## Login as a user

curl -X POST http://localhost:8080/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "testuser",
"password": "testpassword"
}'

## Create Quote as an admin user

curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzI4MDE3MDg4fQ.xK85OBOuS-8J9S3rsmStojEczlBeqD6IGQaoGJnVKmU" \
-H "Content-Type: application/json" \
-d '{
"content": "This is an admin quote",
"author": "Admin"
}'

## Get Quotes by Category

curl -X GET "http://localhost:8080/category/Science?page=1&pageSize=10"

## Create Quote with Category

curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU" \
-H "Content-Type: multipart/form-data" \
-F "content=E = mc^2" \
-F "author=Albert Einstein" \
-F "category=Science"

## Update Quote with Category

curl -X PUT http://localhost:8080/quotes/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU" \
-H "Content-Type: application/json" \
-d '{
"content": "Updated quote content",
"author": "Updated Author",
"category": "Updated Category"
}'

## Get All Quotes (now including category)

curl -X GET "http://localhost:8080/quotes?page=1&pageSize=10"

## Get Quote by ID (now including category)

curl -X GET http://localhost:8080/quotes/1 \
-H "Authorization: Bearer YOUR_JWT_TOKEN"

## Create Quote with Image and Category

curl -X POST http://localhost:8080/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgwODYxNTV9.XJtzxKHLXkYjydkfCouw-wxUC-8VstRb6dFcBB22FhU" \
-H "Content-Type: multipart/form-data" \
-F "content=A picture is worth a thousand words" \
-F "author=Unknown" \
-F "category=Photography" \
-F "image=@/path/to/your/image.jpg"

