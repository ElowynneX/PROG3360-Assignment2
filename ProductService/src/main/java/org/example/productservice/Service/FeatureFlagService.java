package org.example.productservice.Service;

import io.getunleash.Unleash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FeatureFlagService {

    private static final Logger log = LoggerFactory.getLogger(FeatureFlagService.class);
    private final Unleash unleash;

    public FeatureFlagService(Unleash unleash) {
        this.unleash = unleash;
    }

    public boolean isPremiumPricingEnabled() {
        try {
            boolean enabled = unleash.isEnabled("premium-pricing");
            System.out.println("[DEBUG] premium-pricing enabled? " + enabled);
            return enabled;
        } catch (Exception e) {
            System.out.println("Unleash unavailable, defaulting premium-pricing to false");
            return false;
        }
    }
}