package pl.maniak.fooddataviewer.fooddetails

sealed class FoodDetailsEffect

data class LoadProduct(val barcode: String) : FoodDetailsEffect()

data class DeleteProduct(val barcode: String) : FoodDetailsEffect()

data class SaveProduct(val barcode: String) : FoodDetailsEffect()