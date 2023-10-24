package pl.maniak.fooddataviewer.foodlist

sealed class FoodListEffect

object ObserveProducts : FoodListEffect()

object NavigateToScanner : FoodListEffect()

data class NavigateToFoodDetails(val barcode: String) : FoodListEffect()