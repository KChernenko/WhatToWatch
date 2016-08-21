# WhatToWatch
Interesting movies in yours device.

### What does it look like:

You could install the app and check:
https://play.google.com/store/apps/details?id=me.bitfrom.whattowatch

### What does it have:

* Support Libraries
* MVP
* Dagger 2
* RxJava & RxAndroid & Gradle Retrolambda Plugin
* [SQLBrite](https://github.com/square/sqlbrite)
* [SQLDelight](https://github.com/square/sqldelight)
* OkHttp 3 & Retrofit 2 
* [LoganSquare](https://github.com/bluelinelabs/LoganSquare) + [Retrofit-LoganSquare](https://github.com/aurae/retrofit-logansquare)
* Android Universal Image Loader (Picasso cached images using OkHttp cache, Glide has awful cache
    implementation; AUIL has beautiful disk-ram cache mechanism, that helps to use less memory in
    a ReclyclerView and instantly cache images on disk during the synchronization)
* [AutoValue: Parcel Extension](https://github.com/rharter/auto-value-parcel)
* ButterKnife
* [AppIntro](https://github.com/PaoloRotolo/AppIntro)
* [FloatingActionButton](https://github.com/futuresimple/android-floating-action-button)
* [RippleEffect](https://github.com/traex/RippleEffect)

Developer libraries:
* [Timber](https://github.com/JakeWharton/timber)
* [LeakCanary](https://github.com/square/leakcanary)
* [Stetho](http://facebook.github.io/stetho/)
* [Fabric](https://fabric.io)

Code Analysis tools:
* [PMD](https://pmd.github.io/): It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth. See [this project's PMD ruleset](config/quality/pmd/pmd-ruleset.xml).
* [Findbugs](http://findbugs.sourceforge.net/): This tool uses static analysis to find bugs in Java code. Unlike PMD, it uses compiled Java bytecode instead of source code.
* [Checkstyle](http://checkstyle.sourceforge.net/): It ensures that the code style follows [our Android code guidelines](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md#2-code-guidelines). See our [checkstyle config file](config/quality/checkstyle/checkstyle-config.xml).

### What it will have:
* Unit tests (Robolectric and/or JUnit).
* Functional (UI) tests (Espresso).

### What next features will appear:
* ~~Search in the Favorite.~~
* Swipe to delete item from the list.

## License

```
    Copyright 2016 Constantine Chernenko.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```
