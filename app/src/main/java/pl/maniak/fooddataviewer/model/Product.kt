package pl.maniak.fooddataviewer.model

data class Product(
    val id: String,
    val saved: Boolean,
    val name: String,
    val brands: String,
    val imageUrl: String,
    val ingredients: String,
    val nutriments: Nutriments?,
)