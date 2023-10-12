package pl.maniak.fooddataviewer.scan

import io.fotoapparat.preview.Frame

sealed class ScanEvent

data class Captured(val frame: Frame): ScanEvent()

data class Detected(val barcode: String): ScanEvent()