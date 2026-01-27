# Bird Monitoring System

## Build and Run Backend
1. **Prerequisites**: Java 11, Maven, Docker.
2. **Launch Database**: `docker-compose up -d`.
3. **Run App**: `cd bird-service && mvn spring-boot:run`.
   - Access API at `http://localhost:8080/api`.
   - Swagger Documentation: `http://localhost:8080/swagger-ui/index.html`.
   - H2 Console: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:file:./data/birddb`).

## Eclipse Plugin Installation
1. **IDE Version**: Use **Eclipse IDE for RCP and RAP Developers**. 
   - *Note*: If using "Eclipse IDE for Java Developers", you MUST install "PDE (Plugin Development Environment)" from the Eclipse Marketplace first.
2. **Importing Projects**:
   - Go to `File -> Import...`
   - Select `General -> Existing Projects into Workspace` and click `Next`.
   - For **Select root directory**, click `Browse` and select the **root folder of this repository** (the folder containing the `plugins/` directory).
   - **CRITICAL**: Ensure the checkbox **"Search for nested projects"** is **CHECKED**.
   - You should now see three projects in the list:
     - `com.example.bird.client`
     - `com.example.bird.feature`
     - `com.example.bird.ui`
   - Click `Finish`.
3. **Running**:
   - Right-click the `com.example.bird.ui` project -> `Run As` -> `Eclipse Application`.
   - In the new Eclipse instance that opens: `Window -> Show View -> Other -> Bird View`.
