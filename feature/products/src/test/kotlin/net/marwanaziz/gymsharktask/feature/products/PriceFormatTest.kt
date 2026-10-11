package net.marwanaziz.gymsharktask.feature.products

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class PriceFormatTest {
    @Test
    fun ukLocaleUsesPoundsWithADot() {
        assertEquals("£10.50", formatPrice(1_050, Locale.UK))
        assertEquals("£1,000.00", formatPrice(100_000, Locale.UK))
    }

    @Test
    fun germanLocaleUsesEurosWithAComma() {
        assertEquals("10,50\u00A0€", formatPrice(1_050, Locale.GERMANY))
        assertEquals("1.000,00\u00A0€", formatPrice(100_000, Locale.GERMANY))
    }

    @Test
    fun usLocaleUsesDollars() {
        assertEquals("$10.50", formatPrice(1_050, Locale.US))
    }
}
