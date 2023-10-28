package pl.maniak.fooddataviewer.scan

import io.fotoapparat.preview.Frame
import pl.maniak.fooddataviewer.scan.utils.FrameProcessorOnSubscribe

class TestFrameProcessorOnSubscribe : FrameProcessorOnSubscribe() {

    var testFrame: Frame? = null

    override fun invoke(frame: Frame) {
        if (testFrame != null) {
            super.invoke(frame)
        }
    }
}