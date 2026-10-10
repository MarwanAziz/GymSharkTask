package net.marwanaziz.gymsharktask.data.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogueDecodingTest {
    @Test
    fun decodesNormalHitAndGoingFastHit() {
        val catalogue = catalogueJson.decodeFromString(
            CatalogueResponse.serializer(),
            fixture("catalogue.json"),
        )
        val normal = catalogue.hits.single { it.id == 6732609257571L }
        val goingFast = catalogue.hits.single { it.id == 6693927616611L }

        assertNull(normal.labels)
        assertEquals(6732609257571L, normal.id)

        assertEquals(6693927616611L, goingFast.id)
        assertEquals("Vital Seamless 2.0 Leggings", goingFast.title)
        assertEquals("Tahoe Teal Marl", goingFast.colour)
        assertEquals(1000, goingFast.price)
        assertEquals(listOf("going-fast"), goingFast.labels)
        assertEquals(listOf("f"), goingFast.gender)
        assertEquals(
            "https://cdn.shopify.com/s/files/1/1326/4923/products/VitalSeamlessLeggingsTahoeTealMarlB1A2B-TBBS.A_ZH_1.jpg?v=1644310928",
            goingFast.featuredMedia?.src,
        )
        assertEquals(
            "https://cdn.shopify.com/s/files/1/1326/4923/products/VitalSeamlessLeggingsTahoeTealMarlB1A2B-TBBS.A_ZH_1.jpg?v=1644310928",
            goingFast.media?.first()?.src,
        )
        assertEquals(39723356258403L, goingFast.availableSizes?.first()?.id)
        assertEquals(true, goingFast.availableSizes?.first()?.inStock)
    }

    @Test
    fun decodesNullLabelsNullSizeInStockAndMissingFeaturedMedia() {
        val catalogue = catalogueJson.decodeFromString(
            CatalogueResponse.serializer(),
            fixture("hit-missing-featured-media.json"),
        )
        val hit = catalogue.hits.single()

        assertEquals(6732609257571L, hit.id)
        assertNull(hit.labels)
        assertNull(hit.sizeInStock)
        assertNull(hit.featuredMedia)
    }
}

private fun fixture(name: String): String {
    val stream = checkNotNull(CatalogueDecodingTest::class.java.classLoader.getResourceAsStream(name)) {
        "Missing test resource $name"
    }
    return stream.bufferedReader().use { it.readText() }
}
