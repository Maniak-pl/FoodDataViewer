package pl.maniak.fooddataviewer.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import pl.maniak.fooddataviewer.model.dto.ProductDto

@Dao
abstract class ProductDao {

    @Query("SELECT * FROM ProductDto WHERE id = :barcode")
    abstract fun getProduct(barcode: String): Single<ProductDto>

    @Insert
    abstract fun insert(productDto: ProductDto): Completable

    @Query("DELETE FROM ProductDto WHERE id = :barcode")
    abstract fun delete(barcode: String): Completable

}