# API Documentation Updates

## Quote Routes

### Updated: POST /api/v1/quotes

The `POST /api/v1/quotes` endpoint for creating a new quote has been updated to accept a `QuoteRequest` JSON object instead of multipart form data. This change makes the API more consistent and easier to use.

#### Previous Format (multipart form-data)

```http
POST /api/v1/quotes
Content-Type: multipart/form-data

-F "content=Quote content"
-F "author=Author Name"
-F "category=Category Name"
-F "image=@/path/to/image.jpg"
```

#### Updated Format (application/json)

Now, the endpoint expects a JSON object in the request body:

```http
POST /api/v1/quotes
Content-Type: application/json

{
  "content": "Quote content",
  "author": "Author Name",
  "imageUrl": "https://example.com/image.jpg" (optional),
  "category": "Category Name" (optional)
}
```

