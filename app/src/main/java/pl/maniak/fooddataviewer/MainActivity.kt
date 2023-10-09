package pl.maniak.fooddataviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.maniak.fooddataviewer.utils.ActivityService

class MainActivity : AppCompatActivity() {

    private lateinit var activityService: ActivityService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityService = applicationContext.component.activityService()
        activityService.onCreate(this)
    }

    override fun onDestroy() {
        activityService.onDestroy(this)
        super.onDestroy()
    }
}