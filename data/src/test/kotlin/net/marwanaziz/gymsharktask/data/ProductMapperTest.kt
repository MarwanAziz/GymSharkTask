package net.marwanaziz.gymsharktask.data

import net.marwanaziz.gymsharktask.data.remote.CatalogueMedia
import net.marwanaziz.gymsharktask.domain.ProductVariant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductMapperTest {
    private val mapper = ProductMapper()

    @Test
    fun mapsANormalHitToProduct() {
        val hit = catalogueHit()

        val product = mapper.map(hit)

        assertEquals(hit.id, product.id)
        assertEquals(hit.handle, product.handle)
        assertEquals(hit.title, product.title)
        assertEquals(hit.description, product.description)
        assertEquals(hit.type, product.type)
        assertEquals(hit.fit, product.fit)
        assertEquals(hit.colour, product.colour)
        assertEquals(hit.price, product.price)
        assertEquals(hit.compareAtPrice, product.compareAtPrice)
        assertEquals(hit.discountPercentage, product.discountPercentage)
        assertEquals(listOf("f"), product.gender)
        assertEquals(FEATURED_SRC, product.featuredImageUrl)
        assertEquals(listOf(MEDIA_SRC), product.imageUrls)
        assertEquals(
            listOf(
                ProductVariant(
                    id = 39814344835171L,
                    size = "xs",
                    sku = "B3A3E-UBCY-XS",
                    isAvailable = true,
                    price = 1000,
                ),
            ),
            product.variants,
        )
    }

    @Test
    fun keepsGoingFastLabel() {
        val product = mapper.map(catalogueHit(labels = listOf("going-fast")))

        assertEquals(listOf("going-fast"), product.labels)
    }

    @Test
    fun mapsNullAndEmptyLabelsToAnEmptyList() {
        assertEquals(emptyList<String>(), mapper.map(catalogueHit(labels = null)).labels)
        assertEquals(emptyList<String>(), mapper.map(catalogueHit(labels = emptyList())).labels)
    }

    @Test
    fun mapsMissingAndBlankFeaturedImageToNull() {
        assertNull(mapper.map(catalogueHit(featuredMedia = null)).featuredImageUrl)
        assertNull(mapper.map(catalogueHit(featuredMedia = CatalogueMedia(src = null))).featuredImageUrl)
        assertNull(mapper.map(catalogueHit(featuredMedia = CatalogueMedia(src = "   "))).featuredImageUrl)
    }

    @Test
    fun keepsOnlyRealMediaUrls() {
        val product = mapper.map(
            catalogueHit(
                media = listOf(
                    CatalogueMedia(src = MEDIA_SRC),
                    CatalogueMedia(src = null),
                    CatalogueMedia(src = "   "),
                ),
            ),
        )

        assertEquals(listOf(MEDIA_SRC), product.imageUrls)
    }
}
