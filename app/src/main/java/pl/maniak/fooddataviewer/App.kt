package pl.maniak.fooddataviewer

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import pl.maniak.fooddataviewer.di.ApplicationComponent
import pl.maniak.fooddataviewer.di.DaggerRealComponent
import kotlin.reflect.KClass

open class App : Application() {

    open val component: ApplicationComponent by lazy {
        DaggerRealComponent.builder()
            .context(this)
            .build()
    }
}

val Context.component: ApplicationComponent
    get() = (this.applicationContext as App).component

fun <T, M, E> Fragment.getViewModel(type: KClass<T>):
        BaseViewModel<M, E> where T : ViewModel, T : BaseViewModel<M, E> {
    val factory = this.requireContext().component.viewModelFactory()
    return ViewModelProviders.of(this, factory)[type.java]
}