package pl.maniak.fooddataviewer.scan

import com.spotify.mobius.Next
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import pl.maniak.fooddataviewer.MobiusVM
import javax.inject.Inject

fun scanUpdate(
    model: ScanModel,
    event: ScanEvent
): Next<ScanModel, ScanEffect> {
    return when (event) {
        is Captured -> Next.dispatch(setOf(ProcessCameraFrame(event.frame)))
        is Detected -> TODO()
    }
}

class ScanViewModel @Inject constructor() : MobiusVM<ScanModel, ScanEvent, ScanEffect>(
    "ScanViewModel",
    Update(::scanUpdate),
    ScanModel,
    RxMobius.subtypeEffectHandler<ScanEffect, ScanEvent>()
        .build()
)