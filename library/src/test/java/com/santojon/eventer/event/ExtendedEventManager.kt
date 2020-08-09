package com.santojon.eventer.event

import com.santojon.eventer.core.manager.EventManager
import io.reactivex.Scheduler

class ExtendedEventManager<T : Any>(
    override val subscribeOn: Scheduler? = null,
    override val observeOn: Scheduler? = null
) : EventManager<T>(subscribeOn, observeOn)