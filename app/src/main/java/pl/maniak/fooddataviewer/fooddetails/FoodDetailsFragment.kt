package pl.maniak.fooddataviewer.fooddetails

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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
        val loadingIndicator = view.findViewById<View>(R.id.loadingIndicator)
        val contentView = view.findViewById<View>(R.id.contentView)
        val productNameView = view.findViewById<TextView>(R.id.productNameView)
        val brandNameView = view.findViewById<TextView>(R.id.brandNameView)
        val energyValue = view.findViewById<TextView>(R.id.energyValue)
        val carbsValue = view.findViewById<TextView>(R.id.carbsValue)
        val fatValue = view.findViewById<TextView>(R.id.fatValue)
        val proteinValue = view.findViewById<TextView>(R.id.proteinValue)
        val ingredientsText = view.findViewById<TextView>(R.id.ingredientsText)
        val productImageView = view.findViewById<ImageView>(R.id.productImageView)
        val barcodeView = view.findViewById<TextView>(R.id.barcodeView)

        disposable = actionButton.clicks().map { ActionButtonClicked }
            .compose(getViewModel(FoodDetailsViewModel::class).init(Initial(args.barcode)))

            .subscribe { model ->
                loadingIndicator.isVisible = model.activity
                contentView.isVisible = model.product != null
                model.product?.let { product ->
                    productNameView.text = product.name
                    brandNameView.text = product.brands
                    barcodeView.text = product.id
                    energyValue.text = getString(R.string.food_details_energy_value, product.nutriments?.energy)
                    carbsValue.text = getString(R.string.food_details_macro_value, product.nutriments?.carbohydrates)
                    fatValue.text = getString(R.string.food_details_macro_value, product.nutriments?.fat)
                    proteinValue.text = getString(R.string.food_details_macro_value, product.nutriments?.proteins)
                    ingredientsText.text = getString(R.string.food_details_ingridients, product.ingredients)
                    actionButton.text = if (product.saved) {
                        getString(R.string.food_details_delete)
                    } else {
                        getString(R.string.food_details_save)
                    }
                    Glide.with(requireContext())
                        .load(product.imageUrl)
                        .fitCenter()
                        .into(productImageView)
                }
            }
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}