package com.santojon.eventer.event

import com.santojon.eventer.core.stream.EventStream
import io.reactivex.Observable
import io.reactivex.Scheduler

class ExtendedEventStream<T : Any>(
    override val observable: Observable<T>?,
    override val subscribeOn: Scheduler? = null,
    override val observeOn: Scheduler? = null
) : EventStream<T>(observable, subscribeOn, observeOn)