package com.santojon.eventer.core.manager

import com.santojon.eventer.core.event.Event
import com.santojon.eventer.core.stream.EventStream
import io.reactivex.subjects.PublishSubject

class EventManager<T : Event?> {
    // Events subject
    private val evs = PublishSubject.create<T?>()

    // Return stream of events
    val events: EventStream<T?>? = asStream()

    // Return stream of events
    val stream: EventStream<T?>? = asStream()

    /**
     * Add event to Subject
     */
    fun addEvent(event: T?) {
        evs.onNext(event!!)
    }

    /**
     * Add event to Subject
     */
    fun publish(event: T?) {
        addEvent(event)
    }

    /**
     * Add event to Subject
     */
    fun sendEvent(event: T?) {
        addEvent(event)
    }

    /**
     * Return stream of events
     */
    fun asStream(): EventStream<T?>? {
        return EventStream(evs)
    }
}

