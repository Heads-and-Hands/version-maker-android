package ru.handh.versionmaker

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *  Created by Igor Glushkov on 28.04.17.
 */
class VersionMakerPlugin implements Plugin<Project> {

    final static String BUILD_TYPE_RELEASE = "release"
    final static String BUILD_TYPE_BETA = "beta"
    final static String BUILD_TYPE_INTERNAL = "internal"
    @SuppressWarnings("GroovyUnusedDeclaration")
    final static String BUILD_TYPE_DEBUG = "debug"

    @Override
    void apply(Project project) {

        project.extensions.create("versionMaker", TestExtension, project)

//        project.afterEvaluate() {
//            if (it.hasProperty("android")) {
//
//                project.android.applicationVariants.all { variant ->
//                    //generateVersionName(project, variant)
//                    //generateLauncherIcon(project, variant)
//                }
//            } else {
//                throw new IllegalStateException("'android' plugin required.")
//            }
//        }
    }

//    @Deprecated in gradle 4.2.1
//    @SuppressWarnings("GrUnresolvedAccess")
//    void generateLauncherIcon(Project project, variant) {
//
//        //не модицицируем иконку для релизных билдов
//        def buildTypeName = variant.buildType.name
//        if (buildTypeName == BUILD_TYPE_RELEASE) {
//            return
//        }
//
//        def outputDirectory = project.file("$project.buildDir/generated/res/$variant.name/launcher_icons/")
//
//        // add new resource folder to sourceSet with the highest priority
//        // this makes sure the new icons will override the original one
//        variant.sourceSets.get(variant.sourceSets.size() - 1).res.srcDirs += outputDirectory
//
//        def lines = []
//        lines.push(variant.flavorName + " " + variant.buildType.name)
//        lines.push(variant.versionName)
//        lines.push(String.valueOf(variant.versionCode))
//
//        FileCollection files = project.files()
//        variant.sourceSets.each { sourceSet ->
//            List<File> icons = new ArrayList<>()
//            for (File f : sourceSet.res.srcDirs) {
//                //noinspection GroovyAssignabilityCheck
//                searchIcons(/**config,*/
//                        icons, f)
//            }
//            files = files + project.files(icons)
//        }
//
//        if (files.empty) {
//            //noinspection GroovyConstantConditional
//            String source = /**config.mipmap*/
//                    true ? "mipmap" : "drawable"
//            println("WARNING: launcher file not found: ic_launcher.png in $source folders")
//            return
//        }
//
//        Task task = project.task("prepareLauncherIconsFor${variant.name.capitalize()}", type: BuildTypeLauncherIconTask) {
//            sources = files
//            outputDir = outputDirectory
//            isMipmap = true
//            launcherName = /**config.ic_launcher*/
//                    "ic_launcher.png"
//            buildType = variant.name
//            versionName = variant.versionName
//            versionCode = variant.versionCode
//        }
//
//        // register task to make it run before resource merging
//        // add dummy folder because the folder is already added to an sourceSet
//        // when using the folder defined in the argument the generated resources are at the lowest priority
//        // and will cause an conflict with the existing once
//        variant.registerResGeneratingTask(task, new File(outputDirectory, "_dummy"))
//    }

//    @Deprecated
//    static void generateVersionName(Project project, variant) {
//
//        //ИМЯ И НОМЕР ВЕРСИИ
//        def versionCode = getNewVersionCode()
//        def versionName
//
//        if (variant.buildType.name == BUILD_TYPE_RELEASE) {
//            versionName = getReleaseVersionName()
//        } else if (variant.buildType.name == BUILD_TYPE_BETA) {
//            versionName = getBetaVersionName()
//        } else if (variant.buildType.name == BUILD_TYPE_INTERNAL) {
//            versionName = getDevelopVersionName() + "-internal"
//        } else {
//            versionName = getDevelopVersionName() + "-debug"
//        }
//
//        println "new versionName " + versionName
//        println "new versioncode " + versionCode
//
//        variant.mergedFlavor.versionCode = versionCode
//        variant.mergedFlavor.versionName = versionName
//    }

//    void searchIcons(List<File> temp, File dir) {
//        if (!dir.exists()) {
//            return
//        }
//        if (dir.isFile()) {
//            boolean nameCorrect = dir.absolutePath.endsWith(/**config.ic_launcher*/
//                    "ic_launcher.png")
//            //noinspection GroovyConstantConditional
//            boolean typeCorrect = dir.absolutePath.contains(/**config.mipmap*/
//                    true ? "mipmap" : "drawable")
//            if (nameCorrect && typeCorrect) {
//                temp.add(dir)
//            }
//            return
//        }
//        List<File> files = dir.listFiles()
//        if (files == null) {
//            return
//        }
//        for (File f : files) {
//            searchIcons(/**config, */
//                    temp, f)
//        }
//    }

//    static def getNewVersionName(variant) {
//        def versionName
//
//        if (variant.buildType.name == BUILD_TYPE_RELEASE) {
//            versionName = getReleaseVersionName()
//        } else if (variant.buildType.name == BUILD_TYPE_BETA) {
//            versionName = getBetaVersionName()
//        } else if (variant.buildType.name == BUILD_TYPE_INTERNAL) {
//            versionName = getDevelopVersionName() + "-internal"
//        } else {
//            versionName = getDevelopVersionName() + "-debug"
//        }
//
//        return versionName
//    }
//
//
//    static Integer getNewVersionCode() {
//        try {
//            def versionCode = "git rev-list --count HEAD".execute().text.trim()
//            def result = Integer.parseInt(versionCode)
//            return result
//        }
//        catch (ignored) {
//            println "Error getting version code " + ignored.getLocalizedMessage()
//            return -1
//        }
//    }
//
//    /**версия берется из тега */
//    static def getReleaseVersionName() {
//        try {
//            def versionName = "git for-each-ref --count 1 --sort=-taggerdate --format '%(tag)' refs/tags"
//                    .execute().text.trim()
//            if (versionName.empty) {
//                throw new Exception("Empty version name")
//            }
//            return versionName.replaceAll("'", "")
//        }
//        catch (ignored) {
//            println 'Error getting release version name ' + ignored.localizedMessage
//            return "0.0.0"
//        }
//    }
//
//    /** версия берется из названия релиз ветки + счетчик комммитов в данной ветке*/
//    static def getBetaVersionName() {
//        try {
//
//            def branch = "git rev-parse --abbrev-ref HEAD".execute().text.trim()
//
//            //если мы находимся в релизной ветке, то можем посчитать номер беты на основе кол-ва коммитов. Если же мы
//            //по каким-то причинам собираем бету не из релизной версии, то кинем ошибочный номер
//            if (branch.startsWith("release")) {
//                def command = "git rev-list --count " + branch + " ^develop"
//                def number = command.execute().text.trim()
//                def commitsNumber = Integer.parseInt(number) + 1
//                def versionName = branch.toString().replaceAll("release/", "") + "-beta" + commitsNumber
//                return versionName
//            } else {
//                return "Wrong beta version name"
//            }
//        }
//        catch (ignored) {
//            println 'Error getting beta version name ' + ignored.localizedMessage
//            return "1.0.0"
//        }
//    }
//
//    static def getDevelopVersionName() {
//        try {
//            String versionName = getReleaseVersionName()
//            String[] codes = versionName.split("\\.")
//            codes[2] = "0"
//            codes[1] = String.valueOf(Integer.parseInt(codes[1]) + 1)
//            return codes[0] + "." + codes[1] + "." + codes[2]
//        } catch (ignored) {
//            println "Error getting developer version name " + ignored.localizedMessage
//            return "1.0.0"
//        }
//    }

}
