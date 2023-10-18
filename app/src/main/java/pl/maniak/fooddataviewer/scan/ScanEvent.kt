package pl.maniak.fooddataviewer.scan

import io.fotoapparat.preview.Frame
import pl.maniak.fooddataviewer.model.Product

sealed class ScanEvent

data class Captured(val frame: Frame): ScanEvent()

data class Detected(val barcode: String): ScanEvent()

data class ProductLoaded(val product: Product): ScanEvent()

object BarcodeError: ScanEvent()

object ProductInfoClicked: ScanEvent()
