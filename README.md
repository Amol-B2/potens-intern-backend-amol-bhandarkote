# Apartment Recommendation System

Spring Boot API that accepts a renter profile and returns the top apartment matches from a seeded catalogue, along with plain-language reasoning for each match.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- H2 in-memory database
- springdoc OpenAPI / Swagger UI
- JUnit + Spring Boot Test

## What This Project Does

- `POST /recommend` accepts a structured renter profile and returns the top 3 apartment matches.
- `GET /explain/{itemId}` returns the eligibility logic for a single apartment in plain language.
- `/items` provides admin-protected CRUD over the apartment catalogue.
- The database is seeded on startup with 16 apartment listings across multiple cities.

## How To Run

### Prerequisites

- Java `21`
- Maven `3.9+` or the included Maven wrapper

### Configuration

Update the admin token in [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
app.admin.token=change-me-admin-token
```

Replace it with your own value before testing admin routes.

### Start the Application

Using Maven:

```bash
mvn spring-boot:run
```

Using the wrapper:

```bash
# macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Or run `ApartmentRecommendationSystemApplication` directly from IntelliJ.

### Running Tests

```bash
./mvnw test
```

### Default Local URLs

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

H2 connection values:

- JDBC URL: `jdbc:h2:mem:apartments`
- Username: `sa`
- Password: leave blank

## API Endpoints

### Public Endpoints

- `POST /recommend`
- `GET /explain/{itemId}`

### Admin Endpoints

- `GET /items`
- `GET /items/{itemId}`
- `POST /items`
- `PUT /items/{itemId}`
- `DELETE /items/{itemId}`

Admin routes require this header:

```http
x-admin-token: your-token-here
```

## Example Requests

### Recommend Request

```json
{
  "budgetMax": 1800,
  "familySize": 3,
  "city": "Austin",
  "moveInDate": "2026-08-01",
  "hasPets": true,
  "amenitiesWanted": ["parking", "gym"],
  "incomeMonthly": 6000
}
```

### Example Recommend Response Shape

```json
{
  "matches": [
    {
      "id": "apt_002",
      "title": "Riverside Family Flat",
      "city": "Austin",
      "rentMonthly": 1700.00,
      "maxOccupancy": 4,
      "availableFrom": "2026-07-15",
      "petFriendly": true,
      "amenities": ["parking", "gym", "laundry"],
      "minIncomeRequired": 5100.00,
      "description": "Bright two-bedroom flat near the riverside trail with parking, gym access, and in-building laundry.",
      "matchScore": 0.844444,
      "reason": "This apartment matches because ..."
    }
  ]
}
```

## Profile Schema

The recommendation profile uses 7 fields:

- `budgetMax` required
- `familySize` required
- `city` required
- `moveInDate` required
- `hasPets` required
- `amenitiesWanted` optional
- `incomeMonthly` optional

## Recommendation Logic

The matching flow is split into two stages.

### Stage 1: Hard Eligibility Rules

An apartment must pass all applicable hard checks:

- apartment city must match profile city, case-insensitively
- apartment rent must be less than or equal to `budgetMax`
- apartment occupancy must be greater than or equal to `familySize`
- apartment availability date must be on or before `moveInDate`
- if `hasPets` is `true`, the apartment must be pet-friendly
- if `incomeMonthly` is provided, it must meet the apartment's `minIncomeRequired`

If no apartment survives the hard filter, the API returns `200` with an empty `matches` array.

### Stage 2: Weighted Scoring

Eligible apartments are ranked using:

- budget fit weight `0.4`
- amenity overlap weight `0.4`
- space buffer weight `0.2`

Scoring formulas:

- `budgetFit = 1 - (budgetMax - rentMonthly) / budgetMax`
- `amenityOverlap = matchedWantedAmenities / totalWantedAmenities`
- `spaceBuffer = min((maxOccupancy - familySize) / familySize, 1)`

This keeps the ranking from collapsing into a simple "cheapest apartment wins" sort.

## Design Decisions

- `Spring Boot + H2` was chosen to keep the project easy to run locally without external database setup.
- The catalogue is seeded on startup so the application is immediately usable after launch.
- Hard filtering and scoring are separated into different services so the matching logic is easier to test and explain.
- Explanation text is generated from rule outcomes and apartment attributes rather than being handwritten per listing.
- Admin protection uses a simple `x-admin-token` header because the assignment only requires token protection, not a full auth system.
- Amenities are stored as an element collection so each apartment can have flexible amenity lists without extra entity complexity.

## Test Coverage

Implemented test areas include:

- eligibility boundary behavior
- optional income-field behavior
- pet-policy filtering
- scoring calculations
- no-match API behavior
- validation error handling
- missing item explain route
- admin-token protection
- authenticated CRUD happy path

All tests pass on Java 21 via `./mvnw test`.

## Known Limitations / Next Steps

- The admin token is stored in `application.properties` for simplicity. In a production-ready version, this should come from environment variables or secrets management.
- The project includes OpenAPI support through Swagger UI, but no custom API descriptions or tags were added.
- The optional stretch goals were not implemented:
  - subscribe webhook
  - caching layer
  - extra OpenAPI customization beyond auto exposure

## What I Would Build Next

- move the admin token into environment-based configuration
- add response models for admin CRUD instead of exposing the entity directly
- add custom OpenAPI documentation for each endpoint
- add pagination or filtering support to `/items`
- add caching for repeated recommendation requests
- add a subscription feature for profile-based listing alerts

## Project Structure

```text
src/main/java/com/apartmentrecommendation
  config/
  controller/
  dto/
  entity/
  exception/
  repository/
  service/
```

## AI USE LOG

- `OpenAI Codex`
  Approximate usage: `60-80 messages`
  Used for project planning, API design review, Spring Boot implementation, test scaffolding, and README drafting.