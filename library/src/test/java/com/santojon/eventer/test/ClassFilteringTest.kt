package com.santojon.eventer.test

import com.santojon.eventer.core.event.Event
import com.santojon.eventer.core.stream.EventStream
import com.santojon.eventer.event.IntEvent
import com.santojon.eventer.event.StringEvent
import org.junit.Test

/**
 * Tests of Class filtering functions
 */
class ClassFilteringTest {
    /*****************************************************
     *
     * Helpers and Utils
     *
     *****************************************************/

    /**
     * Simulator instance
     */
    private val simulator = EventStreamSimulator<Event>()

    /*****************************************************
     *
     * Tests
     *
     *****************************************************/

    /**
     * Verify return distinct Event classes from source
     */
    @Test
    fun isAsTest() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            IntEvent(10),
            StringEvent("5"),
            StringEvent("12"),
            IntEvent(12)
        )

        val expected = listOf(
            StringEvent("10"),
            StringEvent("5"),
            StringEvent("12")
        )

        assert(expected == simulator.simulateTransform(events, ::isAsStringEvent))
    }

    /*****************************************************
     *
     * Functions to simulate
     *
     *****************************************************/

    /**
     * Filter the stream for [StringEvent] Class
     */
    private fun isAsStringEvent(stream: EventStream<Event>?): EventStream<StringEvent>? =
        stream?.isAs()
}
