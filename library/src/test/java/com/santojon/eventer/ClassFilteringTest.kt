package com.santojon.eventer

import com.santojon.eventer.core.event.Event
import com.santojon.eventer.core.stream.EventStream
import com.santojon.eventer.test.EventStreamSimulator
import org.junit.Test

/**
 * Tests of Class filtering functions
 */
class ClassFilteringTest {
    /**
     *
     * Helpers and Utils
     *
     */

    /**
     * Simulator instance
     */
    private val simulator = EventStreamSimulator<Event>()

    /**
     * Classes that represents events
     */
    private class A(val name: String) : Event {
        override fun equals(other: Any?): Boolean {
            if (other is A) {
                return this.name == other.name
            }
            return super.equals(other)
        }
    }

    private class B(val id: Int) : Event {
        override fun equals(other: Any?): Boolean {
            if (other is B) {
                return this.id == other.id
            }
            return super.equals(other)
        }
    }

    /**
     *
     * Tests
     *
     */

    /**
     * Verify return distinct Event classes from source
     */
    @Test
    fun isAsTest() {
        simulator.clear()

        val events = listOf(
            A("10"),
            B(10),
            A("5"),
            A("12"),
            B(12)
        )

        val expected = listOf(
            A("10"),
            A("5"),
            A("12")
        )

        assert(expected == simulator.simulateTransform(events, ::isAsA))
    }

    /**
     *
     * Functions to simulate
     *
     */

    /**
     * Filter the stream for [A] Class
     */
    private fun isAsA(stream: EventStream<Event>?): EventStream<A>? = stream?.isAs()
}
