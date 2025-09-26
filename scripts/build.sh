#!/bin/bash

# Legacy EGP Application Build Script
# Supports both JBoss AS 7.x and WebLogic 12c deployments

set -e

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$PROJECT_ROOT/target"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Logging function
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
    exit 1
}

# Check prerequisites
check_prerequisites() {
    log "Checking build prerequisites..."
    
    # Check Java version
    if ! command -v java &> /dev/null; then
        error "Java is not installed or not in PATH"
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n1 | cut -d'"' -f2)
    log "Java version: $JAVA_VERSION"
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        error "Maven is not installed or not in PATH"
    fi
    
    MVN_VERSION=$(mvn -version | head -n1)
    log "Maven version: $MVN_VERSION"
    
    # Check JAVA_HOME
    if [ -z "$JAVA_HOME" ]; then
        warn "JAVA_HOME is not set. This may cause issues with some application servers."
    else
        log "JAVA_HOME: $JAVA_HOME"
    fi
}

# Clean previous builds
clean_build() {
    log "Cleaning previous build artifacts..."
    
    cd "$PROJECT_ROOT"
    mvn clean
    
    # Remove any legacy build artifacts
    find . -name "*.class" -type f -delete
    find . -name "*.jar" -path "*/target/*" -delete
    find . -name "*.war" -path "*/target/*" -delete
    find . -name "*.ear" -path "*/target/*" -delete
    
    log "Clean completed"
}

# Compile source code
compile_sources() {
    log "Compiling source code..."
    
    cd "$PROJECT_ROOT"
    
    # Set Maven options for legacy compatibility
    export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"
    
    # Compile all modules
    mvn compile
    
    log "Compilation completed"
}

# Run tests
run_tests() {
    log "Running unit tests..."
    
    cd "$PROJECT_ROOT"
    
    # Run tests with legacy settings
    mvn test -Dmaven.test.failure.ignore=false
    
    # Generate test reports
    if [ -d "target/surefire-reports" ]; then
        log "Test reports generated in target/surefire-reports/"
    fi
    
    log "Tests completed"
}

# Package applications
package_applications() {
    log "Packaging applications..."
    
    cd "$PROJECT_ROOT"
    
    # Package all modules
    mvn package -DskipTests
    
    # Verify EAR file was created
    EAR_FILE="$PROJECT_ROOT/egp-ear/target/egp-ear-1.0.0-SNAPSHOT.ear"
    if [ ! -f "$EAR_FILE" ]; then
        error "EAR file was not created: $EAR_FILE"
    fi
    
    log "EAR file created: $EAR_FILE"
    
    # Create deployment directory
    mkdir -p "$BUILD_DIR/deployment"
    
    # Copy deployment artifacts
    cp "$EAR_FILE" "$BUILD_DIR/deployment/"
    cp "$PROJECT_ROOT/egp-portal-war/target/egp-portal-war-1.0.0-SNAPSHOT.war" "$BUILD_DIR/deployment/" 2>/dev/null || true
    cp "$PROJECT_ROOT/egp-core-ejb/target/egp-core-ejb-1.0.0-SNAPSHOT.jar" "$BUILD_DIR/deployment/" 2>/dev/null || true
    
    log "Packaging completed"
}

# Generate deployment descriptor for WebLogic
generate_weblogic_descriptors() {
    log "Generating WebLogic deployment descriptors..."
    
    # This would typically involve creating weblogic-application.xml
    # and other WebLogic-specific descriptors
    
    warn "WebLogic descriptor generation not implemented in this legacy build"
}

# Create backup of current deployment
backup_current_deployment() {
    if [ "$1" = "prod" ]; then
        log "Creating backup of current production deployment..."
        
        BACKUP_DIR="/opt/backups/egp/$TIMESTAMP"
        mkdir -p "$BACKUP_DIR"
        
        # Backup current EAR if it exists
        if [ -f "/opt/deployments/egp-ear.ear" ]; then
            cp "/opt/deployments/egp-ear.ear" "$BACKUP_DIR/"
            log "Backup created: $BACKUP_DIR/egp-ear.ear"
        fi
    fi
}

# Main build function
main() {
    log "Starting Legacy EGP Application build..."
    log "Build timestamp: $TIMESTAMP"
    
    # Parse command line arguments
    SKIP_TESTS=false
    TARGET_ENV="dev"
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --skip-tests)
                SKIP_TESTS=true
                shift
                ;;
            --env)
                TARGET_ENV="$2"
                shift 2
                ;;
            --help)
                echo "Usage: $0 [--skip-tests] [--env dev|test|prod] [--help]"
                echo ""
                echo "Options:"
                echo "  --skip-tests    Skip unit tests during build"
                echo "  --env ENV       Target environment (dev, test, prod)"
                echo "  --help          Show this help message"
                exit 0
                ;;
            *)
                error "Unknown option: $1"
                ;;
        esac
    done
    
    log "Target environment: $TARGET_ENV"
    
    # Execute build steps
    check_prerequisites
    clean_build
    compile_sources
    
    if [ "$SKIP_TESTS" = false ]; then
        run_tests
    else
        warn "Skipping tests as requested"
    fi
    
    package_applications
    
    if [ "$TARGET_ENV" = "prod" ]; then
        backup_current_deployment "$TARGET_ENV"
    fi
    
    # Generate environment-specific configurations
    case $TARGET_ENV in
        "weblogic")
            generate_weblogic_descriptors
            ;;
    esac
    
    log "Build completed successfully!"
    log "Deployment artifacts available in: $BUILD_DIR/deployment/"
    
    # Display build summary
    echo ""
    echo "=== Build Summary ==="
    echo "Timestamp: $TIMESTAMP"
    echo "Target Environment: $TARGET_ENV"
    echo "EAR File: $(basename "$EAR_FILE")"
    echo "Size: $(du -h "$EAR_FILE" | cut -f1)"
    echo "===================="
}

# Execute main function
main "$@"