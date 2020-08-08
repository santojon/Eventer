package com.santojon.eventer.test

import com.santojon.eventer.core.stream.EventStream

typealias GroupingFunction<R, T> = (stream: EventStream<T>?) -> EventStream<Map<R?, List<T>>>?
typealias CompareFunction<T> = (stream: EventStream<T>?) -> EventStream<List<T>>?
typealias DoubleStreamFunction<T> = (stream1: EventStream<T>?, stream2: EventStream<T>?) -> EventStream<T>?
typealias SingleStreamFunction<T> = (stream: EventStream<T>?) -> EventStream<T>?
typealias SingleStreamTransformFunction<T, K> = (stream: EventStream<T>?) -> EventStream<K>?