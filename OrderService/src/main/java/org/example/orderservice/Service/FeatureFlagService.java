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
            return unleash.isEnabled("order-notifications");
        } catch (Exception e) {
            System.out.println("Unleash unavailable, defaulting order-notifications to false");
            return false;
        }
    }

    public boolean isBulkOrderDiscountEnabled() {
        try {
            return unleash.isEnabled("bulk-order-discount");
        } catch (Exception e) {
            System.out.println("Unleash unavailable, defaulting bulk-order-discount to false");
            return false;
        }
    }
}