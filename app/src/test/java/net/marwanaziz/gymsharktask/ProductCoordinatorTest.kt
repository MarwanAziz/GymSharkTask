package net.marwanaziz.gymsharktask

import net.marwanaziz.gymsharktask.domain.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductCoordinatorTest {
    @Test
    fun selectedProductOpensDetailAndBackReturnsToTheList() {
        val coordinator = ProductCoordinator()
        val product = product(id = 42L)

        assertEquals(ProductDestination.List, coordinator.destination.value)

        coordinator.onProductSelected(product)
        assertEquals(ProductDestination.Detail(product), coordinator.destination.value)

        coordinator.onBack()
        assertEquals(ProductDestination.List, coordinator.destination.value)
    }

    private fun product(id: Long) = Product(
        id = id,
        handle = null,
        title = "Speed Leggings",
        description = null,
        type = null,
        gender = null,
        fit = null,
        colour = "Navy",
        price = 1000,
        compareAtPrice = null,
        discountPercentage = null,
        featuredImageUrl = null,
        imageUrls = null,
        labels = null,
        variants = null,
    )
}
