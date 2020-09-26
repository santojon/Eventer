# :boom:Eventer
#### Use Rx without using Rx!    
[![Download](https://api.bintray.com/packages/santojon/Eventer/Eventer/images/download.svg)](https://bintray.com/santojon/Eventer/Eventer/_latestVersion)
[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15s)

### What is this?
A library made with :heart:. And Kotlin.    
An **100% Kotlin** Event Management Library **for AndroidX**.    
It's made on top of **RX-Java/Kotlin/Android**. 

**Your project don't need to use any Rx dependecy to use this library.**    

# Table of Contents
* [Download](#download)
* [Usage](#usage)
    * [EventManager](#eventmanager)
        * [Sending Events](#sending-events)
        * [Clearing Events](#clearing-events)
        * [Receiving Events](#receiving-events)
    * [EventStream](#eventstream)
        * [onReceive() and subscribe()](#onreceive-and-subscribe)
        * [isAs()](#isas)
        * [isAnyOf()](#isanyof)
        * [isIterableAs()](#isiterableas)
        * [isListEventOf()](#islisteventof)
* [License](#license)

### Follow me:
[![GitHub followers](https://img.shields.io/github/followers/santojon.svg?style=social&label=Follow)](https://github.com/santojon)  [![Twitter Follow](https://img.shields.io/twitter/follow/santojon.svg?style=social)](https://twitter.com/santojon) 

# Download

This library is available in **jCenter** which is the default Maven repository used in Android Studio, and in **jFrog Bintray**.    
You can also import this library from source as a module.    

Import library on **module build.gradle**:

```groovy
dependencies {
    implementation 'com.santojon:eventer:{latest_version}'
}
```

To get more recent versions and latest features, use Bintray. Add in your **project's build.gradle**:

```groovy
allprojects {
    repositories {
        ...
        maven {
            url 'https://dl.bintray.com/santojon/Eventer'
        }
    }
}
```

# Usage

## EventManager

To Use Event management you need to Create an **EventManager**:    

```kotlin
val manager: EventManager<MyBaseEventClass>? = EventManager()
```

You can specify the Schedulers for sending and subscribing to events:    

```kotlin
val manager: EventManager<MyBaseEventClass>? = EventManager(EventSchedulers.IO, EventSchedulers.MAIN_THREAD)
```

### Sending Events

**EventManager** have many functions to send events:    

```kotlin
val event = MyBaseEventClass()

manager?.addEvent(event)
manager?.publish(event)
manager?.sendEvent(event)
```

All of then works the same way, so you can use the name you're confortable with.    
You can send more than one event at a time:    

```kotlin
val event1 = MyBaseEventClass()
val event2 = MyBaseEventClass()

manager?.addEvents(event1, event2)
manager?.publishMany(event1, event2)
manager?.sendEvents(event1, event2)
```

### Clearing Events

**EventManager** can be cleared to remove all events (if **REALLY** needed):    
**It's possible, but not recomendable. You will loose all events in it, and break your app flow if some past event or condition is necessary, so, do it carefully.**    

```kotlin
manager?.clear()  // Bye bye all of my beloved events
```

### Receiving Events

The events are received through an **EventStream**. The manager can return its stream of events to be used for receiving:    

```kotlin
manager?.asStream()  // Here is the stream of [MyBaseEventClass] events
```

You can use manager properties that binds the stream:    

```kotlin
manager?.stream  // Here is the stream of [MyBaseEventClass] events
manager?.events  // Here is the stream of [MyBaseEventClass] events
```

To effectively receive events from **EventStream**, you will need to use its functions. Let's see how next.

## EventStream

### onReceive() and subscribe()

In the stream of events you can manipulate to specify your conditions.    
After get stream from manager, your can finally **receive your events**!

```kotlin
manager?.events?.onReceive { myBaseEvent ->
   // Do something with your event data
}
```

or:    

```kotlin
manager?.events?.subscribe { myBaseEvent ->
   // Do something with your event data
}
```

You can specify **onError** and/or **onComplete** actions to handle exceptional conditions:    

```kotlin
// TODO
```

The **onReceive** and/or **subscribe** functions works the same way, so, the name to use is your choice. There's no difference.    

### isAs()

Is used to filter the **EventStream** by class and return the matched events.    
Requires a class parameter. Is useful to deal with events hierarchy.    

```kotlin
val manager: EventManager<MyBaseEventClass>? = EventManager()

class MyEventClass1(val name: String?) : MyBaseEventClass()
class MyEventClass2(val id: Int) : MyBaseEventClass()

manager?.events?.isAs<MyEventClass1>()?.onReceive { myEvent1 ->
   // Here only [MyEventClass1] data will be managed
   print(myEvent1?.name)
}
```

But you can still sending events of all types in **EventManager** event hierarchy:    

```kotlin
manager?.sendEvents(MyBaseEventClass(), MyEventClass1("name"), MyEventClass2(1))
```

In addition, you can use an inline comparator function:    

```kotlin
manager?.events?.isAs<MyEventClass2> { it.id > 0 }?.onReceive { myEvent1 ->
   // Here only [MyEventClass2] data will be managed for events with id greater than 0
   print(myEvent1?.id)
}
```

### isAnyOf()

Is used to filter the **EventStream** by many classes and return the matched events.    
You pass the **KClass**es you want to receive as parameters.    
De disadvantage is the returned stream will not have events automatically casted to the right types.    
So you will have to check it manually again if needed.    

```kotlin
val manager: EventManager<MyBaseEventClass>? = EventManager()

class MyEventClass1(val name: String?) : MyBaseEventClass()
class MyEventClass2(val id: Int) : MyBaseEventClass()

manager?.events?.isAnyOf(MyEventClass1::class, MyEventClass2::class)?.onReceive { myEvent ->
   // Here only [MyEventClass1 and 2] data will be managed
   // You can check witch one is received to do something
   when(myEvent) {
      is MyEventClass1 -> print(myEvent?.name)
      is MyEventClass2 -> print(myEvent?.id)
   }
}

manager?.sendEvents(MyBaseEventClass(), MyEventClass1("name"), MyEventClass2(1))
```

### isIterableAs()

Is used to filter the **EventStream** by class and return the matched events that are instance of **Iterable**.    
Requires two class parameters. The iterable class and the class of the elements of iterable.    

```kotlin
val manager: EventManager<Event>? = EventManager()

class MyIterableClass<K : Any>() : AttayList<K>, Event

manager?.events?.isIterableAs<MyIterableClass<Int>, Int>()?.onReceive { list ->
   // Here only [MyIterableClass] data will be managed
   list?.forEach { print(it) }
}
```

**Note this will not work for empty lists!**    
**Empty lists can't have their types checked, so all empty lists of any type will be returned into this function.**    

```kotlin
manager?.evets?.isIterableAs<MyIterableClass<Int>, Int>()?.onReceive { list ->
      // Both lists will reach this point!!!
}

// All lists will be returned :(
manager?.sendEvents(listOf<Int>(), listOf<String>())
```

### isListEventOf()

Is used to filter the **EventStream** by class and return the matched events that are instance of **ListEvent**.    
Requires two class parameters. The listevent class and the class of the elements of listevent.    
**ListEvent** is a class created extending **ArrayList**, and can have the list elements type checked even if it is empty.    
So, the empty listevents of different types are distinguished by the function, solving the problem encountered in dealing with iterables.    
The **ListEvent** can be extended, so everyone can personalize is own usage of that function.    

```kotlin
val manager: EventManager<Event>? = EventManager()

manager?.events?.isListEventOf<ListEvent<Int>, Int>()?.onReceive { list ->
   // Here only [ListEvent] data will be managed
   list?.forEach { print(it) }
}
```

With that function this will work fine:    

```kotlin
manager?.evets?.isListEventOf<ListEvent<Int>, Int>()?.onReceive { list ->
      // Only listEventOf<Int>() will reach this point. Great!
}

manager?.sendEvents(listEventOf<Int>(), listEventOf<String>())
```

# About The Author

### Jonathan Santos

Design Patterns Affectionate. Full-stack Developer. Team Leadership Studier. Android Geek.    

### Connect with me:

[<img align="left" alt="santojon.com" width="22px" src="https://raw.githubusercontent.com/iconic/open-iconic/master/svg/globe.svg" />][website]
[<img align="left" alt="santojon | Twitter" width="22px" src="https://cdn.jsdelivr.net/npm/simple-icons@v3/icons/twitter.svg" />][twitter]
[<img align="left" alt="santojon | LinkedIn" width="22px" src="https://cdn.jsdelivr.net/npm/simple-icons@v3/icons/linkedin.svg" />][linkedin]
[<img align="left" alt="santojon | Instagram" width="22px" src="https://cdn.jsdelivr.net/npm/simple-icons@v3/icons/instagram.svg" />][instagram]
[<img align="left" alt="santojon | Facebook" width="22px" src="https://cdn.jsdelivr.net/npm/simple-icons@v3/icons/facebook.svg" />][facebook]

<br />

# License

```
MIT License

Copyright (c) 2020 Jonathan Santos

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

[website]: https://santojon.com
[twitter]: https://twitter.com/santojon
[facebook]: https://fb.com/santojon
[instagram]: https://instagram.com/santojon
[linkedin]: https://linkedin.com/in/santojon
