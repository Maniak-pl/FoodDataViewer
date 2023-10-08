package pl.maniak.fooddataviewer.foodlist

import androidx.fragment.app.Fragment
import pl.maniak.fooddataviewer.getViewModel

class FoodListFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        getViewModel(FoodListViewModel::class)
    }
}