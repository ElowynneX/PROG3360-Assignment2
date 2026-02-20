package org.example.featureflag;

import io.getunleash.Unleash;
import org.example.productservice.Service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceTest {

    @Mock
    private Unleash unleash;

    @Test
    void isPremiumPricingEnabled_ReturnsTrue_WhenFlagEnabled() {
        // Arrange
        when(unleash.isEnabled("premium-pricing")).thenReturn(true);
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isPremiumPricingEnabled();

        // Assert
        assertThat(result).isTrue();
        verify(unleash, times(1)).isEnabled("premium-pricing");
    }

    @Test
    void isPremiumPricingEnabled_ReturnsFalse_WhenFlagDisabled() {
        // Arrange
        when(unleash.isEnabled("premium-pricing")).thenReturn(false);
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isPremiumPricingEnabled();

        // Assert
        assertThat(result).isFalse();
        verify(unleash, times(1)).isEnabled("premium-pricing");
    }

    @Test
    void isPremiumPricingEnabled_ReturnsFalse_WhenUnleashThrowsException() {
        // Arrange
        when(unleash.isEnabled("premium-pricing")).thenThrow(new RuntimeException("Unleash down"));
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isPremiumPricingEnabled();

        // Assert
        assertThat(result).isFalse();
        verify(unleash, times(1)).isEnabled("premium-pricing");
    }
}