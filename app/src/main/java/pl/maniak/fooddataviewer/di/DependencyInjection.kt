package pl.maniak.fooddataviewer.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import dagger.*
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.maniak.fooddataviewer.R
import pl.maniak.fooddataviewer.fooddetails.FoodDetailsViewModel
import pl.maniak.fooddataviewer.foodlist.FoodListViewModel
import pl.maniak.fooddataviewer.model.ProductService
import pl.maniak.fooddataviewer.model.database.ApplicationDatabase
import pl.maniak.fooddataviewer.scan.ScanViewModel
import pl.maniak.fooddataviewer.scan.utils.FrameProcessorOnSubscribe
import pl.maniak.fooddataviewer.utils.ActivityService
import pl.maniak.fooddataviewer.utils.Navigator
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@MustBeDocumented
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ApiBaseUrl


interface ApplicationComponent {

    fun viewModelFactory(): ViewModelProvider.Factory

    fun activityService(): ActivityService

    fun frameProcessorOnSubscribe(): FrameProcessorOnSubscribe
}

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, ApiModule::class, DatabaseModule::class, RealModule::class])
interface RealComponent : ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): RealComponent
    }
}

@Module
object ApplicationModule {

    @Provides
    @Singleton
    @JvmStatic
    fun viewModels(providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {
        return ViewModelFactory(providers)
    }

    @Provides
    @Singleton
    @JvmStatic
    fun activityService(): ActivityService = ActivityService()

    @Provides
    @Singleton
    @JvmStatic
    fun navigator(activityService: ActivityService): Navigator {
        return Navigator(R.id.navigationHostFragment, activityService)
    }

    @Provides
    @ApiBaseUrl
    @JvmStatic
    fun apiBaseUrl(context: Context): String = context.getString(R.string.api_base_url)
}

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FoodListViewModel::class)
    abstract fun foodListViewModel(viewModel: FoodListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanViewModel::class)
    abstract fun scanViewModel(viewModel: ScanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoodDetailsViewModel::class)
    abstract fun foodDetailsViewModel(viewModel: FoodDetailsViewModel): ViewModel
}

@Module
object ApiModule {
    @Provides
    @Singleton
    @JvmStatic
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun retrofit(@ApiBaseUrl apiBaseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun productService(retrofit: Retrofit): ProductService {
        return retrofit.create(ProductService::class.java)
    }
}

@Module
object DatabaseModule {

    @Provides
    @Singleton
    @JvmStatic
    fun applicationDatabase(context: Context): ApplicationDatabase {
        return Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            "food-database"
        ).build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun productDao(applicationDatabase: ApplicationDatabase) = applicationDatabase.productDao()
}

@Module
object RealModule {

        @Provides
        @Singleton
        @JvmStatic
        fun frameProcessorOnSubscribe(): FrameProcessorOnSubscribe = FrameProcessorOnSubscribe()
}