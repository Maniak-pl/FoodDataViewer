package pl.maniak.fooddataviewer.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("product/{barcode}.json")
    fun getProduct(@Path("barcode") barcode: String): Single<Response>
}