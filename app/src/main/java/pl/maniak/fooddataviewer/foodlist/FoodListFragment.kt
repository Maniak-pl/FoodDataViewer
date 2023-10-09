package pl.maniak.fooddataviewer.foodlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding3.view.clicks
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.getViewModel

class FoodListFragment : Fragment(R.layout.food_list_fragment) {

    override fun onStart() {
        super.onStart()
        getViewModel(FoodListViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<FloatingActionButton>(R.id.addButton)
        addButton.clicks().map { AddButtonClicked }
            .compose(getViewModel(FoodListViewModel::class))
            .subscribe()
    }
}
