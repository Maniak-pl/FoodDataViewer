package pl.maniak.fooddataviewer.di

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import pl.maniak.fooddataviewer.scan.TestFrameProcessorOnSubscribe
import pl.maniak.fooddataviewer.scan.utils.FrameProcessorOnSubscribe
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, ApiModule::class, DatabaseModule::class, TestModule::class])
interface TestComponent : ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun context(context: Context): Builder

        fun build(): TestComponent
    }
}

@Module
object TestModule {

    @Singleton
    @Provides
    @JvmStatic
    fun frameProcessorOnSubscribe(): FrameProcessorOnSubscribe = TestFrameProcessorOnSubscribe()
}