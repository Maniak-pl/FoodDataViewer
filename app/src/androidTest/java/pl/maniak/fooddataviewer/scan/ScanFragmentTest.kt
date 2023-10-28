package pl.maniak.fooddataviewer.scan

import android.Manifest
import android.content.res.Resources
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.GrantPermissionRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.component
import pl.maniak.fooddataviewer.di.TestComponent
import pl.maniak.fooddataviewer.utils.TestIdlingResource

class ScanFragmentTest {

    @Rule
    @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

    private val mockWebServer = MockWebServer()

    private val json =
        "{\"status_verbose\":\"product found\",\"code\":\"8888002076009\",\"status\":1,\"product\":{\"allergens_hierarchy\":[],\"generic_name_en\":\"\",\"allergens_from_user\":\"(en)\",\"link\":\"\",\"additives_n\":2,\"languages\":{\"en:english\":5,\"en:french\":2},\"image_nutrition_thumb_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/nutrition_en.15.100.jpg\",\"data_quality_info_tags\":[\"en:nutriscore-computations-same-score\",\"en:nutriscore-computations-same-grade\"],\"nutrient_levels\":{\"saturated-fat\":\"low\",\"sugars\":\"high\",\"fat\":\"low\",\"salt\":\"low\"},\"ingredients_n_tags\":[\"7\",\"1-10\"],\"ingredients_n\":7,\"labels_tags\":[\"en:halal\"],\"product_name_fr\":\"Coca-Cola\",\"countries_lc\":\"en\",\"emb_codes_20141016\":\"\",\"photographers_tags\":[\"openfoodfacts-contributors\",\"kiliweb\",\"bcd4e6\"],\"ingredients_text_debug\":\"CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, SUCROSE, CARAMEL COLOUR, PHOSPHORIC ACID, FLAVOURINGS, CAFFEINE\",\"nutrition_grade_fr\":\"e\",\"states_tags\":[\"en:to-be-checked\",\"en:complete\",\"en:nutrition-facts-completed\",\"en:ingredients-completed\",\"en:expiration-date-to-be-completed\",\"en:packaging-code-to-be-completed\",\"en:characteristics-completed\",\"en:categories-completed\",\"en:brands-completed\",\"en:packaging-completed\",\"en:quantity-completed\",\"en:product-name-completed\",\"en:photos-validated\",\"en:photos-uploaded\"],\"last_modified_by\":\"bredowmax\",\"images\":{\"4\":{\"uploader\":\"bcd4e6\",\"sizes\":{\"100\":{\"h\":47,\"w\":100},\"400\":{\"h\":189,\"w\":400},\"full\":{\"w\":1611,\"h\":763}},\"uploaded_t\":1555473424},\"ingredients_fr\":{\"rev\":\"6\",\"y1\":null,\"geometry\":\"0x0-0-0\",\"normalize\":\"0\",\"sizes\":{\"200\":{\"h\":200,\"w\":176},\"full\":{\"h\":1200,\"w\":1055},\"400\":{\"h\":400,\"w\":352},\"100\":{\"w\":88,\"h\":100}},\"x2\":null,\"white_magic\":\"0\",\"imgid\":\"2\",\"angle\":null,\"y2\":null,\"orientation\":null,\"x1\":null,\"ocr\":1},\"2\":{\"sizes\":{\"full\":{\"h\":1200,\"w\":1055},\"400\":{\"h\":400,\"w\":352},\"100\":{\"h\":100,\"w\":88}},\"uploaded_t\":1552546412,\"uploader\":\"kiliweb\"},\"5\":{\"sizes\":{\"100\":{\"w\":100,\"h\":52},\"400\":{\"h\":208,\"w\":400},\"full\":{\"w\":1810,\"h\":941}},\"uploaded_t\":1555473505,\"uploader\":\"bcd4e6\"},\"3\":{\"uploader\":\"bcd4e6\",\"sizes\":{\"full\":{\"w\":1447,\"h\":3793},\"100\":{\"h\":100,\"w\":38},\"400\":{\"h\":400,\"w\":153}},\"uploaded_t\":1555473390},\"ingredients_en\":{\"sizes\":{\"200\":{\"w\":200,\"h\":95},\"full\":{\"w\":1611,\"h\":763},\"400\":{\"h\":189,\"w\":400},\"100\":{\"h\":47,\"w\":100}},\"normalize\":null,\"rev\":\"12\",\"y1\":null,\"geometry\":\"0x0-0-0\",\"angle\":null,\"x2\":null,\"imgid\":\"4\",\"white_magic\":null,\"x1\":null,\"y2\":null,\"orientation\":null,\"ocr\":1},\"front_en\":{\"imgid\":\"3\",\"white_magic\":null,\"x2\":null,\"angle\":null,\"geometry\":\"0x0-0-0\",\"rev\":\"9\",\"y1\":null,\"normalize\":null,\"sizes\":{\"full\":{\"h\":3793,\"w\":1447},\"200\":{\"w\":76,\"h\":200},\"100\":{\"w\":38,\"h\":100},\"400\":{\"h\":400,\"w\":153}},\"y2\":null,\"x1\":null},\"nutrition_en\":{\"ocr\":1,\"x1\":null,\"y2\":null,\"orientation\":null,\"angle\":null,\"imgid\":\"5\",\"white_magic\":null,\"x2\":null,\"sizes\":{\"100\":{\"w\":100,\"h\":52},\"400\":{\"w\":400,\"h\":208},\"full\":{\"h\":941,\"w\":1810},\"200\":{\"h\":104,\"w\":200}},\"normalize\":null,\"geometry\":\"0x0-0-0\",\"y1\":null,\"rev\":\"15\"},\"1\":{\"uploader\":\"openfoodfacts-contributors\",\"sizes\":{\"full\":{\"w\":214,\"h\":236},\"100\":{\"h\":100,\"w\":91},\"400\":{\"w\":214,\"h\":236}},\"uploaded_t\":1432938553}},\"ingredients_from_palm_oil_n\":0,\"emb_codes\":\"\",\"last_edit_dates_tags\":[\"2019-10-07\",\"2019-10\",\"2019\"],\"product_name\":\"Coke Classic\",\"stores_tags\":[],\"image_nutrition_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/nutrition_en.15.400.jpg\",\"traces\":\"\",\"generic_name\":\"\",\"image_small_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.200.jpg\",\"unique_scans_n\":11,\"rev\":18,\"nova_groups\":\"4\",\"interface_version_modified\":\"20190830\",\"brands_tags\":[\"coca-cola\"],\"labels\":\"Halal\",\"nutrition_data\":\"on\",\"image_ingredients_thumb_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_en.12.100.jpg\",\"minerals_tags\":[],\"ingredients_text\":\"CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, SUCROSE, CARAMEL COLOUR, PHOSPHORIC ACID, FLAVOURINGS, CAFFEINE\",\"nutriments\":{\"fat\":0,\"energy-kcal\":176,\"energy-kcal_unit\":\"kcal\",\"saturated-fat_serving\":0,\"fat_value\":0,\"salt\":0.005,\"sugars_100g\":10.6,\"nutrition-score-fr_100g\":14,\"sodium_value\":2,\"saturated-fat\":0,\"energy_unit\":\"kcal\",\"saturated-fat_unit\":\"g\",\"fat_unit\":\"g\",\"sodium_unit\":\"mg\",\"fat_100g\":0,\"sodium\":0.002,\"nova-group\":4,\"nutrition-score-uk\":2,\"proteins\":0,\"salt_unit\":\"mg\",\"sodium_serving\":0.0064,\"salt_100g\":0.005,\"nutrition-score-fr\":14,\"carbohydrates_value\":10.6,\"sugars\":10.6,\"salt_value\":5,\"proteins_unit\":\"g\",\"carbohydrates_unit\":\"g\",\"fat_serving\":0,\"nova-group_serving\":4,\"energy-kcal_value\":42,\"proteins_value\":0,\"nova-group_100g\":4,\"energy_serving\":563,\"energy_value\":42,\"sodium_100g\":0.002,\"energy-kcal_serving\":563,\"proteins_100g\":0,\"proteins_serving\":0,\"saturated-fat_100g\":0,\"saturated-fat_value\":0,\"salt_serving\":0.016,\"energy_100g\":176,\"energy-kcal_100g\":176,\"carbohydrates\":10.6,\"sugars_value\":10.6,\"sugars_unit\":\"g\",\"sugars_serving\":33.9,\"carbohydrates_100g\":10.6,\"nutrition-score-uk_100g\":2,\"carbohydrates_serving\":33.9,\"energy\":176},\"countries_hierarchy\":[\"en:algeria\",\"en:france\",\"en:singapore\"],\"sortkey\":301570451659,\"additives_prev_original_tags\":[\"en:e150a\",\"en:e338\"],\"serving_size\":\"320 ml\",\"interface_version_created\":\"20120622\",\"labels_hierarchy\":[\"en:halal\"],\"emb_codes_tags\":[],\"data_quality_bugs_tags\":[],\"minerals_prev_tags\":[],\"traces_tags\":[],\"correctors_tags\":[\"openfoodfacts-contributors\",\"yuka.VC9nZ01Za2VwY0pYb2ZNeS9nT081dUJmK3BLblVVenFPKzRMSWc9PQ\",\"bcd4e6\",\"fixbot\",\"bredowmax\"],\"ingredients_text_fr\":\"\",\"lc\":\"en\",\"last_image_t\":1555473506,\"ingredients_text_with_allergens_en\":\"CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, SUCROSE, CARAMEL COLOUR, PHOSPHORIC ACID, FLAVOURINGS, CAFFEINE\",\"generic_name_fr\":\"\",\"packaging_tags\":[\"can\"],\"additives_original_tags\":[\"en:e150c\",\"en:e338\"],\"pnns_groups_1\":\"Beverages\",\"nutrition_data_per\":\"100g\",\"codes_tags\":[\"code-13\",\"8888002076xxx\",\"888800207xxxx\",\"88880020xxxxx\",\"8888002xxxxxx\",\"888800xxxxxxx\",\"88880xxxxxxxx\",\"8888xxxxxxxxx\",\"888xxxxxxxxxx\",\"88xxxxxxxxxxx\",\"8xxxxxxxxxxxx\"],\"scans_n\":18,\"_id\":\"8888002076009\",\"completed_t\":1555473527,\"origins\":\"\",\"amino_acids_tags\":[],\"max_imgid\":\"5\",\"manufacturing_places\":\"Malaysia\",\"pnns_groups_2\":\"Artificially sweetened beverages\",\"nutriscore_grade\":\"e\",\"manufacturing_places_tags\":[\"malaysia\"],\"stores\":\"\",\"allergens_tags\":[],\"traces_from_user\":\"(en)\",\"product_name_en\":\"Coke Classic\",\"ingredients_text_with_allergens\":\"CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, SUCROSE, CARAMEL COLOUR, PHOSPHORIC ACID, FLAVOURINGS, CAFFEINE\",\"nutrition_data_prepared_per\":\"100g\",\"additives_tags\":[\"en:e150c\",\"en:e338\"],\"origins_tags\":[],\"nutrition_score_warning_no_fruits_vegetables_nuts\":1,\"ingredients_that_may_be_from_palm_oil_tags\":[],\"nutrition_grades_tags\":[\"e\"],\"ingredients_analysis_tags\":[\"en:palm-oil-free\",\"en:maybe-vegan\",\"en:maybe-vegetarian\"],\"languages_hierarchy\":[\"en:english\",\"en:french\"],\"selected_images\":{\"front\":{\"thumb\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.100.jpg\"},\"display\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.400.jpg\"},\"small\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.200.jpg\"}},\"ingredients\":{\"thumb\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_en.12.100.jpg\",\"fr\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_fr.6.100.jpg\"},\"display\":{\"fr\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_fr.6.400.jpg\",\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_en.12.400.jpg\"},\"small\":{\"fr\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_fr.6.200.jpg\",\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_en.12.200.jpg\"}},\"nutrition\":{\"thumb\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/nutrition_en.15.100.jpg\"},\"small\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/nutrition_en.15.200.jpg\"},\"display\":{\"en\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/nutrition_en.15.400.jpg\"}}},\"image_front_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.400.jpg\",\"unknown_ingredients_n\":0,\"completeness\":0.8,\"allergens_from_ingredients\":\"\",\"labels_lc\":\"en\",\"traces_hierarchy\":[],\"traces_from_ingredients\":\"\",\"nova_groups_tags\":[\"en:4-ultra-processed-food-and-drink-products\"],\"categories\":\"Beverages,Artificially sweetened beverages,Carbonated drinks,Diet beverages,Sodas,Diet sodas,Colas,Diet cola soft drink\",\"countries_tags\":[\"en:algeria\",\"en:france\",\"en:singapore\"],\"image_ingredients_small_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_en.12.200.jpg\",\"states\":\"en:to-be-checked, en:complete, en:nutrition-facts-completed, en:ingredients-completed, en:expiration-date-to-be-completed, en:packaging-code-to-be-completed, en:characteristics-completed, en:categories-completed, en:brands-completed, en:packaging-completed, en:quantity-completed, en:product-name-completed, en:photos-validated, en:photos-uploaded\",\"nucleotides_prev_tags\":[],\"nutrition_score_beverage\":1,\"image_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.400.jpg\",\"data_sources_tags\":[\"app-yuka\",\"apps\"],\"additives_old_n\":1,\"data_quality_errors_tags\":[],\"nutrient_levels_tags\":[\"en:fat-in-low-quantity\",\"en:saturated-fat-in-low-quantity\",\"en:sugars-in-high-quantity\",\"en:salt-in-low-quantity\"],\"last_image_dates_tags\":[\"2019-04-17\",\"2019-04\",\"2019\"],\"pnns_groups_2_tags\":[\"artificially-sweetened-beverages\",\"known\"],\"nutrition_grades\":\"e\",\"nutriscore_data\":{\"grade\":\"e\",\"proteins_points\":0,\"fiber_value\":0,\"saturated_fat\":0,\"is_water\":0,\"is_fat\":0,\"fiber_points\":0,\"sodium_value\":2,\"saturated_fat_ratio_value\":0,\"fruits_vegetables_nuts_colza_walnut_olive_oils_value\":0,\"saturated_fat_value\":0,\"score\":14,\"energy_points\":6,\"saturated_fat_ratio_points\":0,\"sodium\":2,\"sugars_points\":8,\"proteins\":0,\"sugars\":10.6,\"is_beverage\":1,\"is_cheese\":0,\"proteins_value\":0,\"saturated_fat_points\":0,\"fiber\":0,\"fruits_vegetables_nuts_colza_walnut_olive_oils\":0,\"positive_points\":0,\"saturated_fat_ratio\":0,\"energy_value\":176,\"sodium_points\":0,\"sugars_value\":10.6,\"energy\":176,\"fruits_vegetables_nuts_colza_walnut_olive_oils_points\":0,\"negative_points\":14},\"expiration_date\":\"\",\"misc_tags\":[\"en:nutrition-no-fruits-vegetables-nuts\",\"en:nutrition-no-fiber-or-fruits-vegetables-nuts\",\"en:nutriscore-computed\"],\"editors\":[\"\"],\"states_hierarchy\":[\"en:to-be-checked\",\"en:complete\",\"en:nutrition-facts-completed\",\"en:ingredients-completed\",\"en:expiration-date-to-be-completed\",\"en:packaging-code-to-be-completed\",\"en:characteristics-completed\",\"en:categories-completed\",\"en:brands-completed\",\"en:packaging-completed\",\"en:quantity-completed\",\"en:product-name-completed\",\"en:photos-validated\",\"en:photos-uploaded\"],\"nutrition_data_prepared\":\"\",\"data_sources\":\"App - yuka, Apps\",\"nucleotides_tags\":[],\"debug_param_sorted_langs\":[\"en\",\"fr\"],\"ingredients_from_palm_oil_tags\":[],\"pnns_groups_1_tags\":[\"beverages\",\"known\"],\"brands\":\"Coca-Cola\",\"nova_group_debug\":\" -- categories/en:sodas : 3 -- additives/en:e150c : 4\",\"creator\":\"openfoodfacts-contributors\",\"ingredients_ids_debug\":[\"carbonated-water\",\"high-fructose-corn-syrup\",\"sucrose\",\"caramel-colour\",\"phosphoric-acid\",\"flavourings\",\"caffeine\"],\"complete\":1,\"product_quantity\":320,\"quantity\":\"320 ml\",\"entry_dates_tags\":[\"2015-05-30\",\"2015-05\",\"2015\"],\"packaging\":\"Can\",\"ingredients_original_tags\":[\"en:carbonated-water\",\"en:high-fructose-corn-syrup\",\"en:sucrose\",\"en:e150a\",\"en:e338\",\"en:flavouring\",\"en:caffeine\"],\"cities_tags\":[],\"serving_quantity\":320,\"compared_to_category\":\"en:sweetened-beverages\",\"additives_debug_tags\":[],\"categories_hierarchy\":[\"en:beverages\",\"en:carbonated-drinks\",\"en:artificially-sweetened-beverages\",\"en:sodas\",\"en:diet-beverages\",\"en:colas\",\"en:diet-sodas\",\"en:diet-cola-soft-drink\"],\"additives_old_tags\":[\"en:e150a\"],\"other_nutritional_substances_tags\":[],\"ingredients_from_or_that_may_be_from_palm_oil_n\":0,\"ingredients_text_en\":\"CARBONATED WATER, HIGH FRUCTOSE CORN SYRUP, SUCROSE, CARAMEL COLOUR, PHOSPHORIC ACID, FLAVOURINGS, CAFFEINE\",\"vitamins_prev_tags\":[],\"unknown_nutrients_tags\":[],\"languages_codes\":{\"fr\":2,\"en\":5},\"categories_lc\":\"en\",\"ingredients\":[{\"rank\":1,\"text\":\"CARBONATED WATER\",\"id\":\"en:carbonated-water\",\"vegetarian\":\"yes\",\"vegan\":\"yes\"},{\"text\":\"HIGH FRUCTOSE CORN SYRUP\",\"id\":\"en:high-fructose-corn-syrup\",\"vegetarian\":\"yes\",\"vegan\":\"yes\",\"rank\":2},{\"rank\":3,\"vegan\":\"yes\",\"vegetarian\":\"yes\",\"text\":\"SUCROSE\",\"id\":\"en:sucrose\"},{\"rank\":4,\"id\":\"en:e150a\",\"text\":\"CARAMEL COLOUR\",\"vegetarian\":\"yes\",\"vegan\":\"yes\"},{\"rank\":5,\"vegan\":\"yes\",\"vegetarian\":\"yes\",\"id\":\"en:e338\",\"text\":\"PHOSPHORIC ACID\"},{\"vegan\":\"maybe\",\"vegetarian\":\"maybe\",\"id\":\"en:flavouring\",\"text\":\"FLAVOURINGS\",\"rank\":6},{\"rank\":7,\"id\":\"en:caffeine\",\"text\":\"CAFFEINE\",\"vegetarian\":\"yes\",\"vegan\":\"yes\"}],\"traces_lc\":\"en\",\"categories_tags\":[\"en:beverages\",\"en:carbonated-drinks\",\"en:artificially-sweetened-beverages\",\"en:sodas\",\"en:diet-beverages\",\"en:colas\",\"en:diet-sodas\",\"en:diet-cola-soft-drink\",\"en:non-alcoholic-beverages\",\"en:sweetened-beverages\"],\"allergens\":\"\",\"popularity_tags\":[\"top-50000-scans-2019\",\"top-100000-scans-2019\",\"at-least-5-scans-2019\",\"at-least-10-scans-2019\",\"top-country-be-scans-2019\"],\"nutriscore_score\":14,\"image_thumb_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.100.jpg\",\"lang\":\"en\",\"image_ingredients_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/ingredients_en.12.400.jpg\",\"last_editor\":\"bredowmax\",\"ingredients_hierarchy\":[\"en:carbonated-water\",\"en:water\",\"en:high-fructose-corn-syrup\",\"en:glucose\",\"en:fructose\",\"en:corn-syrup\",\"en:glucose-fructose-syrup\",\"en:sucrose\",\"en:sugar\",\"en:e150a\",\"en:e338\",\"en:flavouring\",\"en:caffeine\"],\"ingredients_debug\":[\"CARBONATED WATER\",\",\",null,null,null,\" HIGH FRUCTOSE CORN SYRUP\",\",\",null,null,null,\" SUCROSE\",\",\",null,null,null,\" CARAMEL COLOUR\",\",\",null,null,null,\" PHOSPHORIC ACID\",\",\",null,null,null,\" FLAVOURINGS\",\",\",null,null,null,\" CAFFEINE\"],\"ingredients_that_may_be_from_palm_oil_n\":0,\"amino_acids_prev_tags\":[],\"no_nutrition_data\":\"\",\"allergens_lc\":\"en\",\"languages_tags\":[\"en:english\",\"en:french\",\"en:2\",\"en:multilingual\"],\"data_quality_warnings_tags\":[],\"purchase_places_tags\":[\"singapore\"],\"countries\":\"Algeria,France,Singapore\",\"nova_group_tags\":[\"not-applicable\"],\"nova_group\":4,\"data_quality_tags\":[\"en:nutriscore-computations-same-score\",\"en:nutriscore-computations-same-grade\"],\"vitamins_tags\":[],\"created_t\":1432938553,\"id\":\"8888002076009\",\"nutrition_score_debug\":\" -- in beverages category - a_points_fr_beverage: 6 (energy) + 0 (sat_fat) + 8 (sugars) + 0 (sodium) = 14 -  -- energy 0 + sat-fat 0 + fr-sat-fat-for-fats 0 + sugars 2 + sodium 0 - fruits 0% 0 - fiber 0 - proteins 0 -- fsa 2 -- fr 14\",\"editors_tags\":[\"kiliweb\",\"fixbot\",\"yuka.VC9nZ01Za2VwY0pYb2ZNeS9nT081dUJmK3BLblVVenFPKzRMSWc9PQ\",\"openfoodfacts-contributors\",\"bredowmax\",\"bcd4e6\"],\"update_key\":\"nutriscore\",\"image_front_thumb_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.100.jpg\",\"ingredients_tags\":[\"en:carbonated-water\",\"en:water\",\"en:high-fructose-corn-syrup\",\"en:glucose\",\"en:fructose\",\"en:corn-syrup\",\"en:glucose-fructose-syrup\",\"en:sucrose\",\"en:sugar\",\"en:e150a\",\"en:e338\",\"en:flavouring\",\"en:caffeine\"],\"image_front_small_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/front_en.9.200.jpg\",\"purchase_places\":\"Singapore\",\"_keywords\":[\"drink\",\"classic\",\"soda\",\"cola\",\"halal\",\"soft\",\"sweetened\",\"beverage\",\"coca-cola\",\"carbonated\",\"diet\",\"coke\",\"artificially\"],\"last_modified_t\":1570451659,\"checkers_tags\":[],\"ingredients_text_with_allergens_fr\":\"\",\"informers_tags\":[\"openfoodfacts-contributors\",\"yuka.VC9nZ01Za2VwY0pYb2ZNeS9nT081dUJmK3BLblVVenFPKzRMSWc9PQ\",\"kiliweb\",\"bcd4e6\"],\"image_nutrition_small_url\":\"https://static.openfoodfacts.org/images/products/888/800/207/6009/nutrition_en.15.200.jpg\",\"code\":\"8888002076009\"}}"

