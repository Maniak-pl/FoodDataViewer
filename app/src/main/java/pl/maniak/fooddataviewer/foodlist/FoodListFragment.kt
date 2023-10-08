package pl.maniak.fooddataviewer.foodlist

import androidx.fragment.app.Fragment
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.getViewModel

class FoodListFragment : Fragment(R.layout.food_list_fragment) {

    override fun onStart() {
        super.onStart()
        getViewModel(FoodListViewModel::class)
    }
}
