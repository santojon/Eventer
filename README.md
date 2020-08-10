# 💥Eventer
#### Use Rx without using Rx!    

### What is this?
A library made with :heart:. And Kotlin.    
A **100% Kotlin** Event Management Library **for AndroidX**.    
It's made on top of **RX-Java/Kotlin/Android**. 

**Your project don't need to use any Rx dependecy to use this library.**    

# Table of Contents
* [Download](#download)
* [Usage](#usage)
    * [Basics](#basics)
    * [Gravity](#gravity)
    * [Title](#title)
    * [Message](#message)
    * [Background & Overlay](#background-overlay)
      * [Background](#background)
      * [Overlay](#overlay)
    * [Actions](#actions)
      * [Primary](#primary)
      * [Positive/Negative](#positivenegative)
    * [Icon & Progress](#icon-progress)
      * [Icon](#icon)
      * [Progress](#progress)
    * [Animations](#animations)
      * [Enter/Exit](#enterexit)
      * [Icon](#icon)
    * [Event Listeners](#event-listeners)
      * [Show](#show)
      * [Dismiss](#dismiss)
      * [Taps](#taps)
    * [Miscellaneous](#miscellaneous)
      * [Swipe-to-dismiss](#swipe-to-dismiss)
      * [Shadow](#shadow)
      * [Vibration](#vibration)
* [License](#license)

### Spread Some :heart:
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

## Basics

To Use Event management you need to Create an **EventManager**:    

```kotlin
val manager: EventManager<Any>? = EventManager()
```

 You can specify the Schedulers for sending and subscribing to events:    

```kotlin
val manager: EventManager<Any>? = EventManager(EventSchedulers.IO, EventSchedulers.MAIN_THREAD)
```

### Specs
[![Download](https://api.bintray.com/packages/santojon/Eventer/Eventer/images/download.svg)](https://bintray.com/santojon/Eventer/Eventer/_latestVersion)
[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15s)
