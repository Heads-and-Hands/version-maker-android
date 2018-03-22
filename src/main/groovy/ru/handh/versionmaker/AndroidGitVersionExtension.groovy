package ru.handh.versionmaker

import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Igor Glushkov on 01.11.17.
 */
class AndroidGitVersionExtension {

    Project project
    String buildTypeName
    String buildVariantName
    Integer versionCode
    String versionName

    AndroidGitVersionExtension(Project project) {
        this.project = project

        Gradle gradle = project.getGradle()

        String tskReqStr = gradle.getStartParameter().getTaskRequests().toString()
        println "task " + tskReqStr

        Pattern pattern

        if (tskReqStr.contains("assemble")) {
            pattern = Pattern.compile("assemble(.*?)("
                    + VersionMakerPlugin.BUILD_TYPE_BETA + "|"
                    + VersionMakerPlugin.BUILD_TYPE_DEBUG + "|"
                    + VersionMakerPlugin.BUILD_TYPE_INTERNAL + "|"
                    + VersionMakerPlugin.BUILD_TYPE_RELEASE + ")")
        } else {
            return
        }

        Matcher matcher = pattern.matcher(tskReqStr.toLowerCase())

        if (matcher.find()) {
            try {
                buildTypeName = matcher.group(2).toLowerCase()
                buildVariantName = matcher.group(0).replace("assemble", "")
                println("buildTypeName " + buildTypeName)
                println("buildVariantName " + buildVariantName)
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace()
                println "NO MATCH FOUND"
            }
        } else {
            println "NO MATCH FOUND"
        }

    }

    final String name() {
        if (versionName == null) {
            versionName = getNewVersionName(buildTypeName)
        }
        //println("versionName: " + versionName)
        return versionName
    }

    final int code() {
        if (versionCode == null) {
            versionCode = getNewVersionCode()
        }
        //println("versionCode: " + versionCode)
        return versionCode
    }

    static String getNewVersionName(String buildType) {

        String versionName

        if (buildType == null) {
            versionName = "0.0.1"
        } else if (buildType == VersionMakerPlugin.BUILD_TYPE_RELEASE) {
            versionName = getReleaseVersionNameFromReleaseBranch()
        } else if (buildType == VersionMakerPlugin.BUILD_TYPE_BETA) {
            versionName = getBetaVersionName()
        } else if (buildType == VersionMakerPlugin.BUILD_TYPE_INTERNAL) {
            versionName = getDevelopVersionName() + "-internal"
        } else {
            versionName = getDevelopVersionName() + "-debug"
        }

        return versionName
    }


    static def getNewVersionCode() {
        try {
            def versionCode = "git rev-list --count HEAD".execute().text.trim()
            def result = Integer.parseInt(versionCode)
            return result
        }
        catch (ignored) {
            println "Error getting version code " + ignored.getLocalizedMessage()
            return -1
        }
    }

    /**версия берется из тега */
    static def getReleaseVersionName() {
        try {
            def versionName = "git for-each-ref --count 1 --sort=-taggerdate --format '%(tag)' refs/tags"
                    .execute().text.trim()
            if (versionName.empty) {
                throw new Exception("Empty version name")
            }
            return versionName.replaceAll("'", "")
        }
        catch (ignored) {
            println 'Error getting release version name ' + ignored.localizedMessage
            return "0.0.0"
        }
    }

    /** версия берется из названия релиз ветки + счетчик комммитов в данной ветке*/
    static def getBetaVersionName() {
        try {

            def branch = "git rev-parse --abbrev-ref HEAD".execute().text.trim()

            //если мы находимся в релизной ветке, то можем посчитать номер беты на основе кол-ва коммитов. Если же мы
            //по каким-то причинам собираем бету не из релизной версии, то кинем ошибочный номер
            if (branch.startsWith("release")) {
                def command = "git rev-list --count " + branch + " ^develop"
                def number = command.execute().text.trim()
                def commitsNumber = Integer.parseInt(number) + 1
                def versionName = branch.toString().replaceAll("release/", "") + "-beta" + commitsNumber
                return versionName
            } else {
                return "Wrong beta version name"
            }
        }
        catch (ignored) {
            println 'Error getting beta version name ' + ignored.localizedMessage
            return "1.0.0"
        }
    }

    static def getReleaseVersionNameFromReleaseBranch() {

        try {
            def branch = "git rev-parse --abbrev-ref HEAD".execute().text.trim()
            if (branch.startsWith("release")) {
                def versionName = branch.toString().replaceAll("release/", "")
                return versionName
            } else {
                return getReleaseVersionName()
            }
        }
        catch (ignored) {
            println 'Error getting release version name from release branch' + ignored.localizedMessage
            return "1.0.0"
        }

    }

    static def getDevelopVersionName() {
        try {
            String versionName = getReleaseVersionName()
            String[] codes = versionName.split("\\.")
            codes[2] = String.valueOf(Integer.parseInt(codes[2]) + 1)
            return codes[0] + "." + codes[1] + "." + codes[2]
        } catch (ignored) {
            println "Error getting developer version name " + ignored.localizedMessage
            return "1.0.0"
        }
    }
}
