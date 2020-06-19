package com.santojon.eventer

import com.santojon.eventer.core.event.Event
import com.santojon.eventer.core.manager.EventManager
import com.santojon.eventer.core.scheduler.EventSchedulers
import com.santojon.eventer.core.stream.EventStream
import com.santojon.eventer.test.EventStreamSimulator
import org.junit.Test

/**
 * Tests of CEP operators related to Flow Management
 */
class FlowOperatorsTest {
    /**
     *
     * Helpers and Utils
     *
     */

    /**
     * Simulator instance
     */
    private val simulator = EventStreamSimulator<IntEvent>()

    /**
     * Class that represents Int event
     */
    private class IntEvent(val value: Int) : Event, Comparable<IntEvent> {
        override fun compareTo(other: IntEvent): Int {
            return this.value.compareTo(other.value)
        }

        override fun equals(other: Any?): Boolean {
            if (other is IntEvent) {
                return this.value == other.value
            }
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }

        override fun toString(): String {
            return value.toString()
        }

        companion object
    }

    /**
     *
     * Tests
     *
     */

    /**
     * Verify return distinct Events from source
     */
    @Test
    fun distinctEvents() {
        simulator.clear()

        val events = listOf(
            IntEvent(10),
            IntEvent(10),
            IntEvent(5),
            IntEvent(12),
            IntEvent(12)
        )

        val expected = listOf(
            IntEvent(10),
            IntEvent(5),
            IntEvent(12)
        )

        val manager: EventManager<Any> = EventManager(EventSchedulers.IO, EventSchedulers.IO)
        manager.events?.isAnyOf(IntEvent::class, String::class, Long::class)?.onReceive({ event ->
            when (event) {
                is Double -> {
                    println(event::class.simpleName + event.toString())
                }
                is Long -> {
                    println(event::class.simpleName + event.toString())
                }
                is IntEvent -> {
                    println(event::class.simpleName + event.toString())
                }
            }
        }, {
            println(it?.message)
        })

        manager.sendEvents(IntEvent(1), 1, "qwe", 1.0)

        manager.sendEvent(1L)


        val output = simulator.simulate(events, ::distinctFunction)
        assert(expected == output)
    }

    /**
     * Verify two sources to give only Events existent only in first one
     */
    @Test
    fun exceptEvents() {
        simulator.clear()

        val events1 = listOf(
            IntEvent(10),
            IntEvent(10),
            IntEvent(5),
            IntEvent(12),
            IntEvent(12)
        )

        val events2 = listOf(
            IntEvent(10),
            IntEvent(10),
            IntEvent(12),
            IntEvent(12)
        )

        val expected = listOf(
            IntEvent(5)
        )

        val output = simulator.simulate(events1, events2, ::exceptFunction)
        assert(expected == output)
    }

    /**
     * Verify two sources to give Events existent in both with no duplicates
     */
    @Test
    fun intersectEvents() {
        simulator.clear()

        val events1 = listOf(
            IntEvent(10),
            IntEvent(10),
            IntEvent(5),
            IntEvent(12),
            IntEvent(12)
        )

        val events2 = listOf(
            IntEvent(10),
            IntEvent(10),
            IntEvent(12),
            IntEvent(12)
        )

        val expected = listOf(
            IntEvent(10),
            IntEvent(12)
        )

        val output = simulator.simulate(events1, events2, ::intersectFunction)
        assert(expected == output)
    }

    /**
     * Order Events in source by comparison
     */
    @Test
    fun orderEvents() {
        simulator.clear()

        val events = listOf(
            IntEvent(10),
            IntEvent(11),
            IntEvent(5),
            IntEvent(1),
            IntEvent(12)
        )

        val expected = listOf(
            IntEvent(1),
            IntEvent(5),
            IntEvent(10),
            IntEvent(11),
            IntEvent(12)
        )

        val output = simulator.simulateCompare(events, ::orderByFunction)
        assert(expected == output)
    }

    /**
     * Order Events in source by comparison
     */
    @Test
    fun groupEvents() {
        simulator.clear()

        val events = listOf(
            IntEvent(10),
            IntEvent(10),
            IntEvent(5),
            IntEvent(12),
            IntEvent(12)
        )

        val expected = mapOf(
            Pair(
                10, listOf(
                    IntEvent(10),
                    IntEvent(10)
                )
            ),
            Pair(
                5, listOf(
                    IntEvent(5)
                )
            ),
            Pair(
                12, listOf(
                    IntEvent(12),
                    IntEvent(12)
                )
            )
        )

        val output = simulator.simulate(events, ::groupByFunction)
        assert(expected == output)
    }


    /**
     *
     * CEP using functions
     *
     */

    /**
     * Filter the distinct Events
     */
    private fun distinctFunction(stream: EventStream<IntEvent>?): EventStream<IntEvent>? {
        return stream?.distinct()
    }

    /**
     * Executes Except operator
     */
    private fun exceptFunction(
        stream1: EventStream<IntEvent>?,
        stream2: EventStream<IntEvent>?
    ): EventStream<IntEvent>? {
        return stream1?.not(stream2)
    }

    /**
     * Executes Intersect operator
     */
    private fun intersectFunction(
        stream1: EventStream<IntEvent>?,
        stream2: EventStream<IntEvent>?
    ): EventStream<IntEvent>? {
        return stream1?.intersect(stream2)
    }

    /**
     * Executes OrderBy operator
     */
    private fun orderByFunction(stream: EventStream<IntEvent>?): EventStream<List<IntEvent>>? {
        return stream?.orderBy { event ->
            event?.value
        }
    }

    /**
     * Executes GroupBy operator
     */
    private fun groupByFunction(stream: EventStream<IntEvent>?): EventStream<Map<Int?, List<IntEvent>>>? {
        return stream?.groupBy { event ->
            event?.value
        }
    }
}
