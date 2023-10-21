package pl.maniak.fooddataviewer.fooddetails

sealed class FoodDetailsEvent

data class Initial(val barcode: String) : FoodDetailsEvent()

object ActionButtonClicked : FoodDetailsEvent()