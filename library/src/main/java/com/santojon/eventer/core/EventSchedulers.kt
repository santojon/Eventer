package com.santojon.eventer.core

import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

object EventSchedulers {
    val io: Scheduler by lazy { Schedulers.io() }
    val computation: Scheduler by lazy { Schedulers.computation() }
    val newThread: Scheduler by lazy { Schedulers.newThread() }
    val trampoline: Scheduler by lazy { Schedulers.trampoline() }
    val single: Scheduler by lazy { Schedulers.single() }
    val mainThread: Scheduler by lazy { AndroidSchedulers.mainThread() }

    fun from(executor: Executor): Scheduler = Schedulers.from(executor)
    fun from(looper: Looper): Scheduler = AndroidSchedulers.from(looper)
    fun from(looper: Looper, async: Boolean): Scheduler =
        AndroidSchedulers.from(looper, async)
}