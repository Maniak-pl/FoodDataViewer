package pl.maniak.fooddataviewer.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import pl.maniak.fooddataviewer.scan.TestFrameProcessorOnSubscribe
import pl.maniak.fooddataviewer.scan.utils.FrameProcessorOnSubscribe
import pl.maniak.fooddataviewer.utils.IdlingResource
import pl.maniak.fooddataviewer.utils.TestIdlingResource
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, ApiModule::class, DatabaseModule::class, TestModule::class])
interface TestComponent : ApplicationComponent {

    fun idlingResource(): IdlingResource

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): TestComponent
    }
}

@Module
object TestModule {

    @Singleton
    @JvmStatic
    @Provides
    fun frameProcessorOnSubscribe(): FrameProcessorOnSubscribe = TestFrameProcessorOnSubscribe()

    @Provides
    @Singleton
    @JvmStatic
    fun idlingResource(): IdlingResource = TestIdlingResource()
}