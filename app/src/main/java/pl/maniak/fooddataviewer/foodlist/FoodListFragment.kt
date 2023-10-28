package pl.maniak.fooddataviewer.foodlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pl.maniak.fooddataviewer.databinding.FoodListFragmentBinding
import pl.maniak.fooddataviewer.foodlist.widget.FoodListAdapter
import pl.maniak.fooddataviewer.getViewModel

class FoodListFragment : Fragment() {

    private lateinit var binding: FoodListFragmentBinding
    private lateinit var disposable: Disposable

    override fun onStart() {
        super.onStart()
        getViewModel(FoodListViewModel::class)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FoodListFragmentBinding.inflate(inflater, container, false)
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = FoodListAdapter()
            recyclerView.adapter = adapter

            disposable = Observable
                .mergeArray(
                    addButton.clicks().map { AddButtonClicked },
                    adapter.productClicks.map { ProductClicked(it.id) }
                )
                .compose(getViewModel(FoodListViewModel::class).init(Initial))
                .subscribe { model ->
                    adapter.submitList(model.products)
                }
        }
        return binding.root
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}
