#!/bin/bash

# Legacy EGP Application Deployment Script
# Supports JBoss AS 7.x and WebLogic 12c

set -e

# Configuration
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
EAR_FILE="$PROJECT_ROOT/egp-ear/target/egp-ear-1.0.0-SNAPSHOT.ear"

# Environment configurations (legacy hardcoded approach)
declare -A JBOSS_CONFIGS
JBOSS_CONFIGS[dev]="localhost:9999"
JBOSS_CONFIGS[test]="egp-test.internal.corp:9999"
JBOSS_CONFIGS[prod]="egp-prod.internal.corp:9999"

declare -A WEBLOGIC_CONFIGS
WEBLOGIC_CONFIGS[dev]="t3://localhost:7001"
WEBLOGIC_CONFIGS[test]="t3://egp-test.internal.corp:7001"
WEBLOGIC_CONFIGS[prod]="t3://egp-prod.internal.corp:7001"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Logging functions
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

info() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')] INFO: $1${NC}"
}

# Check if EAR file exists
check_ear_file() {
    if [ ! -f "$EAR_FILE" ]; then
        error "EAR file not found: $EAR_FILE. Please run build first."
    fi
    log "EAR file found: $EAR_FILE"
}

# Deploy to JBoss AS 7.x
deploy_to_jboss() {
    local env=$1
    local server=${JBOSS_CONFIGS[$env]}
    
    if [ -z "$server" ]; then
        error "Unknown environment: $env"
    fi
    
    log "Deploying to JBoss AS 7.x ($env environment: $server)..."
    
    # Check if JBOSS_HOME is set
    if [ -z "$JBOSS_HOME" ]; then
        # Try common locations
        for jboss_path in "/opt/jboss-as-7.1.1" "/usr/local/jboss" "/opt/jboss"; do
            if [ -d "$jboss_path" ]; then
                export JBOSS_HOME="$jboss_path"
                break
            fi
        done
        
        if [ -z "$JBOSS_HOME" ]; then
            error "JBOSS_HOME not set and JBoss installation not found"
        fi
    fi
    
    log "Using JBOSS_HOME: $JBOSS_HOME"
    
    # Check if JBoss CLI is available
    JBOSS_CLI="$JBOSS_HOME/bin/jboss-cli.sh"
    if [ ! -f "$JBOSS_CLI" ]; then
        error "JBoss CLI not found: $JBOSS_CLI"
    fi
    
    # Test connection to JBoss
    log "Testing connection to JBoss server..."
    if ! $JBOSS_CLI --connect --controller=$server --command=":read-attribute(name=server-state)" &>/dev/null; then
        error "Cannot connect to JBoss server: $server"
    fi
    
    # Undeploy existing application if it exists
    log "Checking for existing deployment..."
    if $JBOSS_CLI --connect --controller=$server --command="deployment-info --name=egp-ear-1.0.0-SNAPSHOT.ear" &>/dev/null; then
        log "Undeploying existing application..."
        $JBOSS_CLI --connect --controller=$server --command="undeploy egp-ear-1.0.0-SNAPSHOT.ear"
    fi
    
    # Deploy new application
    log "Deploying new application..."
    $JBOSS_CLI --connect --controller=$server --command="deploy $EAR_FILE"
    
    # Verify deployment
    log "Verifying deployment..."
    if $JBOSS_CLI --connect --controller=$server --command="deployment-info --name=egp-ear-1.0.0-SNAPSHOT.ear" | grep -q "OK"; then
        log "Deployment successful!"
    else
        error "Deployment verification failed"
    fi
}

