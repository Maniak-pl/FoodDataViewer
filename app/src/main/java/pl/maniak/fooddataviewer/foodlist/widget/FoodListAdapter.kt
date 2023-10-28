package pl.maniak.fooddataviewer.foodlist.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.databinding.FoodListProductItemBinding
import pl.maniak.fooddataviewer.model.Product

class FoodListAdapter : ListAdapter<Product, FoodListProductViewHolder>(DiffUtilCallback()) {

    private val productClicksSubject = PublishSubject.create<Int>()
    val productClicks: Observable<Product> = productClicksSubject
        .map { position -> getItem(position) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListProductViewHolder {
        val binding = FoodListProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodListProductViewHolder(binding, productClicksSubject)
    }

    override fun onBindViewHolder(holder: FoodListProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.food_list_product_item
    }
}

private class DiffUtilCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}

class FoodListProductViewHolder(
    private val binding: FoodListProductItemBinding,
    private val productClicksSubject: PublishSubject<Int>
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        with(binding.root) {
            val context = context
            val productNameView = findViewById<TextView>(R.id.productNameView)
            val brandNameView = findViewById<TextView>(R.id.brandNameView)
            val energyValueView = findViewById<TextView>(R.id.energyValueView)
            val carbsValueView = findViewById<TextView>(R.id.carbsValueView)
            val fatValueView = findViewById<TextView>(R.id.fatValueView)
            val proteinValueView = findViewById<TextView>(R.id.proteinValueView)
            val productImageView = findViewById<ImageView>(R.id.productImageView)
            val productCard = findViewById<CardView>(R.id.productCard)

            productNameView.text = product.name
            brandNameView.text = product.brands
            energyValueView.text = context.getString(R.string.scan_energy_value, product.nutriments?.energy)
            carbsValueView.text = context.getString(R.string.scan_macro_value, product.nutriments?.carbohydrates)
            fatValueView.text = context.getString(R.string.scan_macro_value, product.nutriments?.fat)
            proteinValueView.text = context.getString(R.string.scan_macro_value, product.nutriments?.proteins)

            Glide.with(context)
                .load(product.imageUrl)
                .fitCenter()
                .into(productImageView)

            productCard.clicks()
                .map { adapterPosition }
                .subscribe(productClicksSubject)
        }
    }
}