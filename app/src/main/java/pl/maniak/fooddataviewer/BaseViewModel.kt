package pl.maniak.fooddataviewer

import androidx.lifecycle.ViewModel
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.Update
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.rx2.RxEventSources
import com.spotify.mobius.rx2.RxMobius
import com.spotify.mobius.rx2.SchedulerWorkRunner
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.atomic.AtomicBoolean

interface BaseViewModel<M, E> : ObservableTransformer<E, M> {
    fun init(event: E): BaseViewModel<M, E>
}

abstract class MobiusVM<M : Any, E, F>(
    tag: String, update: Update<M, E, F>,
    initialModel: M,
    effectHandler: ObservableTransformer<F, E>,
    vararg eventSources: ObservableSource<E>
) : ViewModel(), BaseViewModel<M, E> {
    private val loop: MobiusLoop<M, E, F>
    private val initialized = AtomicBoolean(false)

    init {
        var builder = RxMobius
            .loop(update, effectHandler)
            .eventRunner { SchedulerWorkRunner(AndroidSchedulers.mainThread()) }
            .effectRunner { SchedulerWorkRunner(AndroidSchedulers.mainThread()) }
            .logger(AndroidLogger(tag))
        if (eventSources.isNotEmpty()) {
            builder = builder.eventSource(RxEventSources.fromObservables(*eventSources))
        }
        loop = builder.startFrom(initialModel)
    }

    final override fun init(event: E): BaseViewModel<M, E> {
        if (!initialized.getAndSet(true)) {
            loop.dispatchEvent(event)
        }
        return this
    }

    final override fun apply(upstream: Observable<E>): ObservableSource<M> {
        return Observable.create { emitter ->
            val eventDisposable = upstream.subscribe { event -> loop.dispatchEvent(event) }
            val modelDisposable = loop.observe { model -> emitter.onNext(model) }
            emitter.setCancellable {
                eventDisposable.dispose()
                modelDisposable.dispose()
            }
        }
    }

    final override fun onCleared() {
        super.onCleared()
        loop.dispose()
    }
}