    @Test
    fun successfulScanResult() {
        val scenario =
            launchFragmentInContainer<ScanFragment>(themeResId = R.style.Theme_FoodDataViewer)
        var idlingResource: TestIdlingResource? = null
        var resources: Resources? = null
        scenario.onFragment { fragment ->
            resources = fragment.resources
            idlingResource =
                ((fragment.activity!!.component as TestComponent).idlingResource() as TestIdlingResource)
            IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
            idlingResource!!.increment()
            (fragment.activity!!.component.frameProcessorOnSubscribe() as TestFrameProcessorOnSubscribe)
                .testFrame = getFrame(fragment.requireContext(), "coke.jpg")
        }

        val mockResponse = MockResponse()
        mockResponse.setBody(json)
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(response = mockResponse)
        mockWebServer.start(8500)

        onView(withId(R.id.productView)).check(matches(isDisplayed()))
        onView(withId(R.id.productNameView)).check(matches(withText("Coke Classic")))
        onView(withId(R.id.brandNameView)).check(matches(withText("Coca-Cola")))
        onView(withId(R.id.energyValueView)).check(
            matches(
                withText(
                    resources!!.getString(
                        R.string.scan_energy_value, 176

                    )
                )
            )
        )
        onView(withId(R.id.carbsValueView)).check(
            matches(
                withText(
                    resources!!.getString(
                        R.string.scan_macro_value, 10.6

                    )
                )
            )
        )
        onView(withId(R.id.fatValueView)).check(
            matches(
                withText(
                    resources!!.getString(
                        R.string.scan_macro_value, 0.0

                    )
                )
            )
        )
        onView(withId(R.id.proteinValueView)).check(
            matches(
                withText(
                    resources!!.getString(
                        R.string.scan_macro_value, 0.0

                    )
                )
            )
        )

        IdlingRegistry.getInstance().unregister(idlingResource!!.countingIdlingResource)
    }

    @Test
    fun errorScanResult() {
        val scenario =
            launchFragmentInContainer<ScanFragment>(themeResId = R.style.Theme_FoodDataViewer)
        var idlingResource: TestIdlingResource? = null
        scenario.onFragment { fragment ->
            idlingResource =
                ((fragment.activity!!.component as TestComponent).idlingResource() as TestIdlingResource)
            IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
            idlingResource!!.increment()
            (fragment.activity!!.component.frameProcessorOnSubscribe() as TestFrameProcessorOnSubscribe)
                .testFrame = getFrame(fragment.requireContext(), "coke.jpg")
        }

        val mockResponse = MockResponse()
        mockResponse.setResponseCode(400)
        mockWebServer.enqueue(response = mockResponse)
        mockWebServer.start(8500)

        onView(withId(R.id.productView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.errorView)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(idlingResource!!.countingIdlingResource)
    }
}