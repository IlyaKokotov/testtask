# Bird Service REST API Documentation

## 1. Birds
- **GET /api/birds**: List all birds.
  - Query Params: `name` (optional), `color` (optional).
- **POST /api/birds**: Create a new bird.
  - Body: `{"name": "Robin", "color": "Red", "weight": 25.0, "height": 12.5}`
- **GET /api/birds/{id}**: Get bird details.
- **PUT /api/birds/{id}**: Update bird details.
- **DELETE /api/birds/{id}**: Remove a bird record.

## 2. Sightings
- **GET /api/sightings**: List all sightings.
  - Query Params: `birdName`, `location`, `start` (ISO Date), `end` (ISO Date).
- **POST /api/sightings**: Create a new sighting.
  - Body: `{"bird": {"id": 1}, "location": "Park", "dateTime": "2023-10-27T10:00:00"}`