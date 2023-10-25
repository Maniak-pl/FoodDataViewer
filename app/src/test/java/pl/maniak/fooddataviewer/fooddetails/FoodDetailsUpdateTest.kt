package pl.maniak.fooddataviewer.fooddetails

import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Before
import org.junit.Test
import pl.maniak.fooddataviewer.model.Product

class FoodDetailsUpdateTest {
    private lateinit var updateSpec: UpdateSpec<FoodDetailsModel, FoodDetailsEvent, FoodDetailsEffect>

    @Before
    fun before() {
        updateSpec = UpdateSpec(::foodDetailsUpdate)
    }

    @Test
    fun initialEvent_activityTrue_loadProductDispatched() {
        val model = FoodDetailsModel()
        val barcode = "1234567890"
        updateSpec
            .given(model)
            .whenEvent(Initial(barcode))
            .then(
                assertThatNext(
                    hasModel(model.copy(activity = true)),
                    hasEffects(LoadProduct(barcode) as FoodDetailsEffect)
                )
            )
    }

    @Test
    fun productLoaded_activityFalseProduct() {
        val model = FoodDetailsModel(activity = true)
        val product = Product(
            id = "1234567890",
            saved = false,
            name = "name",
            brands = "brands",
            imageUrl = "imageUrl",
            nutriments = null,
            ingredients = "ingredients"
        )
        updateSpec
            .given(model)
            .whenEvent(ProductLoaded(product))
            .then(
                assertThatNext(
                    hasModel(model.copy(activity = false, product = product)),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun errorLoadingProduct_activityFalseErrorTrue() {
        val model = FoodDetailsModel(activity = true)
        updateSpec
            .given(model)
            .whenEvent(ErrorLoadingProduct)
            .then(
                assertThatNext(
                    hasModel(model.copy(activity = false, error = true)),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun actionButtonClicked_deleteProductDispatched() {
        val product = Product(
            id = "1234567890",
            saved = true,
            name = "name",
            brands = "brands",
            imageUrl = "imageUrl",
            nutriments = null,
            ingredients = "ingredients"
        )
        val model = FoodDetailsModel(product = product)
        updateSpec
            .given(model)
            .whenEvent(ActionButtonClicked)
            .then(
                assertThatNext(
                    hasModel(model.copy(product = product.copy(saved = false))),
                    hasEffects(DeleteProduct(product.id) as FoodDetailsEffect)
                )
            )
    }

    @Test
    fun actionButtonClicked_saveProductDispatched() {
        val product = Product(
            id = "1234567890",
            saved = false,
            name = "name",
            brands = "brands",
            imageUrl = "imageUrl",
            nutriments = null,
            ingredients = "ingredients"
        )
        val model = FoodDetailsModel(product = product)
        updateSpec
            .given(model)
            .whenEvent(ActionButtonClicked)
            .then(
                assertThatNext(
                    hasModel(model.copy(product = model.product!!.copy(saved = !product.saved))),
                    hasEffects(SaveProduct(product))
                )
            )
    }

    @Test
    fun actionButtonClicked_noChange() {
        val model = FoodDetailsModel()
        updateSpec
            .given(model)
            .whenEvent(ActionButtonClicked)
            .then(
                assertThatNext(
                    hasNothing()
                )
            )
    }
}