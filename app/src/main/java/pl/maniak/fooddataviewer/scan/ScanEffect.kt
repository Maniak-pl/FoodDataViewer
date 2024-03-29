package pl.maniak.fooddataviewer.scan

import io.fotoapparat.preview.Frame

sealed class ScanEffect

data class ProcessCameraFrame(val frame: Frame): ScanEffect()

data class ProcessBarcode(val barcode: String): ScanEffect()

data class NavigateToFoodsDetails(val barcode: String): ScanEffect()