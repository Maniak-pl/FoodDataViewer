package pl.maniak.fooddataviewer

import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import io.fotoapparat.parameter.Resolution
import io.fotoapparat.preview.Frame
import org.junit.Before
import org.junit.Test
import pl.maniak.fooddataviewer.model.Product
import pl.maniak.fooddataviewer.scan.*

class ScanUpdateTest {
    private lateinit var updateSpec: UpdateSpec<ScanModel, ScanEvent, ScanEffect>

    @Before
    fun before() {
        updateSpec = UpdateSpec(::scanUpdate)
    }

    @Test
    fun capturedEvent_processFrameDispatched() {
        val model = ScanModel()

        val frame = Frame(
            size = Resolution(width = 100, height = 100),
            image = ByteArray(100),
            rotation = 0
        )
        updateSpec
            .given(model)
            .whenEvent(Captured(frame))
            .then(
                assertThatNext(
                    hasNoModel(),
                    hasEffects(ProcessCameraFrame(frame) as ScanEffect)
                )
            )
    }

    @Test
    fun detectedEvent_activity_processBarcodeDispatched() {
        val model = ScanModel()
        val barcode = "1234567890"
        updateSpec
            .given(model)
            .whenEvent(Detected(barcode))
            .then(
                assertThatNext(
                    hasModel(model.copy(activity = true)),
                    hasEffects(ProcessBarcode(barcode))
                )
            )
    }

    @Test
    fun detectedEvent_noChange() {
        val model = ScanModel(activity = true)
        val barcode = "1234567890"
        updateSpec
            .given(model)
            .whenEvent(Detected(barcode))
            .then(
                assertThatNext(
                    hasNothing()
                )
            )
    }

    @Test
    fun barcodeErrorEvent_activityFalseProcessBarcodeResultError() {
        val model = ScanModel(activity = false)
        updateSpec
            .given(model)
            .whenEvent(BarcodeError)
            .then(
                assertThatNext(
                    hasModel(
                        model.copy(
                            activity = false,
                            processBarcodeResult = ProcessBarcodeResult.Error
                        )
                    ),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun productLoadedEvent_activityFalseProductLoaded() {
        val model = ScanModel(activity = true)
        val product = Product(
            id = "1234567890",
            saved = false,
            name = "Product",
            brands = "Brand",
            imageUrl = "https://static.openfoodfacts.org/images/products/1234567890/front_en.3.400.jpg",
            ingredients = "lots of sugar",
            nutriments = null
        )
        updateSpec
            .given(model)
            .whenEvent(ProductLoaded(product))
            .then(
                assertThatNext(
                    hasModel(
                        model.copy(
                            activity = false,
                            processBarcodeResult = ProcessBarcodeResult.ProductLoaded(product)
                        )
                    ),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun productInfoClickedEvent_navigateToFoodDetailsDispatched() {
        val product = Product(
            id = "1234567890",
            saved = false,
            name = "Product",
            brands = "Brand",
            imageUrl = "https://static.openfoodfacts.org/images/products/1234567890/front_en.3.400.jpg",
            ingredients = "lots of sugar",
            nutriments = null
        )
        val model = ScanModel(
            activity = false,
            processBarcodeResult = ProcessBarcodeResult.ProductLoaded(product)
        )
        updateSpec
            .given(model)
            .whenEvent(ProductInfoClicked)
            .then(
                assertThatNext(
                    hasNoModel(),
                    hasEffects(NavigateToFoodsDetails(product.id))
                )
            )
    }

    @Test
    fun productInfoClickedEvent_noChange() {
        val model = ScanModel()
        updateSpec
            .given(model)
            .whenEvent(ProductInfoClicked)
            .then(
                assertThatNext(
                    hasNothing()
                )
            )
    }
}