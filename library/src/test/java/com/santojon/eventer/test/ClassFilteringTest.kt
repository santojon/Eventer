package com.santojon.eventer.test

import com.santojon.eventer.core.event.Event
import com.santojon.eventer.core.event.ListEvent
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
     * Verify return distinct [StringEvent] class from source
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

        assert(expected == simulator.simulate(events, ::isAsStringEvent))
    }

    /**
     * Verify return distinct [IntEvent] class from source with comparator
     */
    @Test
    fun isAsTestWithComparator() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            IntEvent(10),
            StringEvent("5"),
            StringEvent("12"),
            IntEvent(12)
        )

        val expected = listOf(IntEvent(12))

        assert(expected == simulator.simulate(events, ::isAsIntEventCompare))
    }

    /**
     * Verify return distinct [ListEvent]s of [StringEvent] class from source
     */
    @Test
    fun isIterableAsTest() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            IntEvent(10),
            StringEvent("5"),
            StringEvent("12"),
            IntEvent(12),
            ListEvent(
                listOf(StringEvent("L1"), StringEvent("L2")),
                StringEvent::class
            )
        )

        val expected = listOf(
            ListEvent(
                listOf(StringEvent("L1"), StringEvent("L2")),
                StringEvent::class
            )
        )

        assert(expected == simulator.simulate(events, ::isIterableAsStringEventList))
    }

    /**
     * Verify return distinct [ListEvent]s of [StringEvent] class from source
     * ensure failing when sending empty list
     */
    @Test
    fun isIterableAsEmptyListTest() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            IntEvent(10),
            StringEvent("5"),
            StringEvent("12"),
            IntEvent(12),
            ListEvent(StringEvent::class)
        )

        // Ensure list of received events is empty (the list isn't received)
        val expected = listOf<ListEvent<StringEvent>?>()

        assert(expected == simulator.simulate(events, ::isIterableAsStringEventList))
    }

    /**
     * Verify return distinct [ListEvent]s of [StringEvent] class from source
     * using function that validates empty lists element items type
     */
    @Test
    fun isListEventOfTest() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            IntEvent(10),
            StringEvent("5"),
            StringEvent("12"),
            IntEvent(12),
            ListEvent(StringEvent::class)
        )

        val expected = listOf(ListEvent(StringEvent::class))

        assert(expected == simulator.simulate(events, ::isListEventOfStringEventList))
    }

    /**
     * Verify return distinct [Event]s of given classes from source
     */
    @Test
    fun isAnyOfTest() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            IntEvent(10),
            StringEvent("5"),
            StringEvent("12"),
            IntEvent(12),
            ListEvent(StringEvent::class)
        )

        val expected = listOf(
            IntEvent(10),
            IntEvent(12),
            ListEvent(StringEvent::class)
        )

        assert(expected == simulator.simulate(events, ::isAnyOf))
    }

    /**
     * Verify not returning distinct [Event]s of given classes from source
     */
    @Test
    fun isNotAnyOfTest() {
        simulator.clear()

        val events = listOf<Event?>(
            StringEvent("10"),
            StringEvent("5"),
            StringEvent("12")
        )

        val expected = listOf<Event?>()

        assert(expected == simulator.simulate(events, ::isAnyOf))
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

    /**
     * Filter the stream for [IntEvent] Class for values higher than 10
     */
    private fun isAsIntEventCompare(stream: EventStream<Event>?): EventStream<IntEvent>? =
        stream?.isAs { it?.value!! > 10 }

    /**
     * Filter the stream for [ListEvent] of [StringEvent] Class
     */
    private fun isIterableAsStringEventList(stream: EventStream<Event>?): EventStream<ListEvent<StringEvent>>? =
        stream?.isIterableAs()

    /**
     * Filter the stream for [ListEvent] of [StringEvent] Class
     * using function that validates empty lists element items type
     */
    private fun isListEventOfStringEventList(stream: EventStream<Event>?): EventStream<ListEvent<StringEvent>>? =
        stream?.isListEventOf()

    /**
     * Filter the stream for given classes
     */
    private fun isAnyOf(stream: EventStream<Event>?): EventStream<Event>? =
        stream?.isAnyOf(IntEvent::class, ListEvent::class)
}
