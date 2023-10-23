package pl.maniak.fooddataviewer.fooddetails

import pl.maniak.fooddataviewer.model.Product

sealed class FoodDetailsEvent

data class Initial(val barcode: String) : FoodDetailsEvent()

object ActionButtonClicked : FoodDetailsEvent()

data class ProductLoaded(val product: Product) : FoodDetailsEvent()

object ErrorLoadingProduct : FoodDetailsEvent()