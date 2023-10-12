package pl.maniak.fooddataviewer.scan.handlers

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.maniak.fooddataviewer.scan.ProcessBarcode
import pl.maniak.fooddataviewer.scan.ScanEvent
import javax.inject.Inject

class ProcessBarcodeHandler @Inject constructor(): ObservableTransformer<ProcessBarcode, ScanEvent> {
    override fun apply(upstream: Observable<ProcessBarcode>): ObservableSource<ScanEvent> {
         TODO("Not yet implemented - model ProductService")
    }
}