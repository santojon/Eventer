package com.santojon.eventer.event

import com.santojon.eventer.core.event.Event
import com.santojon.eventer.core.event.ListEvent
import kotlin.reflect.KClass

/**
 * An [Event]able [ArrayList].
 *
 * @param kClass: Class of list to validate list type even if it is empty
 */
class ExtendedListEvent<T : Event>(override val kClass: KClass<T>) : ListEvent<T>(kClass) {
    /**
     * Alternative Constructor for add data from [List] as-is
     *
     * @param kClass: Class of list to validate list type even if it is empty
     * @param list: list of data to put into [ExtendedListEvent]
     */
    constructor(kClass: KClass<T>, list: List<T>?) : this(kClass) {
        list?.let { addAll(it) }
    }
}

/**
 * Extension functions to create [ExtendedListEvent]
 */
inline fun <reified T : Event> extendedListEventOf(): ExtendedListEvent<T> =
    ExtendedListEvent(T::class)

inline fun <reified T : Event> extendedListEventOf(list: List<T>): ExtendedListEvent<T> =
    ExtendedListEvent(T::class, list)