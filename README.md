# Bird Monitoring System

### API Documentation & Swagger
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Build and Run Backend
1. **Prerequisites**: Java 11, Maven, Docker.
2. **Launch Database**: `docker-compose up -d`.
3. **Run App**: `cd bird-service && mvn spring-boot:run`.
   - Access API at `http://localhost:8080/api`.
   - H2 Console: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:file:./data/birddb`).

## Eclipse Plugin Installation
1. **IDE Version**: Use **Eclipse IDE for RCP and RAP Developers**.
2. **Importing Projects**:
   - Go to `File -> Import...`
   - Select `General -> Existing Projects into Workspace`.
   - Select the root folder of this repository.
   - **CHECK** "Search for nested projects".
   - Import `com.example.bird.client`, `com.example.bird.feature`, and `com.example.bird.ui`.