package pl.maniak.fooddataviewer.scan

import com.spotify.mobius.Next
import com.spotify.mobius.Next.*
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import pl.maniak.fooddataviewer.MobiusVM
import pl.maniak.fooddataviewer.scan.handlers.ProcessBarcodeHandler
import pl.maniak.fooddataviewer.scan.handlers.ProcessFrameHandler
import pl.maniak.fooddataviewer.utils.Navigator
import javax.inject.Inject

fun scanUpdate(
    model: ScanModel,
    event: ScanEvent
): Next<ScanModel, ScanEffect> {
    return when (event) {
        is Captured -> dispatch(setOf(ProcessCameraFrame(event.frame)))
        is Detected -> if (!model.activity) {
            next(
                model.copy(activity = true),
                setOf(ProcessBarcode(event.barcode))
            )
        } else {
            noChange()
        }
        is ProductLoaded -> next(
            model.copy(
                activity = false,
                processBarcodeResult = ProcessBarcodeResult.ProductLoaded(event.product)
            )
        )
        is BarcodeError -> next(
            model.copy(
                activity = false,
                processBarcodeResult = ProcessBarcodeResult.Error
            )
        )
        is ProductInfoClicked -> if (model.processBarcodeResult is ProcessBarcodeResult.ProductLoaded) {
            dispatch(
                setOf(NavigateToFoodsDetails(model.processBarcodeResult.product.id))
            )
        } else {
            noChange()
        }
    }
}

class ScanViewModel @Inject constructor(
    processCameraFrameHandler: ProcessFrameHandler,
    processBarcodeHandler: ProcessBarcodeHandler,
    navigator: Navigator,
) : MobiusVM<ScanModel, ScanEvent, ScanEffect>(
    "ScanViewModel",
    Update(::scanUpdate),
    ScanModel(),
    RxMobius.subtypeEffectHandler<ScanEffect, ScanEvent>()
        .addTransformer(ProcessCameraFrame::class.java, processCameraFrameHandler)
        .addTransformer(ProcessBarcode::class.java, processBarcodeHandler)
        .addConsumer(NavigateToFoodsDetails::class.java) { effect ->
            navigator.to(ScanFragmentDirections.foodDetails(effect.barcode))
        }
        .build()
)