package pl.maniak.fooddataviewer.scan

import com.spotify.mobius.Next
import com.spotify.mobius.Next.noChange
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import pl.maniak.fooddataviewer.MobiusVM
import pl.maniak.fooddataviewer.scan.handlers.ProcessFrameHandler
import javax.inject.Inject

fun scanUpdate(
    model: ScanModel,
    event: ScanEvent
): Next<ScanModel, ScanEffect> {
    return when (event) {
        is Captured -> Next.dispatch(setOf(ProcessCameraFrame(event.frame)))
        is Detected -> if (!model.activity) {
            Next.next(
                model.copy(activity = true),
                setOf(ProcessBarcode(event.barcode))
            )
        } else {
            noChange()
        }
    }
}

class ScanViewModel @Inject constructor(
    processCameraFrameHandler: ProcessFrameHandler
) : MobiusVM<ScanModel, ScanEvent, ScanEffect>(
    "ScanViewModel",
    Update(::scanUpdate),
    ScanModel(),
    RxMobius.subtypeEffectHandler<ScanEffect, ScanEvent>()
        .addTransformer(ProcessCameraFrame::class.java, processCameraFrameHandler)
        .build()
)