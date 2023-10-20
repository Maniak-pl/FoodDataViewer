package pl.maniak.fooddataviewer.fooddetails

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pl.maniak.fooddataviewer.getViewModel

class FoodDetailsFragment : Fragment() {

    private val args: FoodDetailsFragmentArgs by navArgs()
    private lateinit var disposable: Disposable

    override fun onStart() {
        super.onStart()

        disposable = Observable.empty<FoodDetailsEvent>()
            .compose(getViewModel(FoodDetailsViewModel::class))
            .subscribe()
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}