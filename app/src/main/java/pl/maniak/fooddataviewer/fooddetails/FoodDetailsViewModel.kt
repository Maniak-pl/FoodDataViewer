package pl.maniak.fooddataviewer.fooddetails

import com.spotify.mobius.Next
import com.spotify.mobius.Next.*
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.maniak.fooddataviewer.MobiusVM
import pl.maniak.fooddataviewer.model.ProductRepository
import javax.inject.Inject

fun foodDetailsUpdate(
    model: FoodDetailsModel, event: FoodDetailsEvent
): Next<FoodDetailsModel, FoodDetailsEffect> {
    return when (event) {
        is Initial -> next(model.copy(activity = true), setOf(LoadProduct(event.barcode)))
        is ProductLoaded -> next(model.copy(activity = false, product = event.product))
        is ErrorLoadingProduct -> next(model.copy(activity = false, error = true))
        is ActionButtonClicked -> if (model.product != null) {
            if (model.product.saved) {
                dispatch(setOf(DeleteProduct(model.product.id)))
            } else {
                dispatch(setOf(SaveProduct(model.product.id)))
            }
        } else {
            noChange()
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
        .addTransformer(LoadProduct::class.java) { upstream ->
            upstream.switchMap { effect ->
                productRepository.loadProduct(effect.barcode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .map { product -> ProductLoaded(product) as FoodDetailsEvent }
                    .onErrorReturn { ErrorLoadingProduct }
            }
        }
        .build()
)