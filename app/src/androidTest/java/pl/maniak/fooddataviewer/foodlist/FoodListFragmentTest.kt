package pl.maniak.fooddataviewer.foodlist

import android.content.res.Resources
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test
import pl.maniak.fooddataviewer.AndroidTestApplication
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.model.dto.NutrimentsDto
import pl.maniak.fooddataviewer.model.dto.ProductDto
import pl.maniak.fooddataviewer.utils.withRecyclerView

class FoodListFragmentTest {

    private val productDto = ProductDto(
        id = "8888002076009",
        product_name = "Coke Classic",
        brands = "Coca-Cola",
        image_url = "https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.400.jpg",
        ingredients_text_debug = "CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, SUCROSE, CARAMEL COLOUR, PHOSPHORIC ACID, FLAVOURINGS, CAFFEINE",
        nutriments = NutrimentsDto(
            energy_100g = 176,
            salt_100g = 0.005,
            carbohydrates_100g = 10.6,
            fiber_100g = 0.0,
            sugars_100g = 10.6,
            proteins_100g = 0.0,
            fat_100g = 0.0
        )
    )

    @Test
    fun views() {
        val scenario =
            launchFragmentInContainer<FoodListFragment>(
                themeResId = R.style.Theme_FoodDataViewer
            )
        var resources: Resources? = null
        scenario.onFragment { fragment ->
            resources = fragment.resources
            (fragment.activity!!.applicationContext as AndroidTestApplication).productDtoSubject.onNext(
                listOf(
                    productDto
                )
            )
        }
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.productNameView))
            .check(matches(withText(productDto.product_name)))

        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.brandNameView))
            .check(matches(withText(productDto.brands)))
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.energyValueView))
            .check(
                matches(
                    withText(
                        resources!!.getString(
                            R.string.food_list_energy_value, productDto.nutriments!!.energy_100g

                        )
                    )
                )
            )
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.carbsValueView))
            .check(
                matches(
                    withText(
                        resources!!.getString(
                            R.string.food_list_macro_value, productDto.nutriments!!.carbohydrates_100g
                        )
                    )
                )
            )
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.fatValueView))
        matches(
            withText(
                resources!!.getString(
                    R.string.food_list_macro_value, productDto.nutriments!!.fat_100g

                )
            )
        )
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.productNameView))
        matches(
            withText(
                resources!!.getString(
                    R.string.food_list_macro_value, productDto.nutriments!!.fat_100g

                )
            )
        )
    }

    @Test
    fun views2() {
        val scenario =
            launchFragmentInContainer<FoodListFragment>(
                themeResId = R.style.Theme_FoodDataViewer
            )
        val otherName = "name2"
        scenario.onFragment { fragment ->
            (fragment.activity!!.applicationContext as AndroidTestApplication).productDtoSubject.onNext(
                listOf(
                    productDto,
                    productDto.copy(product_name = otherName)
                )
            )
        }
        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(1, R.id.productNameView))
            .check(matches(withText(otherName)))
    }
}