package pl.maniak.fooddataviewer

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import pl.maniak.fooddataviewer.di.DaggerTestComponent
import pl.maniak.fooddataviewer.model.database.ProductDao
import pl.maniak.fooddataviewer.model.dto.ProductDto

class AndroidTestApplication : App() {

    val productDaoSubject = PublishSubject.create<List<ProductDto>>()
    private val productDao: ProductDao = object : ProductDao() {
        override fun getProducts(): Observable<List<ProductDto>> {
            return productDaoSubject
        }

        override fun getProduct(barcode: String): Single<ProductDto> {
            throw NotImplementedError("Not implemented in instrumented testing")
        }

        override fun insert(productDto: ProductDto): Completable {
            throw NotImplementedError("Not implemented in instrumented testing")
        }

        override fun delete(barcode: String): Completable {
            throw NotImplementedError("Not implemented in instrumented testing")
        }

    }

    override val component by lazy {
        DaggerTestComponent.builder()
            .context(this)
            .productDao(productDao)
            .build()
    }
}