# Deploy to WebLogic 12c
deploy_to_weblogic() {
    local env=$1
    local server=${WEBLOGIC_CONFIGS[$env]}
    
    if [ -z "$server" ]; then
        error "Unknown environment: $env"
    fi
    
    log "Deploying to WebLogic 12c ($env environment: $server)..."
    
    # Check if WEBLOGIC_HOME is set
    if [ -z "$WEBLOGIC_HOME" ]; then
        # Try common locations
        for wl_path in "/opt/oracle/middleware/wlserver_12.1" "/opt/weblogic" "/usr/local/weblogic"; do
            if [ -d "$wl_path" ]; then
                export WEBLOGIC_HOME="$wl_path"
                break
            fi
        done
        
        if [ -z "$WEBLOGIC_HOME" ]; then
            error "WEBLOGIC_HOME not set and WebLogic installation not found"
        fi
    fi
    
    log "Using WEBLOGIC_HOME: $WEBLOGIC_HOME"
    
    # WebLogic deployment using WLST (WebLogic Scripting Tool)
    WLST_SCRIPT="/tmp/deploy_egp_${env}.py"
    
    # Create WLST deployment script
    cat > "$WLST_SCRIPT" << EOF
# WebLogic deployment script for EGP application
import sys

# Connection parameters
adminURL = '$server'
username = 'weblogic'  # Legacy: hardcoded credentials (security issue!)
password = 'welcome1'  # Legacy: default password

try:
    print('Connecting to WebLogic Admin Server...')
    connect(username, password, adminURL)
    
    # Check if application is already deployed
    appName = 'egp-ear'
    if isAppDeployed(appName):
        print('Undeploying existing application...')
        undeploy(appName)
    
    # Deploy new application
    print('Deploying new application...')
    deploy(appName, '$EAR_FILE', targets='AdminServer')
    
    # Start application
    print('Starting application...')
    startApplication(appName)
    
    print('Deployment completed successfully!')
    
except Exception, e:
    print('Deployment failed: ' + str(e))
    sys.exit(1)
    
finally:
    disconnect()
EOF
    
    # Execute WLST script
    "$WEBLOGIC_HOME/common/bin/wlst.sh" "$WLST_SCRIPT"
    
    # Clean up
    rm -f "$WLST_SCRIPT"
}

# Deploy to Tomcat (for WAR-only deployment)
deploy_to_tomcat() {
    local env=$1
    
    log "Deploying WAR to Tomcat ($env environment)..."
    
    WAR_FILE="$PROJECT_ROOT/egp-portal-war/target/egp-portal-war-1.0.0-SNAPSHOT.war"
    
    if [ ! -f "$WAR_FILE" ]; then
        error "WAR file not found: $WAR_FILE"
    fi
    
    # Tomcat deployment (legacy approach - direct file copy)
    case $env in
        "dev")
            TOMCAT_WEBAPPS="/opt/tomcat/webapps"
            ;;
        "test")
            TOMCAT_WEBAPPS="/opt/tomcat-test/webapps"
            ;;
        "prod")
            TOMCAT_WEBAPPS="/opt/tomcat-prod/webapps"
            ;;
        *)
            error "Unknown environment: $env"
            ;;
    esac
    
    if [ ! -d "$TOMCAT_WEBAPPS" ]; then
        error "Tomcat webapps directory not found: $TOMCAT_WEBAPPS"
    fi
    
    # Stop Tomcat (legacy approach)
    log "Stopping Tomcat..."
    sudo systemctl stop tomcat || warn "Could not stop Tomcat service"
    
    # Remove old deployment
    if [ -d "$TOMCAT_WEBAPPS/egp-portal" ]; then
        log "Removing old deployment..."
        sudo rm -rf "$TOMCAT_WEBAPPS/egp-portal"
    fi
    
    if [ -f "$TOMCAT_WEBAPPS/egp-portal.war" ]; then
        sudo rm -f "$TOMCAT_WEBAPPS/egp-portal.war"
    fi
    
    # Copy new WAR file
    log "Copying new WAR file..."
    sudo cp "$WAR_FILE" "$TOMCAT_WEBAPPS/egp-portal.war"
    
    # Start Tomcat
    log "Starting Tomcat..."
    sudo systemctl start tomcat
    
    # Wait for deployment
    log "Waiting for application to deploy..."
    sleep 30
    
    # Verify deployment
    if [ -d "$TOMCAT_WEBAPPS/egp-portal" ]; then
        log "Tomcat deployment successful!"
    else
        error "Tomcat deployment failed"
    fi
}

