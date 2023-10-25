package pl.maniak.fooddataviewer.foodlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.foodlist.widget.FoodListAdapter
import pl.maniak.fooddataviewer.getViewModel

class FoodListFragment : Fragment(R.layout.food_list_fragment) {

    lateinit var disposable: Disposable
    override fun onStart() {
        super.onStart()
        getViewModel(FoodListViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val addButton = view.findViewById<FloatingActionButton>(R.id.addButton)

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

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}
