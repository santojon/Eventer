package com.santojon.eventer.core.manager

import com.santojon.eventer.core.scheduler.EventSchedulers
import com.santojon.eventer.core.stream.EventStream
import io.reactivex.Scheduler
import io.reactivex.subjects.PublishSubject

class EventManager<T : Any>(
    val subscribeOn: Scheduler? = null,
    val observeOn: Scheduler? = null
) {
    constructor(subscribeOn: Int?, observeOn: Int?) : this(
        EventSchedulers.from(subscribeOn),
        EventSchedulers.from(observeOn)
    )

    // Events subject
    private var evs: PublishSubject<T>? = PublishSubject.create()

    // Return stream of events
    val events: EventStream<T>? = asStream()

    // Return stream of events
    val stream: EventStream<T>? = asStream()

    /**
     * Add event to Subject
     */
    fun addEvent(event: T?) {
        event?.let { evs?.onNext(event) }
    }

    fun publish(event: T?) = addEvent(event)
    fun sendEvent(event: T?) = addEvent(event)

    /**
     * Add events to Subject
     */
    fun addEvents(vararg events: T?) {
        events.forEach { event ->
            addEvent(event)
        }
    }

    fun publishMany(vararg events: T?) = addEvents(*events)
    fun sendEvents(vararg events: T?) = addEvents(*events)

    /**
     * Return stream of events
     */
    fun asStream(): EventStream<T>? {
        return EventStream(evs, subscribeOn, observeOn)
    }

    fun clear() {
        evs = null
        evs = PublishSubject.create()
    }
}