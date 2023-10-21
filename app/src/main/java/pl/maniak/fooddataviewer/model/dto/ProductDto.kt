package pl.maniak.fooddataviewer.model.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class ProductDto(
    @PrimaryKey val id: String,
    val product_name: String,
    val brands: String,
    val image_url: String,
    val ingridients_text_debug: String?,
    @Embedded val nutriments: NutrimentsDto?,
)