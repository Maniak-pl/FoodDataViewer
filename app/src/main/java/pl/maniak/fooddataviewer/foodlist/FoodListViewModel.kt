package pl.maniak.fooddataviewer.foodlist

import com.spotify.mobius.Next
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import pl.maniak.fooddataviewer.MobiusVM
import javax.inject.Inject

fun foodListUpdate(
    model: FoodListModel,
    event: FoodListEvent
): Next<FoodListModel, FoodListEffect> {
    return Next.next(FoodListModel)
}

class FoodListViewModel @Inject constructor() : MobiusVM<FoodListModel, FoodListEvent, FoodListEffect>(
    "FoodListViewModel",
    Update(::foodListUpdate),
    FoodListModel,
    RxMobius.subtypeEffectHandler<FoodListEffect, FoodListEvent>()
        .build()
)