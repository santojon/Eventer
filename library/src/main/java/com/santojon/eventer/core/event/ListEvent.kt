package com.santojon.eventer.core.event

import kotlin.reflect.KClass

/**
 * Class used to deal with [ArrayList]s as [Event]s
 * It can validate the type even for empty lists, using [validType] inline function
 *
 * @param kClass: The [KClass] of [T] to validate for empty lists
 */
open class ListEvent<T : Any>(open val kClass: KClass<T>) : ArrayList<T>(), Event {
    /**
     * Validate type of [ListEvent]
     * Used for empty lists validation
     */
    inline fun <reified K : Any> validType(): Boolean {
        return K::class == kClass
    }

    /**
     * Alternative constructor receiving [List] of [T]
     *
     * @param list: [List] of [T] to put into [ListEvent]
     * @param kClass: empty list validation type
     */
    constructor(list: List<T>?, kClass: KClass<T>) : this(kClass) {
        list?.let { addAll(it) }
    }

    /**
     * Alternative constructor receiving [List] of [T]
     *
     * @param elements: vararg of [T] to put into [ListEvent]
     * @param kClass: empty list validation type
     */
    constructor(kClass: KClass<T>, vararg elements: T?) : this(kClass) {
        elements.forEach { element -> element?.let { add(it) } }
    }
}