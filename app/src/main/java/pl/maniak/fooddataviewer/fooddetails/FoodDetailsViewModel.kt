package pl.maniak.fooddataviewer.fooddetails

import com.spotify.mobius.Next
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import pl.maniak.fooddataviewer.MobiusVM
import pl.maniak.fooddataviewer.model.ProductRepository
import javax.inject.Inject

fun foodDetailsUpdate(
    model: FoodDetailsModel,
    event: FoodDetailsEvent
): Next<FoodDetailsModel, FoodDetailsEffect> {
    return when (event) {
        is Initial -> {
            next(
                model.copy(activity = true),
                setOf(LoadProduct(event.barcode))
            )
        }
        else -> {
            next(model.copy(activity = false))
        }
    }
}


class FoodDetailsViewModel @Inject constructor(
    productRepository: ProductRepository
) : MobiusVM<FoodDetailsModel, FoodDetailsEvent, FoodDetailsEffect>(
    "FoodDetailsViewModel",
    Update(::foodDetailsUpdate),
    FoodDetailsModel(),
    RxMobius.subtypeEffectHandler<FoodDetailsEffect, FoodDetailsEvent>()
        .build()
)