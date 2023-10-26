package pl.maniak.fooddataviewer

class AndroidTestApplication: App() {
    override val component by lazy {
        DaggerTestComponent.builder()
            .context(this)
            .build()
    }
}