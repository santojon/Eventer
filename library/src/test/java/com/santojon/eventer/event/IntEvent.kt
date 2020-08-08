package com.santojon.eventer.event

import com.santojon.eventer.core.event.Event

/**
 * Class that represents Int event
 */
class IntEvent(val value: Int) : Event, Comparable<IntEvent> {
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
}