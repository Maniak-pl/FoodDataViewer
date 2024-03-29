package pl.maniak.fooddataviewer.scan.handlers

import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.maniak.fooddataviewer.model.ProductRepository
import pl.maniak.fooddataviewer.scan.BarcodeError
import pl.maniak.fooddataviewer.scan.ProcessBarcode
import pl.maniak.fooddataviewer.scan.ProductLoaded
import pl.maniak.fooddataviewer.scan.ScanEvent
import pl.maniak.fooddataviewer.utils.IdlingResource
import javax.inject.Inject

class ProcessBarcodeHandler @Inject constructor(
    private val productRepository: ProductRepository,
    private val idlingResource: IdlingResource
) : ObservableTransformer<ProcessBarcode, ScanEvent> {

    override fun apply(upstream: Observable<ProcessBarcode>): ObservableSource<ScanEvent> {
        return upstream.switchMap { effect ->
            productRepository.getProductFromApi(effect.barcode)
                .map { product ->
                    idlingResource.decrement()
                    ProductLoaded(product) as ScanEvent
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { error ->
                    idlingResource.decrement()
                    Log.e("ProcessBarcodeHandler", error.message, error)
                }
                .onErrorReturnItem(BarcodeError)
                .toObservable()
        }
    }
}