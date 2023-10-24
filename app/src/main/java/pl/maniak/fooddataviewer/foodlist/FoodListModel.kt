package pl.maniak.fooddataviewer.foodlist

import pl.maniak.fooddataviewer.model.Product

data class FoodListModel(
    val products: List<Product> = listOf()
)