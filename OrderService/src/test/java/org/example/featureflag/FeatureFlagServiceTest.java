package org.example.featureflag;

import io.getunleash.Unleash;
import org.example.orderservice.Service.FeatureFlagService;
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
    void isBulkOrderDiscountEnabled_returnsTrue_whenFlagEnabled() {
        // Arrange
        when(unleash.isEnabled("bulk-order-discount")).thenReturn(true);
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isBulkOrderDiscountEnabled();

        // Assert
        assertThat(result).isTrue();
        verify(unleash, times(1)).isEnabled("bulk-order-discount");
    }

    @Test
    void isBulkOrderDiscountEnabled_returnsFalse_whenFlagDisabled() {
        // Arrange
        when(unleash.isEnabled("bulk-order-discount")).thenReturn(false);
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isBulkOrderDiscountEnabled();

        // Assert
        assertThat(result).isFalse();
        verify(unleash, times(1)).isEnabled("bulk-order-discount");
    }

    @Test
    void isBulkOrderDiscountEnabled_returnsFalse_whenUnleashThrowsException() {
        // Arrange
        when(unleash.isEnabled("bulk-order-discount")).thenThrow(new RuntimeException("Unleash down"));
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isBulkOrderDiscountEnabled();

        // Assert
        assertThat(result).isFalse();
        verify(unleash, times(1)).isEnabled("bulk-order-discount");
    }

    @Test
    void isOrderNotificationsEnabled_returnsTrue_whenFlagEnabled() {
        // Arrange
        when(unleash.isEnabled("order-notifications")).thenReturn(true);
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isOrderNotificationsEnabled();

        // Assert
        assertThat(result).isTrue();
        verify(unleash, times(1)).isEnabled("order-notifications");
    }

    @Test
    void isOrderNotificationsEnabled_returnsFalse_whenFlagDisabled() {
        // Arrange
        when(unleash.isEnabled("order-notifications")).thenReturn(false);
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isOrderNotificationsEnabled();

        // Assert
        assertThat(result).isFalse();
        verify(unleash, times(1)).isEnabled("order-notifications");
    }

    @Test
    void isOrderNotificationsEnabled_returnsFalse_whenUnleashThrowsException() {
        // Arrange
        when(unleash.isEnabled("order-notifications")).thenThrow(new RuntimeException("Unleash down"));
        FeatureFlagService service = new FeatureFlagService(unleash);

        // Act
        boolean result = service.isOrderNotificationsEnabled();

        // Assert
        assertThat(result).isFalse();
        verify(unleash, times(1)).isEnabled("order-notifications");
    }
}