# Bird Monitoring System

A distributed system for tracking bird sightings.

## 1. Backend Service (bird-service)
### Features:
- **CRUD Operations**: Complete support for Birds and Sightings.
- **Thread Safety**: Verified with concurrent execution tests.
- **Database**: H2 (local) or PostgreSQL (Docker).

### Build and Test
    cd bird-service
    mvn test          # Runs unit and Concurrency tests
    mvn clean package

### Concurrency / Velocity Test
Check `BirdServiceConcurrencyTest.java` in the test folder. It validates that the repository handles simultaneous write requests without data loss.

## 2. Eclipse Client & Feature
### Delivery:
The project is split into three parts in the `plugins/` directory:
1. `com.example.bird.client`: Logic and REST communication.
2. `com.example.bird.ui`: SWT Views and User Interface.
3. `com.example.bird.feature`: An Eclipse Feature project to package the plugins for an Update Site.

### UI Functionality:
- **Birds Tab**: List all birds and add new bird records.
- **Sightings Tab**: Filter sightings by bird name and view in table.

### Installation in Eclipse:
1. Import all projects from `plugins/` into your Eclipse workspace.
2. Open `com.example.bird.feature/feature.xml`.
3. Use the "Export Wizard" to create a deployable archive (Update Site).
4. Install via `Help -> Install New Software -> Add -> Archive`.