package com.santojon.eventer.event

import com.santojon.eventer.core.event.Event

/**
 * Class that represents String event
 */
class StringEvent(val value: String) : Event, Comparable<StringEvent> {
    override fun compareTo(other: StringEvent): Int {
        return this.value.compareTo(other.value)
    }

    override fun equals(other: Any?): Boolean {
        if (other is StringEvent) {
            return this.value == other.value
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}