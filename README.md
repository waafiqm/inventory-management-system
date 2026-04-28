# Inventory Management System

A cloud-deployed microservices simple inventory management web app where you can log in, view products, record sales, update stock levels, and monitor low-stock items. Nothing crazy, but it's running on Google Cloud with real containerization and messaging under the hood.

## Services

**FrontendService** — The UI. JSP/Servlet-based web app that handles login and lets users interact with the inventory. Talks to the backend services via REST.

**InventoryService** — The core backend. Manages all products, stock levels, and sales records. Exposes a REST API consumed by the frontend.

**LowStockService** — Monitors which products have fallen below their restock threshold. Keeps its own database that stays in sync with inventory through async messaging.

## Tech Stack

- **Java** — All three services
- **Apache Tomcat 8.5** — Servlet container
- **MySQL 8** — Each service has its own database (Account_IMS, Inventory_IMS, LowStock_IMS)
- **Jersey (JAX-RS)** — REST APIs for InventoryService and LowStockService
- **KubeMQ** — Async messaging broker
- **Docker** — Each service and database is containerized
- **Kubernetes (GKE)** — Orchestrates all containers on Google Cloud
- **Maven** — Build tool

## Key Concepts

### Microservices Architecture
Three independently deployable services, each with its own database. They only talk to each other through well-defined interfaces — REST for synchronous calls, KubeMQ for async events.

### Asynchronous Messaging
When a sale is recorded or stock is updated, InventoryService publishes a message to a KubeMQ channel called `stock_update_channel`. LowStockService subscribes to that channel in a background thread and updates its own database when a message arrives. Neither service waits on the other — they're fully decoupled.

### Environment Variables
No hardcoded addresses anywhere. All service URLs and database connections are injected at runtime through Kubernetes environment variables (`DB_URL`, `inventoryService`, `lowStockService`, `kubeMQAddress`). This is what makes the same code work locally and on the cloud.

### Containerization & Deployment
Every service and database runs in its own Docker container. Persistent Volume Claims (PVCs) are used for all three databases so data survives pod restarts. The frontend is exposed publicly through a Kubernetes LoadBalancer service.

Check out the Docker project here: https://hub.docker.com/r/waafiqmaz/waafiqims

## Project Structure
- FrontendService/       # JSP/Servlet UI
- InventoryService/      # Core REST backend 
- LowStockService/       # Low stock monitor 
- Docker-IMSMicroservices/  # All Dockerfiles + SQL dumps 
- kube/ # All deployments, PVCs, internal services 
