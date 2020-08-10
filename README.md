# 💥Eventer
#### Use Rx without using Rx!    
[![Download](https://api.bintray.com/packages/santojon/Eventer/Eventer/images/download.svg)](https://bintray.com/santojon/Eventer/Eventer/_latestVersion)
[![platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15s)

### What is this?
A library made with :heart:. And Kotlin.    
A **100% Kotlin** Event Management Library **for AndroidX**.    
It's made on top of **RX-Java/Kotlin/Android**. 

**Your project don't need to use any Rx dependecy to use this library.**    

# Table of Contents
* [Download](#download)
* [Usage](#usage)
    * [Basics](#basics)
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
