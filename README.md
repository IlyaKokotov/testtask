# Bird Monitoring System

## Build and Run Backend
1. **Prerequisites**: Java 11, Maven, Docker.
2. **Launch Database**: `docker-compose up -d`.
3. **Run App**: `cd bird-service && mvn spring-boot:run`.
   - Access API at `http://localhost:8080/api`.
   - H2 Console (if local): `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:file:./data/birddb`).

## Eclipse Plugin Installation
1. Open Eclipse IDE.
2. **Import**: `File -> Import -> General -> Existing Projects into Workspace`. Select the `plugins/` folder.
3. **Dependencies**: Ensure you have the `Eclipse Plugin Development Environment (PDE)` installed.
4. **Running**:
   - Right-click `com.example.bird.ui` -> `Run As` -> `Eclipse Application`.
   - In the new Eclipse instance, go to `Window -> Show View -> Other -> Bird View`.
5. **Update Site Export**:
   - Open `com.example.bird.feature/feature.xml`.
   - Click the "Export Wizard" in the Overview tab.
   - Choose "Archive file" to create the ZIP update site.

## Thread Safety Design
- **Backend**: Uses `@Transactional` via Spring Data JPA. Thread safety verified by `BirdServiceConcurrencyTest.java`.
- **Frontend**: API calls are wrapped in Eclipse `Job` objects to run asynchronously. UI updates are marshalled back to the UI thread using `Display.asyncExec`.