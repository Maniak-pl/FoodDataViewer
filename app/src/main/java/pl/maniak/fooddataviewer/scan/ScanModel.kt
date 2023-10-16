package pl.maniak.fooddataviewer.scan

import pl.maniak.fooddataviewer.model.Product
import pl.maniak.fooddataviewer.scan.ProcessBarcodeResult.Empty

data class ScanModel(
    val activity: Boolean = false,
    val processBarcodeResult: ProcessBarcodeResult = Empty,
)

sealed class ProcessBarcodeResult {
    object Empty : ProcessBarcodeResult()

    object Error : ProcessBarcodeResult()

    data class ProductLoaded(val product: Product) : ProcessBarcodeResult()
}