## kotlin-extensions
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/81a9a254ed8545c6b42cabb6ab28d0c7)](https://app.codacy.com/app/yuriy.kulikov.87/kotlin-extensions?utm_source=github.com&utm_medium=referral&utm_content=yuriykulikov/kotlin-extensions&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/yuriykulikov/kotlin-extensions.svg?branch=master)](https://travis-ci.org/yuriykulikov/kotlin-extensions)
[![codecov](https://codecov.io/gh/yuriykulikov/kotlin-extensions/branch/master/graph/badge.svg)](https://codecov.io/gh/yuriykulikov/kotlin-extensions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

I do quite a lot of collection/sequence processing in different languages and with different libraries like Vavr, RxJava and Kotlin. All share a lot of similarities, but once in a while some functions are missing. Great artists copy, so let's do just that!

### Sequences
#### Scan
Scan works similarly to foldLeft, but it generates a sequence instead of terminating it. Every value returned by the scanner function is emitted by the resulting sequence.

Inspired by RxJava Observable.scan http://reactivex.io/documentation/operators/scan.html