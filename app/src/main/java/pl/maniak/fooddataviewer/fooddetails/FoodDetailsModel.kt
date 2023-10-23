package pl.maniak.fooddataviewer.fooddetails

import pl.maniak.fooddataviewer.model.Product

data class FoodDetailsModel(val activity: Boolean = false, val product: Product? = null, val error: Boolean = false)