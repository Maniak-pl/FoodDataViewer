package pl.maniak.fooddataviewer

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

@Suppress("unused")
class AndroidTestRunner: AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return Instrumentation.newApplication(AndroidTestApplication::class.java, context)
    }
}