package pl.maniak.fooddataviewer.fooddetails

sealed class FoodDetailsEffect

data class LoadProduct(val barcode: String) : FoodDetailsEffect()