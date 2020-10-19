package com.santojon.eventer.core.manager

import com.santojon.eventer.core.scheduler.EventSchedulers
import com.santojon.eventer.core.stream.EventStream
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Used to manage events using [EventStream]
 */
open class EventManager<T : Any>() {
    private val subscribeOn: Scheduler? = null
    private val observeOn: Scheduler? = null

    /**¬
     * Alternative constructors
     */
    constructor(subscribeOn: Scheduler? = null, observeOn: Scheduler? = null) : this()
    constructor(subscribeOn: Int?, observeOn: Int?) : this(
        EventSchedulers.from(subscribeOn),
        EventSchedulers.from(observeOn)
    )

    // Events subject
    private var evs: PublishSubject<T>? = PublishSubject.create()

    /**
     * Add event to Subject
     */
    fun addEvent(event: T?) = event?.let { evs?.onNext(event) }
    fun publish(event: T?) = addEvent(event)
    fun sendEvent(event: T?) = addEvent(event)

    /**
     * Add events to Subject
     */
    fun addEvents(vararg events: T?) = events.forEach { event -> addEvent(event) }
    fun publishMany(vararg events: T?) = addEvents(*events)
    fun sendEvents(vararg events: T?) = addEvents(*events)

    /**
     * Return stream of events
     */
    fun asStream(): EventStream<T>? = EventStream(evs, subscribeOn, observeOn)
    val events: EventStream<T>? = asStream()
    val stream: EventStream<T>? = asStream()

    /**
     * Clear the eventStream
     */
    fun clear() {
        evs = null
        evs = PublishSubject.create()
    }
}