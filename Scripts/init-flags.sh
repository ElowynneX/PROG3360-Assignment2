#!/bin/sh
set -e

UNLEASH_URL="${UNLEASH_URL:-http://unleash-server:4242/api}"
ADMIN_TOKEN="${UNLEASH_ADMIN_TOKEN}"
PROJECT="default"
ENVIRONMENT="development"

FEATURES="premium-pricing order-notifications bulk-order-discount"

for FEATURE_NAME in $FEATURES; do
    echo "Checking if feature '$FEATURE_NAME' exists..."
    EXISTS=$(curl -s -H "Authorization: $ADMIN_TOKEN" \
        "$UNLEASH_URL/admin/projects/$PROJECT/features" | jq -r ".features[] | select(.name==\"$FEATURE_NAME\") | .name")

    if [ "$EXISTS" = "$FEATURE_NAME" ]; then
        echo "Feature '$FEATURE_NAME' already exists"
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
        echo "Strategy for '$FEATURE_NAME' in '$ENVIRONMENT' already exists"
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

echo "Init-flags script completed!"