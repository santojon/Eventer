package com.santojon.eventer.test

import com.santojon.eventer.core.manager.EventManager
import com.santojon.eventer.core.stream.*

/**
 * Used to simulate Events subscription
 */
class EventStreamSimulator<T : Any> {
    private var primaryEventManager = EventManager<T>()
    private var secondaryEventManager = EventManager<T>()

    /**
     * Simulate Events list throughout [EventStream] passing
     * a CEP operator function that needs NO PARAMETERS
     */
    fun simulate(events: List<T?>?, function: SingleStreamFunction<T>): List<T?>? {
        //create the result variable
        val result = arrayListOf<T?>()

        // Apply given Function to stream, and subscribe to result
        function(primaryEventManager.asStream())?.subscribe { result.add(it!!) }

        // add all events to stream
        eventsFromEntries(events, primaryEventManager)
        return result
    }

    /**
     * Simulate Events list throughout [EventStream] passing
     * a CEP operator function that needs an [EventStream] as parameter
     */
    fun simulate(
        events1: List<T?>?,
        events2: List<T?>?,
        function: DoubleStreamFunction<T>
    ): List<T?>? {
        //create the result variable
        val result = arrayListOf<T?>()

        // Apply given Function to stream, and subscribe to result
        function(primaryEventManager.asStream(), secondaryEventManager.asStream())?.subscribe {
            result.add(it)
        }

        // add all events to stream
        // The secondary stream have to be filled first in order to be inspected from primary in simulation
        eventsFromEntries(events2, secondaryEventManager)
        eventsFromEntries(events1, primaryEventManager)

        return result
    }

    /**
     * Simulate Events list throughout [EventStream] passing
     * a CEP operator function that needs a [Comparator]
     */
    fun simulateCompare(events: List<T?>, function: CompareFunction<T>): List<T?>? {
        //create the result variable
        var result = listOf<T?>()

        // Apply given Function to stream, and subscribe to result
        function(primaryEventManager.asStream())?.subscribe { result = it!! }

        // add all events to stream
        eventsFromEntries(events, primaryEventManager)
        return result
    }

    /**
     * Simulate Events list throughout [EventStream] passing
     * a CEP operator function that needs a Grouping function
     */
    fun <R : Any?> simulate(
        events: List<T?>?,
        function: GroupingFunction<R, T>
    ): Map<R?, List<T?>>? {
        //create the result variable
        var result = mapOf<R?, List<T?>>()

        // Apply given Function to stream, and subscribe to result
        function(primaryEventManager.asStream())?.subscribe { result = it!! }

        // add all events to stream
        eventsFromEntries(events, primaryEventManager)
        return result
    }

    /**
     * Add Events to [EventManager]
     */
    private fun eventsFromEntries(entries: List<T?>?, manager: EventManager<T>?) =
        entries?.forEach { manager?.addEvent(it) }

    /**
     * Clear [EventManager]s
     */
    fun clear() {
        primaryEventManager.clear()
        secondaryEventManager.clear()
    }
}