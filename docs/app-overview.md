## Recent Changes

- Refactored the quote creation route in `QuoteRoutes.kt`:
  - Extracted multipart parsing logic into a separate function `receiveQuoteMultipart`
  - Created a new data class `QuoteMultipartData` to hold parsed multipart data
  - Improved code organization and readability in the `post` route