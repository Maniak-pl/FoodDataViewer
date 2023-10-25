package pl.maniak.fooddataviewer.foodlist

import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import org.junit.Before
import org.junit.Test
import pl.maniak.fooddataviewer.model.Product

class FoodListUpdateTest {

    private lateinit var updateSpec: UpdateSpec<FoodListModel, FoodListEvent, FoodListEffect>

    @Before
    fun setUp() {
        updateSpec = UpdateSpec(::foodListUpdate)
    }

    @Test
    fun initial_observeProductsDispatched() {
        val model = FoodListModel()
        updateSpec
            .given(model)
            .whenEvent(Initial)
            .then(
                UpdateSpec.assertThatNext(
                    hasNoModel(),
                    hasEffects(ObserveProducts as FoodListEffect)
                )
            )
    }

    @Test
    fun addButtonClicked_NavigateToScannerDispatched() {
        val model = FoodListModel()
        updateSpec
            .given(model)
            .whenEvent(AddButtonClicked)
            .then(
                UpdateSpec.assertThatNext(
                    hasNoModel(),
                    hasEffects(NavigateToScanner as FoodListEffect)
                )
            )
    }

    @Test
    fun productLoaded_productsUpdated() {
        val model = FoodListModel()
        val products = listOf(
            Product("1", false, "name", "brands", "imageUrl", "ingredients", null)
        )
        updateSpec
            .given(model)
            .whenEvent(ProductsLoaded(products))
            .then(
                UpdateSpec.assertThatNext(
                    hasModel(model.copy(products = products)),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun productClicked_navigationToFoodDetailsDispatched() {
        val model = FoodListModel()
        val barcode = "123456789"
        updateSpec
            .given(model)
            .whenEvent(ProductClicked(barcode))
            .then(
                UpdateSpec.assertThatNext(
                    hasNoModel(),
                    hasEffects(NavigateToFoodDetails(barcode) as FoodListEffect)
                )
            )
    }
}