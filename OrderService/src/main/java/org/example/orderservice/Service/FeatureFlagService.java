package org.example.orderservice.Service;

import io.getunleash.Unleash;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {
    private final Unleash unleash;

    public FeatureFlagService(Unleash unleash) {
        this.unleash = unleash;
    }

    public boolean isOrderNotificationsEnabled() {
        try {
            boolean enabled = unleash.isEnabled("order-notifications");
            System.out.println("[DEBUG] order-notifications enabled? " + enabled);
            return enabled;
        } catch (Exception e) {
            System.out.println("Unleash unavailable, defaulting order-notifications to false");
            return false;
        }
    }

    public boolean isBulkOrderDiscountEnabled() {
        try {
            boolean enabled = unleash.isEnabled("bulk-order-discount");
            System.out.println("[DEBUG] bulk-order-discount enabled? " + enabled);
            return enabled;
        } catch (Exception e) {
            System.out.println("Unleash unavailable, defaulting bulk-order-discount to false");
            return false;
        }
    }
}