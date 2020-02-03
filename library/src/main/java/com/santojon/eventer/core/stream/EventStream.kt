package com.santojon.eventer.core.stream

import com.santojon.eventer.core.event.ComplexEvent
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class EventStream<T : Any?>(val observable: Observable<T?>?) {

    fun subscribe(onNext: ((T?) -> Unit)?) {
        observable?.subscribe(onNext)
    }

    inline fun <reified R : Any?> isAs(): EventStream<R?>? {
        return EventStream(
            filter {
                it is R?
            }?.map {
                it as R
            }?.observable
        )
    }

    /**
     * The filter operator emits only events from an
     * EventStream that satisfies a predicate function.
     */
    fun filter(predicate: ((T?) -> Boolean)?): EventStream<T?>? {
        return EventStream(observable?.filter(predicate))
    }

    /**
     * The map operator transforms an EventStream by creating
     * a new EventStream through a projection function.
     */
    fun <R : Any?> map(transform: ((T?) -> R?)?): EventStream<R?>? {
        return EventStream(observable?.map(transform))
    }

    /**
     * The sequence operator emits only events that follows
     * a specified order within a set of events. The operator
     * takes a predicate function as the sequence condition
     * and the length of the sequence to be considered.
     */
    fun sequence(
        predicate: ((T?, T?) -> Boolean)?,
        count: Int?,
        skip: Int? = count
    ): EventStream<List<T?>?>? {
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
     * The merge operator merges two EventStreams and notifies
     * the subscriber through a ComplexEvent object when
     * both EventStreams happen within a given time frame.
     */
    fun <R : Any?> merge(stream: EventStream<R?>?): ComplexEvent? {
        val merged = Observable.merge(
            observable?.map { element -> Pair(element, (1 as Int?)) },
            stream?.observable?.map { element -> Pair(element, (2 as Int?)) }
        )
        return ComplexEvent(
            observable = merged,
            numberOfEvents = 2
        )
    }

    fun buffer(timespan: Long?, timeUnit: TimeUnit?): EventStream<List<T?>?>? {
        return EventStream(observable?.buffer(timespan!!, timeUnit))
    }

    fun buffer(count: Int?, skip: Int?): EventStream<List<T?>?>? {
        return EventStream(observable?.buffer(count!!, skip!!))
    }

    /**
     * The window operator only emits events that
     * happened within a given time frame.
     */
    fun window(timespan: Long?, timeUnit: TimeUnit?): EventStream<T?>? {
        return EventStream(
            observable?.buffer(
                timespan!!,
                timeUnit
            )?.flatMap { Observable.fromIterable(it) })
    }

    /**
     * The union operator merges two EventStreams into one EventStream
     * that emits events from both streams as they arrive.
     */
    fun union(stream: EventStream<T?>?): EventStream<T?>? {
        val merged = Observable.merge(
            observable,
            stream?.observable
        )?.distinct()
        return EventStream(merged)
    }

    /**
     * Equivalent to Cugola Except operator
     *
     * <p>
     *     Filters the accumulated from all EventStreams that exists in current stream
     *     but not in given one
     * </p>
     */
//    fun not(stream: EventStream<T?>?): EventStream<T?>? {
//        val streamAccumulated =
//            EventStream(
//                stream?.accumulator()?.observable?.startWith(
//                    ArrayList<T?>()
//                )
//            )
//        val filtered = observable?.withLatestFrom(streamAccumulated.observable)
//            ?.filter { (event, accumulated) ->
//                !accumulated.contains(event)
//            }?.map {
//                it.first
//            }
//        return EventStream(filtered)
//    }

    /**
     * Equivalent to Cugola Remove-duplicates operator
     *
     * <p>
     *     This is a simple wrap of RxJava distinct
     * </p>
     */
    fun distinct(): EventStream<T?>? {
        return EventStream(observable?.distinct())
    }

    /**
     * Equivalent to Cugola Intersect operator
     *
     * <p>
     *     Filters the accumulated from all EventStreams that exists in current stream
     *     and in given one
     * </p>
     */
//    fun intersect(stream: EventStream<T?>?): EventStream<T?>? {
//        val streamAccumulated =
//            EventStream(
//                stream?.accumulator()?.observable?.startWith(
//                    ArrayList<T?>()
//                )
//            )
//        val filtered = observable?.withLatestFrom(streamAccumulated.observable)
//            ?.filter { (event, accumulated) ->
//                accumulated.contains(event)
//            }?.map {
//                it.first
//            }?.distinct()
//        return EventStream(filtered)
//    }

    /**
     * Equivalent to Cugola Order by operator
     *
     * <p>
     *     Compare events by given comparator and return a stream with all of then ordered by accordingly
     * </p>
     */
    fun <R : Comparable<R>> orderBy(comparison: ((T?) -> R?)): EventStream<List<T?>?>? {
        val ordered = accumulator()?.map {
            it?.sortedBy(comparison)
        }?.observable
        return EventStream(ordered)
    }

    /**
     * Equivalent to Cugola Group by operator
     *
     * <p>
     *     Compare events by given comparator and return a stream with all of then grouped by accordingly
     * </p>
     */
    fun <R> groupBy(comparison: ((T?) -> R?)): EventStream<Map<R?, List<T?>>>? {
        val grouped = accumulator()?.map {
            it?.groupBy(comparison)
        }?.observable
        return EventStream(grouped)
    }

    fun <R : Any?> distinct(transform: ((T?) -> R?)?): EventStream<R?>? {
        return EventStream(observable?.map(transform))
    }

    fun accumulator(): EventStream<MutableList<T?>?>? {
        val accumulator = observable?.scan(
            mutableListOf<T?>(),
            { accumulated, item ->
                accumulated.add(item)
                accumulated
            }
        )
        return EventStream(accumulator)
    }
}