# Verify deployment health
verify_deployment() {
    local env=$1
    local app_server=$2
    
    log "Verifying deployment health..."
    
    # Determine health check URL based on environment and server
    case $app_server in
        "jboss")
            case $env in
                "dev") HEALTH_URL="http://localhost:8080/egp-portal/health" ;;
                "test") HEALTH_URL="http://egp-test.internal.corp:8080/egp-portal/health" ;;
                "prod") HEALTH_URL="http://egp-prod.internal.corp:8080/egp-portal/health" ;;
            esac
            ;;
        "weblogic")
            case $env in
                "dev") HEALTH_URL="http://localhost:7001/egp-portal/health" ;;
                "test") HEALTH_URL="http://egp-test.internal.corp:7001/egp-portal/health" ;;
                "prod") HEALTH_URL="http://egp-prod.internal.corp:7001/egp-portal/health" ;;
            esac
            ;;
        "tomcat")
            case $env in
                "dev") HEALTH_URL="http://localhost:8080/egp-portal/health" ;;
                "test") HEALTH_URL="http://egp-test.internal.corp:8080/egp-portal/health" ;;
                "prod") HEALTH_URL="http://egp-prod.internal.corp:8080/egp-portal/health" ;;
            esac
            ;;
    esac
    
    if [ -n "$HEALTH_URL" ]; then
        log "Checking health endpoint: $HEALTH_URL"
        
        # Wait for application to start
        sleep 10
        
        # Try health check (with retries)
        for i in {1..5}; do
            if curl -f -s "$HEALTH_URL" > /dev/null 2>&1; then
                log "Health check passed!"
                return 0
            else
                warn "Health check attempt $i failed, retrying in 10 seconds..."
                sleep 10
            fi
        done
        
        error "Health check failed after 5 attempts"
    else
        warn "No health check URL configured for $app_server/$env"
    fi
}

# Main deployment function
main() {
    log "Starting Legacy EGP Application deployment..."
    
    # Parse command line arguments
    if [ $# -lt 1 ]; then
        echo "Usage: $0 <environment> [app-server]"
        echo ""
        echo "Environments: dev, test, prod"
        echo "App Servers: jboss (default), weblogic, tomcat"
        echo ""
        echo "Examples:"
        echo "  $0 dev"
        echo "  $0 test jboss"
        echo "  $0 prod weblogic"
        exit 1
    fi
    
    local env=$1
    local app_server=${2:-jboss}
    
    log "Target environment: $env"
    log "Application server: $app_server"
    
    # Validate environment
    case $env in
        dev|test|prod) ;;
        *) error "Invalid environment: $env. Use dev, test, or prod." ;;
    esac
    
    # Validate app server
    case $app_server in
        jboss|weblogic|tomcat) ;;
        *) error "Invalid app server: $app_server. Use jboss, weblogic, or tomcat." ;;
    esac
    
    # Production deployment confirmation
    if [ "$env" = "prod" ]; then
        echo ""
        warn "You are about to deploy to PRODUCTION environment!"
        read -p "Are you sure you want to continue? (yes/no): " confirm
        if [ "$confirm" != "yes" ]; then
            log "Deployment cancelled by user"
            exit 0
        fi
    fi
    
    # Check prerequisites
    check_ear_file
    
    # Deploy based on application server
    case $app_server in
        "jboss")
            deploy_to_jboss "$env"
            ;;
        "weblogic")
            deploy_to_weblogic "$env"
            ;;
        "tomcat")
            deploy_to_tomcat "$env"
            ;;
    esac
    
    # Verify deployment
    verify_deployment "$env" "$app_server"
    
    log "Deployment completed successfully!"
    
    # Display deployment summary
    echo ""
    echo "=== Deployment Summary ==="
    echo "Environment: $env"
    echo "Application Server: $app_server"
    echo "EAR File: $(basename "$EAR_FILE")"
    echo "Deployment Time: $(date)"
    echo "=========================="
}

# Execute main function
main "$@"