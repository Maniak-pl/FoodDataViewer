package pl.maniak.fooddataviewer.foodlist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test
import pl.maniak.fooddataviewer.AndroidTestApplication
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.model.dto.NutrimentsDto
import pl.maniak.fooddataviewer.model.dto.ProductDto

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
            launchFragmentInContainer<FoodListFragment>(themeResId = R.style.Theme_FoodDataViewer)
        scenario.onFragment { fragment ->
            ((fragment.activity!!.applicationContext as AndroidTestApplication)).productDaoSubject.onNext(
                listOf(productDto)
            )
        }
        onView(withId(R.id.productNameView)).check(matches(withText(productDto.product_name)))
    }
}