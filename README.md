# version-maker-android
This Gradle plugin generates versionName, versionCode and adds version badge to launcher icon for Android application 
based on meta information from git repository and current build settings

# Overview

The plugin supports four build types:
1. debug
2. internal
2. beta
4. release

It generates version code and name by following rules:
* versionCode: number of commits
* versionName depends from buildType:
* * release - last tag in git (tag should have semantic version format)
* * debug - increments minor version in the last tag and adds `-internal` suffix
* * beta - same as release type with additional `-beta` suffix
* * internal - same as debug type with additional `-debug` suffix

# Example of launcher icon

Plugin generates a launcher icon for all build types except release:

![example](images/example.jpg)

1 line - buildType + flavor name

2 line - version name

3 line - version code

# Installation

https://plugins.gradle.org/plugin/ru.handh.versionmaker

defaultConfig {
        versionName versionMaker.name()
        versionCode versionMaker.code()    
    }
