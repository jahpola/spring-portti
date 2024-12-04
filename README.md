# Spring Cloud Gateway Application with Kubernetes Integration

This project is a Spring Cloud Gateway application named "Portti" designed for seamless integration with Kubernetes environments. It provides a robust API gateway solution with service discovery, load balancing, and observability features.

Portti is built using Spring Boot and Spring Cloud, leveraging the power of Spring Cloud Gateway for routing and Spring Cloud Kubernetes for native Kubernetes integration. The application is containerized and can be easily deployed to Kubernetes clusters using provided manifests and Skaffold configurations.

Key features of this project include:
- Dynamic service discovery in Kubernetes environments
- Configurable routing and load balancing
- Integration with OpenTelemetry for distributed tracing
- Health probes for Kubernetes readiness and liveness checks
- Graceful shutdown support
- Prometheus metrics exposure
- Customizable deployment options for local development and production environments

The project is structured to support different deployment scenarios, including local development using Rancher Desktop and deployment to AWS-based Kubernetes clusters. It includes Kustomize configurations for managing environment-specific settings and an AWS Application Load Balancer (ALB) Ingress setup for production deployments.

## Repository Structure

```
.
├── build.gradle                 # Gradle build configuration
├── gradlew.bat                  # Gradle wrapper for Windows
├── manifests/                   # Kubernetes manifests
│   ├── base/                    # Base Kubernetes configurations
│   └── overlays/                # Environment-specific overlays
│       ├── dev/                 # Development environment config
│       └── local/               # Local development config
├── skaffold.yaml                # Skaffold configuration for CI/CD
├── src/
│   ├── main/
│   │   ├── java/                # Java source code
│   │   └── resources/           # Application properties and configs
│   └── test/                    # Test source code
└── README.md                    # This file
```

## Usage Instructions

### Prerequisites

- Java Development Kit (JDK) 21
- Gradle 8.x (included via wrapper)
- Docker
- Kubernetes cluster (local or remote)
- Skaffold

### Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   cd spring-portti
   ```

2. Build the project:
   ```
   ./gradlew build
   ```

3. Build and push the Docker image (if not using Skaffold):
   ```
   ./gradlew jib
   ```

### Running the Application

#### Local Development

For local development using Rancher Desktop:

```
skaffold dev --profile=local
```

This command will build the application, deploy it to your local Kubernetes cluster, and set up port forwarding.

#### Deployment to Development Environment

To deploy to a development Kubernetes cluster:

1. Ensure your kubectl context is set to the correct cluster.
2. Apply the Kubernetes manifests:
   ```
   kubectl apply -k manifests/overlays/dev
   ```

### Configuration

The application can be configured using the following files:

- `src/main/resources/application.yaml`: Main application configuration
- `src/main/resources/application-kubernetes.yaml`: Kubernetes-specific configuration

Key configuration options:

- `spring.application.name`: Set to "portti"
- `management.endpoints.web.exposure.include`: Exposes all actuator endpoints
- `management.tracing.sampling.probability`: Set to 1.0 for full tracing
- `management.otlp.tracing.endpoint`: Configure the OpenTelemetry endpoint

### Testing

Run the tests using:

```
./gradlew test
```

### Troubleshooting

1. If the application fails to start in Kubernetes:
   - Check the pod logs: `kubectl logs -l app=portti`
   - Verify the readiness and liveness probe endpoints are accessible

2. For networking issues:
   - Ensure the Kubernetes service is correctly configured
   - Check the Ingress resource configuration in `manifests/overlays/dev/ingress.yaml`

3. To enable debug logging:
   - Uncomment the logging configuration in `src/main/resources/application.yaml`
   - Redeploy the application

## Data Flow

The request data flow through the Portti application is as follows:

1. External request reaches the Kubernetes Ingress (ALB in AWS environments)
2. Request is forwarded to the Portti service
3. Spring Cloud Gateway receives the request
4. Gateway routes the request based on configured rules
5. If enabled, service discovery locates the target service
6. Request is forwarded to the appropriate backend service
7. Response follows the reverse path back to the client

```
[Client] <-> [Ingress] <-> [Portti Service] <-> [Spring Cloud Gateway] <-> [Backend Services]
```

Note: The gateway integrates with Kubernetes service discovery to dynamically route requests to available services.

## Deployment

### Prerequisites

- Access to a Kubernetes cluster
- `kubectl` configured to access the cluster
- Docker registry access (e.g., AWS ECR)

### Deployment Steps

1. Build and push the Docker image:
   ```
   ./gradlew jib
   ```

2. Update the image reference in `manifests/base/deployment.yaml` if necessary.

3. Apply the Kubernetes manifests:
   ```
   kubectl apply -k manifests/overlays/dev
   ```

4. Verify the deployment:
   ```
   kubectl get pods -l app=portti
   ```

### Environment Configurations

- Local: Uses `manifests/overlays/local` with Ingress configured for local access
- Development: Uses `manifests/overlays/dev` with ALB Ingress for AWS environments

## Infrastructure

The application uses the following key infrastructure components:

- Kubernetes Deployment:
  - Type: `apps/v1/Deployment`
  - Name: `portti`
  - Runs the Portti application container
  - Configures readiness and liveness probes

- Kubernetes Service:
  - Type: `v1/Service`
  - Name: `portti`
  - Exposes the Portti application on port 8080

- Kubernetes Ingress (Development):
  - Type: `networking.k8s.io/v1/Ingress`
  - Name: `portti`
  - Configures ALB for internet-facing access
  - Restricts inbound traffic to specific CIDR

- Kubernetes ConfigMap:
  - Type: `v1/ConfigMap`
  - Name: `portti`
  - Stores configuration data for the application

- Kubernetes Role and RoleBinding:
  - Grants necessary permissions for the Portti service account

- Kubernetes ServiceAccount:
  - Name: `portti`
  - Used by the Portti deployment for Kubernetes API access