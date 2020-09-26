package com.santojon.eventer.core.scheduler

import android.os.Looper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor

/**
 * Used to provide [Scheduler]s for streams to be observed
 */
object EventSchedulers {
    // Constant values for schedulers
    const val IO: Int = 0
    const val COMPUTATION: Int = 1
    const val NEW_THREAD: Int = 2
    const val TRAMPOLINE: Int = 3
    const val SINGLE: Int = 4
    const val MAIN_THREAD: Int = 5

    // Schedulers to provide
    private val io: Scheduler by lazy { Schedulers.io() }
    private val computation: Scheduler by lazy { Schedulers.computation() }
    private val newThread: Scheduler by lazy { Schedulers.newThread() }
    private val trampoline: Scheduler by lazy { Schedulers.trampoline() }
    private val single: Scheduler by lazy { Schedulers.single() }
    private val mainThread: Scheduler by lazy { AndroidSchedulers.mainThread() }

    /**
     * Provide a [Scheduler] responding to given parameter(s)
     */
    fun from(executor: Executor) = Schedulers.from(executor)
    fun from(looper: Looper) = AndroidSchedulers.from(looper)
    fun from(looper: Looper, async: Boolean) = AndroidSchedulers.from(looper, async)
    fun from(schedulerRef: Int?): Scheduler {
        return when (schedulerRef) {
            IO -> io
            COMPUTATION -> computation
            NEW_THREAD -> newThread
            TRAMPOLINE -> trampoline
            SINGLE -> single
            MAIN_THREAD -> mainThread
            else -> io
        }
    }
}