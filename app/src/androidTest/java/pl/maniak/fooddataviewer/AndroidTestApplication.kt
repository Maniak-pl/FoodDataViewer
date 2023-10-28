package pl.maniak.fooddataviewer

import pl.maniak.fooddataviewer.di.DaggerTestComponent

class AndroidTestApplication: App() {
    override val component by lazy {
        DaggerTestComponent.builder()
            .context(this)
            .build()
    }
}