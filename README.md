# Legacy EGP Application

A legacy Java EE (J2EE) enterprise application built with EJB 3.x, JSP/Servlets, and JPA. This multi-module Maven project represents a typical enterprise government portal (EGP) system from the mid-2000s era.

## Architecture

- **egp-core-ejb**: Core business logic with EJB 3.x stateless session beans, JPA entities, and DAOs
- **egp-portal-war**: Web presentation layer with JSP pages and servlets
- **egp-ear**: Enterprise Archive packaging both WAR and EJB JAR modules

## Technology Stack

- Java EE 5/6 (EJB 3.x, JPA 1.0, Servlet 2.5, JSP 2.1)
- Maven 3.x for build management
- JBoss AS 7.x / WebLogic 12c target runtime
- Oracle Database / PostgreSQL
- Log4j for logging
- SOAP web services

## Build & Deploy

```bash
# Build all modules
mvn clean install

# Deploy to JBoss
./scripts/deploy.sh jboss

# Deploy to WebLogic
./scripts/deploy.sh weblogic
```

## Database Setup

```bash
# Initialize database schema
psql -d egp_db -f db/schema.sql
psql -d egp_db -f db/seed.sql
```

## Legacy Characteristics

This application demonstrates typical legacy patterns:

- Tightly coupled architecture
- Session-based authentication
- Direct JDBC in some places alongside JPA
- Mixed servlet/JSP presentation
- Synchronous processing
- Monolithic deployment model
- XML-heavy configuration

## Modernization Candidates

- Microservices decomposition
- REST API migration from SOAP
- Spring Boot conversion
- Cloud-native deployment
- Event-driven architecture
- Modern authentication (OAuth2/JWT)
