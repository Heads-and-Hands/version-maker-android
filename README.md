# version-maker-android
Gradle plugin that generates version name, code and launcher icon labeles for android application

# Version name format:

x.y.z

# Overview

Plugin supports four build types:
1. debug
2. intenral
2. beta
4. release

It generates version code and name by rules below:
* versionCode - number of commits
* versionName by buildType:
* * release - last tag in git (tag should be like x.y.z)
* * debug - last tag in git and modifies it: (x.y+1.0) + __-internal__
* * beta - 'release' branch name + __-beta__
* * internal - debug verion name + __-debug__

# Example of launcher icon

Add three lines to all build types, except __release__:

1 line - buildType + flavor name

2 line - version name

3 line - version code

![example](images/example.jpg)

# Installation

https://plugins.gradle.org/plugin/ru.handh.versionmaker
