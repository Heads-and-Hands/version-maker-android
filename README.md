# version-maker-android
This Gradle plugin generates versionName and versionCode based on meta information from git repository and current build settings

# Overview

The plugin supports three build types:
1. debug
2. internal
3. release

It generates version code and name by following rules:
* versionCode: number of commits
* versionName depends from buildType:
* * release - last tag in git (tag should have semantic version format) or name of current release branch
* * debug - increments minor version in the last tag and adds `-internal` suffix
* * internal - same as debug type with additional `-debug` suffix

# Installation

https://plugins.gradle.org/plugin/ru.handh.versionmaker

    defaultConfig {
        versionName versionMaker.name()
        versionCode versionMaker.code()    
    }
