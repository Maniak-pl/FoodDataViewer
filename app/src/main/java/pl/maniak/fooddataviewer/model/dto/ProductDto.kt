package pl.maniak.fooddataviewer.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDto(
    val product_name: String,
    val brands: String,
    val image_url: String,
    val ingridients_text_debug: String?,
)