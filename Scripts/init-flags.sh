#!/bin/sh
set -e

# -------------------------
# Configuration (env vars)
# -------------------------
UNLEASH_URL="${UNLEASH_URL:-http://unleash-server:4242/api}"
ADMIN_USER="${UNLEASH_ADMIN_USER:-admin}"
ADMIN_PASSWORD="${UNLEASH_ADMIN_PASSWORD:-unleash4all}"
ADMIN_TOKEN="${UNLEASH_ADMIN_TOKEN:-}"
PROJECT="${UNLEASH_PROJECT:-default}"
ENVIRONMENT="${UNLEASH_ENVIRONMENT:-development}"
FEATURES="${FEATURES:-premium-pricing order-notifications bulk-order-discount}"

# -------------------------
# Wait for Unleash API
# -------------------------
sleep 5
echo "Waiting for Unleash API to be ready..."
until curl -s "http://unleash-server:4242/health" | grep -q '"health":"GOOD"'; do
    echo "Unleash not ready yet..."
    sleep 2
done
echo "Unleash is ready!"
echo "Admin token ready."

# -------------------------
# Loop over features
# -------------------------
for FEATURE_NAME in $FEATURES; do
    echo "Processing feature '$FEATURE_NAME'..."

    # Check if feature exists
    EXISTS=$(curl -s -H "Authorization: $ADMIN_TOKEN" \
        "$UNLEASH_URL/admin/projects/$PROJECT/features" | jq -r ".features[] | select(.name==\"$FEATURE_NAME\") | .name")

    if [ "$EXISTS" = "$FEATURE_NAME" ]; then
        echo "Feature '$FEATURE_NAME' already exists."
    else
        echo "Creating feature '$FEATURE_NAME'..."
        curl -s -X POST "$UNLEASH_URL/admin/features" \
            -H "Authorization: $ADMIN_TOKEN" \
            -H "Content-Type: application/json" \
            -d "{
                \"name\": \"$FEATURE_NAME\",
                \"project\": \"$PROJECT\",
                \"description\": \"Auto-created feature flag\",
                \"type\": \"release\",
                \"stale\": false,
                \"enabled\": false
            }"
    fi

    # Check if strategy exists
    STRATEGY_EXISTS=$(curl -s -H "Authorization: $ADMIN_TOKEN" \
        "$UNLEASH_URL/admin/projects/$PROJECT/features/$FEATURE_NAME/environments/$ENVIRONMENT/strategies" | jq -r ".[] | select(.name==\"default\") | .name")

    if [ "$STRATEGY_EXISTS" = "default" ]; then
        echo "Strategy for '$FEATURE_NAME' in '$ENVIRONMENT' already exists."
    else
        echo "Adding 100% rollout strategy for '$FEATURE_NAME' in '$ENVIRONMENT'..."
        curl -s -X POST "$UNLEASH_URL/admin/projects/$PROJECT/features/$FEATURE_NAME/environments/$ENVIRONMENT/strategies" \
            -H "Authorization: $ADMIN_TOKEN" \
            -H "Content-Type: application/json" \
            -d "{
                \"name\": \"default\",
                \"parameters\": {\"percentage\":\"100\"}
            }"
    fi
done

echo "All features and strategies are set up. ✅"