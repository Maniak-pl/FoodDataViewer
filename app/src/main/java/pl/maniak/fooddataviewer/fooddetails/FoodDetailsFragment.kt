package pl.maniak.fooddataviewer.fooddetails

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.getViewModel

class FoodDetailsFragment : Fragment(R.layout.food_details_fragment) {

    private val args: FoodDetailsFragmentArgs by navArgs()
    private lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionButton = view.findViewById<Button>(R.id.actionButton)

        disposable = actionButton.clicks().map { ActionButtonClicked }
            .compose(getViewModel(FoodDetailsViewModel::class).init(Initial(args.barcode)))
            .subscribe()
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}