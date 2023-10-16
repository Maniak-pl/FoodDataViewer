package pl.maniak.fooddataviewer.model

import com.squareup.moshi.JsonClass
import pl.maniak.fooddataviewer.model.dto.ProductDto

@JsonClass(generateAdapter = true)
data class Response(
    val product: ProductDto
)