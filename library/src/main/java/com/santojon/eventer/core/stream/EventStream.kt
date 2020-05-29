package com.santojon.eventer.core.stream

import com.santojon.eventer.core.event.ComplexEvent
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import java.util.concurrent.TimeUnit

class EventStream<T : Any>(val observable: Observable<T>?) {
    /**
     * Subscribe to get stream
     */
    fun subscribe(onNext: ((T?) -> Unit)?) {
        observable?.subscribe(onNext)
    }

    /**
     * Subscribe to get stream
     */
    fun subscribe(onNext: ((T?) -> Unit)?, onError: ((Throwable?) -> Unit)?) {
        observable?.subscribe(onNext, onError)
    }

    /**
     * Subscribe to get stream
     */
    fun onReceive(onNext: ((T?) -> Unit)?) {
        subscribe(onNext)
    }

    /**
     * Subscribe to get stream
     */
    fun onReceive(onNext: ((T?) -> Unit)?, onError: ((Throwable?) -> Unit)?) {
        subscribe(onNext, onError)
    }

    /**
     * Filter and Map Events by Class
     */
    inline fun <reified R : Any> isAs(): EventStream<R>? {
        return EventStream(
            filter {
                it is R?
            }?.map {
                it as R
            }?.observable
        )
    }

    /**
     * Filter and Map Events by Class with comparator
     */
    inline fun <reified R : Any> isAs(crossinline comparator: ((R?) -> Boolean) = { _: R? -> true }): EventStream<R>? {
        return EventStream(
            filter {
                it is R?
            }?.filter {
                comparator(it as R?)
            }?.map {
                it as R
            }?.observable
        )
    }

    /**
     * Filter and Map Events by Class for Iterables
     */
    inline fun <reified R : Iterable<K>, reified K : Any> isIterableAs(): EventStream<R>? {
        return EventStream(
            filter {
                when (it) {
                    is Iterable<*> -> {
                        it.filterIsInstance<K>().isNotEmpty()
                    }
                    else -> {
                        it is R?
                    }
                }
            }?.map {
                it as R
            }?.observable
        )
    }

    /**
     * Filter emits only events from an
     * EventStream that satisfies a predicate function.
     */
    fun filter(predicate: ((T?) -> Boolean)?): EventStream<T>? {
        return EventStream(observable?.filter(predicate))
    }

    /**
     * Map transforms an EventStream by creating
     * a new EventStream through a projection function.
     */
    fun <R : Any> map(transform: ((T?) -> R)?): EventStream<R>? {
        return EventStream(observable?.map(transform))
    }

    /**
     * Sequence emits only events that follows
     * a specified order within a set of events.
     *
     * Takes a predicate function as the sequence condition
     * and the length of the sequence to be considered.
     */
    fun sequence(
        predicate: ((T?, T?) -> Boolean)?,
        count: Int?,
        skip: Int? = count
    ): EventStream<List<T>>? {
        val sequenceEquals = observable
            ?.buffer(count!!, skip!!)
            ?.filter {
                var filter = true
                if (count > 1) {
                    for (i in 1 until (it.size - 1)) {
                        if (!predicate!!.invoke(it[i - 1], it[i])) {
                            filter = false
                            break
                        }
                    }
                }
                filter
            }

        return EventStream(sequenceEquals)
    }

    /**
     * Merges two EventStreams and notifies
     * the subscriber through a ComplexEvent object when
     * both EventStreams happen within a given time frame.
     */
    fun <R : Any> merge(stream: EventStream<R>?): ComplexEvent? {
        val merged = Observable.merge(
            observable?.map { element -> Pair(element, (1 as Int?)) },
            stream?.observable?.map { element -> Pair(element, (2 as Int?)) }
        )

        return ComplexEvent(
            observable = merged,
            numberOfEvents = 2
        )
    }

    /**
     * Window only emits events that happened within a given time frame.
     */
    fun window(timespan: Long?, timeUnit: TimeUnit?): EventStream<T>? {
        return EventStream(
            observable?.buffer(
                timespan!!,
                timeUnit
            )?.flatMap { Observable.fromIterable(it) })
    }

    /**
     * Union merges two EventStreams into one EventStream
     * that emits events from both streams as they arrive.
     */
    fun union(stream: EventStream<T>?): EventStream<T>? {
        val merged = Observable.merge(
            observable,
            stream?.observable
        )?.distinct()

        return EventStream(merged)
    }

    /**
     * Filters the accumulated from all EventStreams that
     * exists in current stream but not in given one.
     */
    fun not(stream: EventStream<T>?): EventStream<T>? {
        val streamAccumulated: EventStream<MutableList<T>>? =
            EventStream(
                stream?.accumulator()?.observable?.startWith(
                    ArrayList<T>()
                )
            )

        val filtered = observable?.withLatestFrom(streamAccumulated?.observable!!)
            ?.filter { (event, accumulated) ->
                !accumulated.contains(event)
            }?.map {
                it.first
            }

        return EventStream(filtered)
    }

    /**
     * Filters the accumulated from all EventStreams
     * that exists in current stream and in given one.
     */
    fun intersect(stream: EventStream<T>?): EventStream<T>? {
        val streamAccumulated: EventStream<MutableList<T>>? =
            EventStream(
                stream?.accumulator()?.observable?.startWith(
                    arrayListOf<T>()
                )
            )

        val filtered = observable?.withLatestFrom(streamAccumulated?.observable!!)
            ?.filter { (event, accumulated) ->
                accumulated.contains(event)
            }?.map {
                it.first
            }?.distinct()

        return EventStream(filtered)
    }

    /**
     * Return a stream without duplicates.
     */
    fun distinct(): EventStream<T>? {
        return EventStream(observable?.distinct())
    }

    /**
     * Compare events by given comparator and return
     * a stream with all of then ordered by accordingly.
     */
    fun <R : Comparable<R>> orderBy(comparison: ((T?) -> R?)): EventStream<List<T>>? {
        val ordered = accumulator()?.map {
            it!!.sortedBy(comparison)
        }?.observable

        return EventStream(ordered)
    }

    /**
     * Compare events by given comparator and return
     * a stream with all of then grouped by accordingly.
     */
    fun <R> groupBy(comparison: ((T?) -> R?)): EventStream<Map<R?, List<T>>>? {
        val grouped = accumulator()?.map {
            it!!.groupBy(comparison)
        }?.observable

        return EventStream(grouped)
    }

    /**
     * Scans the observable to get an accumulation of events as a List Stream.
     */
    fun accumulator(): EventStream<MutableList<T>>? {
        val accumulator = observable?.scan(
            mutableListOf<T>(),
            { accumulated, item ->
                accumulated.add(item)
                accumulated
            }
        )

        return EventStream(accumulator)
    }
}