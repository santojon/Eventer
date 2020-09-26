package com.santojon.eventer.core.event

import com.santojon.eventer.core.stream.EventStream
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

open class ComplexEvent(
    val observable: Observable<Pair<Any, Int?>>?,
    val numberOfEvents: Int?,
    val timespan: Long? = 5,
    val timeUnit: TimeUnit? = TimeUnit.SECONDS
) {
    fun subscribe(onComplete: () -> Unit) {
        observable?.buffer(timespan!!, timeUnit, numberOfEvents!!)
            ?.subscribe { bundle ->
                val events = bundle.listIterator()
                val values = mutableSetOf<Int?>()
                for (item in events) values.add(item?.second)
                if (values.count() == numberOfEvents) onComplete()
            }
    }

    fun <E : Any> merge(eventStream: EventStream<E>?): ComplexEvent? {
        val merged = Observable.merge(
            observable,
            eventStream?.observable?.map { element ->
                Pair(element, numberOfEvents?.plus(1))
            }
        )
        return ComplexEvent(merged, numberOfEvents?.plus(1))
    }
}