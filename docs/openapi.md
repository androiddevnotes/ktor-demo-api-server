# API Documentation Updates

## Quote Routes

### POST /api/v1/quotes

The POST endpoint for creating a new quote has been updated to use a `QuoteRequest` object instead of multipart form data. This change simplifies the API and makes it more consistent with other endpoints.

#### Request Body

The request body now expects a JSON object with the following structure:

