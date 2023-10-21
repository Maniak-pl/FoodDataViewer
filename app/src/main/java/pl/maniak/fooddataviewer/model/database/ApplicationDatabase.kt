package pl.maniak.fooddataviewer.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.maniak.fooddataviewer.model.dto.ProductDto

@Database(
    entities = [ProductDto::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}