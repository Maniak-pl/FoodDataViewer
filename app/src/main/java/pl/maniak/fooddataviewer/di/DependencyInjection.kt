package pl.maniak.fooddataviewer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Component
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import pl.maniak.fooddataviewer.foodlist.FoodListViewModel
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun viewModelFactory(): ViewModelProvider.Factory
}

@Module
object ApplicationModule {

    @Provides
    @Singleton
    @JvmStatic
    fun viewModels(providers: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory {
        return ViewModelFactory(providers)
    }
}

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FoodListViewModel::class)
    abstract fun foodListViewModel(viewModel: FoodListViewModel): ViewModel
}