# write curl commands here

## Register

curl -X POST http://localhost:8080/api/v1/register \
 -H "Content-Type: application/json" \
 -d '{
"username": "testuser",
"password": "testpassword"
}'

## Login

curl -X POST http://localhost:8080/api/v1/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "testuser",
"password": "testpassword"
}'

## JWT Token

## Create Quote

creating a quote (this should fail for a regular user):

curl -X POST http://localhost:8080/api/v1/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: application/json" \
-d '{
"content": "This is a test quote",
"author": "Test Author"
}'

## Create Quote with Image
path: /Users/ask/PROJECTS/ASSETS/IMAGE/image.jpg
curl -X POST http://localhost:8080/api/v1/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "X-API-Key: GYatWWwnXL0iVwNLdniBs_fom24HuOyeZ_KwnGdSPOk" \
-H "Content-Type: multipart/form-data" \
-F "content=This is a test quote" \
-F "author=Test Author" \
-F "image=@/Users/ask/PROJECTS/ASSETS/IMAGE/image.jpg"

## Update Quote

curl -X PUT http://localhost:8080/api/v1/quotes/32007 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: application/json" \
-d '{
"content": "This is an updated test quote",
"author": "Updated Test Author"
}'

## Get Quotes

curl -X GET http://localhost:8080/api/v1/quotes

# Get Quotes with pagination

curl -X GET "http://localhost:8080/api/v1/quotes?page=1&pageSize=10"

## Get Quote by ID

Requires auth:

curl -X GET http://localhost:8080/api/v1/quotes/2008 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k"

## Login as an admin user

curl -X POST http://localhost:8080/api/v1/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "admin",
"password": "admin123"
}'

## Login as a user

curl -X POST http://localhost:8080/api/v1/login \
 -H "Content-Type: application/json" \
 -d '{
"username": "testuser",
"password": "testpassword"
}'

## Create Quote as an admin user

curl -X POST http://localhost:8080/api/v1/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImlkIjoxLCJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIiwiZXhwIjoxNzI4MDE3MDg4fQ.xK85OBOuS-8J9S3rsmStojEczlBeqD6IGQaoGJnVKmU" \
-H "Content-Type: application/json" \
-d '{
"content": "This is an admin quote",
"author": "Admin"
}'

## Get Quotes by Category

curl -X GET "http://localhost:8080/api/v1/category/Science?page=1&pageSize=10"

## Create Quote with Category

curl -X POST http://localhost:8080/api/v1/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: multipart/form-data" \
-F "content=E = mc^2" \
-F "author=Albert Einstein" \
-F "category=Science"

## Update Quote with Category

curl -X PUT http://localhost:8080/api/v1/quotes/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: application/json" \
-d '{
"content": "Updated quote content",
"author": "Updated Author",
"category": "Updated Category"
}'

## Get All Quotes (now including category)

curl -X GET "http://localhost:8080/api/v1/quotes?page=1&pageSize=10"

## Get Quote by ID (now including category)

curl -X GET http://localhost:8080/api/v1/quotes/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k"

## Create Quote with Image and Category

curl -X POST http://localhost:8080/api/v1/quotes \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: multipart/form-data" \
-F "content=A picture is worth a thousand words" \
-F "author=Unknown" \
-F "category=Photography" \
-F "image=@/Users/ask/PROJECTS/ASSETS/IMAGE/image.jpg"

## Dictionary API

## Get All Dictionary Entries

curl -X GET "http://localhost:8080/api/v1/dictionary"

## Get Dictionary Entry by ID

curl -X GET "http://localhost:8080/api/v1/dictionary/1"

## Search Dictionary Entries (case-insensitive)

curl -X GET "http://localhost:8080/api/v1/dictionary/search?q=ALGORITHM"

## Create Dictionary Entry (requires authentication)

curl -X POST "http://localhost:8080/api/v1/dictionary" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: application/json" \
-d '{
"name": "Algorithm",
"definition": "A step-by-step procedure or formula for solving a problem.",
"examples": ["Sorting algorithms", "Search algorithms"],
"relatedTerms": ["Data structure", "Complexity"],
"tags": ["Computer Science", "Programming"],
"category": "Computer Science",
"languages": ["General"]
}'

## Update Dictionary Entry (requires authentication)

curl -X PUT "http://localhost:8080/api/v1/dictionary/1" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VycyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k" \
-H "Content-Type: application/json" \
-d '{
  "name": "Updated Algorithm",
  "definition": "An updated step-by-step procedure or formula for solving a problem.",
  "examples": ["Updated sorting algorithms", "Updated search algorithms"],
  "relatedTerms": ["Updated data structure", "Updated complexity"],
  "tags": ["Updated Computer Science", "Updated Programming"],
  "category": "Updated Computer Science",
  "languages": ["Updated General"]
}'

## Delete Dictionary Entry (requires authentication)

curl -X DELETE "http://localhost:8080/api/v1/dictionary/1" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6InF1b3RlLWFwcCIsImF1ZCI6InF1b3RlLWFwcC11c2VrcyIsImlkIjozLCJ1c2VybmFtZSI6InRlc3R1c2VyIiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MjgyMDQ5MjV9.uGQlVHPfo80TfmPkL_NSVwveeUin96ep32QPcNEm57k"