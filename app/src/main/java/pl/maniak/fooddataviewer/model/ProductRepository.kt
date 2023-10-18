package pl.maniak.fooddataviewer.model

import io.reactivex.Single
import pl.maniak.fooddataviewer.model.dto.NutrimentsDto
import pl.maniak.fooddataviewer.model.dto.ProductDto
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productService: ProductService) {
    fun getProductFromApi(barcode: String): Single<Product> {
        return productService.getProduct(barcode)
            .map { response -> mapProduct(dto = response.product, saved = false) }
    }
}

fun mapProduct(dto: ProductDto, saved: Boolean): Product {
    return Product(
        id = dto.id,
        name = dto.product_name,
        brands = dto.brands,
        imageUrl = dto.image_url,
        ingredients = dto.ingridients_text_debug ?: "",
        saved = saved,
        nutriments = mapNutriments(dto.nutriments)
    )
}

fun mapNutriments(dto: NutrimentsDto?): Nutriments? {
    if (dto == null) return null
    return Nutriments(
        energy = dto.energy_100g,
        salt = dto.salt_100g,
        carbohydrates = dto.carbohydrates_100g,
        fiber = dto.fiber_100g,
        sugars = dto.sugars_100g,
        proteins = dto.proteins_100g,
        fat = dto.fat_100g
    )
}