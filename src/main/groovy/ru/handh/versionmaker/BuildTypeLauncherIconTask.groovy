package ru.handh.versionmaker

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON

/**
 *  Created by Igor Glushkov on 11.05.17.
 */
class BuildTypeLauncherIconTask extends DefaultTask {

    private static Class clazz

    @InputFiles
    FileCollection sources

    @Input
    String launcherName

    @SuppressWarnings("GroovyUnusedDeclaration")
    @Input
    boolean isMipmap

    @Input
    String buildType

    @Input
    String versionName

    @Input
    String versionCode

    /**
     * The output directory.
     */
    @OutputDirectory
    File outputDir

    @SuppressWarnings("GroovyUnusedDeclaration")
    @TaskAction
    def generateDrawables(IncrementalTaskInputs inputs) {

        println("generateDrawables")

        clazz = getClass()

        for (File it : sources) {
            def parent = it.toPath().parent.toFile()

            def outputParentDir = new File(outputDir, parent.name)

            def output = new File(outputParentDir, launcherName)

            try {
                outputParentDir.mkdirs()
                output.createNewFile()

                def lines = []
                lines.push(buildType)
                lines.push(versionName)
                lines.push(versionCode)

                addTextToImage(it, output, IconVersionConfig.DEFAULT, *lines)

            } catch (Exception e) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Draws the given text over an inputImage
     *
     * @param inputImage The inputImage file which will be written too
     * @param config The configuration which controls how the overlay will appear
     * @param lines The lines of text to be displayed
     */
    static void addTextToImage(File inputImage,
                               File outputPath,
                               IconVersionConfig config,
                               String... lines) {

        final BufferedImage bufferedImage = ImageIO.read(inputImage)

        final Color backgroundOverlayColor = config.getBackgroundOverlayColor()
        final Color textColor = config.getTextColor()
        int fontSize = config.fontSize
        final int linePadding = config.verticalLinePadding
        final int imgWidth = bufferedImage.width
        final int imgHeight = bufferedImage.width

        fontSize = imgWidth / 8

        final int lineCount = lines.length
        final int totalLineHeight = (fontSize * lineCount) + ((linePadding + 1) * lineCount)

        GraphicsEnvironment.localGraphicsEnvironment.createGraphics(bufferedImage).with { g ->
            g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON)

            // Draw our background overlay
            g.setColor(backgroundOverlayColor)
            g.fillRect(0, imgHeight - totalLineHeight, imgWidth, totalLineHeight)

            // Draw each line of our text
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, fontSize))
            g.setColor(textColor)
            lines.reverse().eachWithIndex { String line, int i ->

                final int strWidth = g.getFontMetrics().stringWidth(line)

                int x = 0
                if (imgWidth >= strWidth) {
                    x = ((imgWidth - strWidth) / 2)
                }

                int y = imgHeight - (fontSize * i) - ((i + 1) * linePadding)

                g.drawString(line, x, y)
            }
        }

        ImageIO.write(bufferedImage, "png", outputPath)
    }

}
