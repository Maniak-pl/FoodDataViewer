package pl.maniak.fooddataviewer.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController

class Navigator(private val viewId: Int, private val service: ActivityService) {

    private val navController: NavController
        get() = service.activity.findNavController(viewId)

    fun to(directions: NavDirections) {
        navController.navigate(directions)
    }
}