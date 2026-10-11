package net.marwanaziz.gymsharktask.feature.products.detail

import org.junit.Assert.assertEquals
import org.junit.Test

class SizeAvailabilityTest {
    @Test
    fun nullAvailabilityAsksTheCustomerToMakeContact() {
        assertEquals("Contact us", sizeAvailabilityLabel(null))
    }

    @Test
    fun falseAvailabilityIsOutOfStock() {
        assertEquals("Out of stock", sizeAvailabilityLabel(false))
    }

    @Test
    fun trueAvailabilityIsInStock() {
        assertEquals("In stock", sizeAvailabilityLabel(true))
    }
}
