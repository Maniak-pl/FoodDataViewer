package pl.maniak.fooddataviewer.scan

import io.fotoapparat.preview.Frame

sealed class ScanEffect

data class ProcessCameraFrame(val frame: Frame): ScanEffect()