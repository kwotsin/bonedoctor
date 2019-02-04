package uk.ac.cam.cl.bravo

import uk.ac.cam.cl.bravo.classify.BodypartViewClassifier
import uk.ac.cam.cl.bravo.dataset.Bodypart
import uk.ac.cam.cl.bravo.dataset.Dataset
import uk.ac.cam.cl.bravo.dataset.Normality
import uk.ac.cam.cl.bravo.gui.DisplayImage
import uk.ac.cam.cl.bravo.overlay.AffineTransformer
import uk.ac.cam.cl.bravo.overlay.ImageOverlayImpl
import uk.ac.cam.cl.bravo.overlay.InnerWarpTransformer
import uk.ac.cam.cl.bravo.overlay.PixelSimilarity
import uk.ac.cam.cl.bravo.preprocessing.ImagePreprocessor
import uk.ac.cam.cl.bravo.util.ImageTools
import java.io.File
import javax.imageio.ImageIO


fun main(args: Array<String>) {
    tryOverlay(
        "MURA-v1.1/train/XR_HAND/patient09734/study1_positive/image1_edit.png",
        "MURA-v1.1/train/XR_HAND/patient09734/study1_positive/image3_edit.png"
    )
}

fun preprocessPipeline() {
    val dataset = Dataset()

    val imagePreprocessor: ImagePreprocessor = TODO()
    val bodypartViewClassifier: BodypartViewClassifier = TODO()

    dataset.training.forEach { sample ->
        //var image = sample.loadImage()

        // preprocessing
        var image = imagePreprocessor.preprocess(sample.path)

        // classify view
        val view = bodypartViewClassifier.classify(image, sample.bodypart)

        val newPath = sample.path.removeSuffix(".png") + "_edit.png"
        ImageIO.write(image, "png", File(newPath))
    }
}

fun loadDataset() {
    val dataset = Dataset()
    val subset = dataset.training.filter { it.normality == Normality.positive && it.bodypart == Bodypart.HAND }
}

fun tryOverlay(file1: String, file2: String) {
    println(file1)
    println(file2)

    val base = ImageIO.read(File(file1))
    val sample = ImageIO.read(File(file2))
    DisplayImage(base)
    DisplayImage(sample)

    val warper = InnerWarpTransformer(
        parameterScale = 0.8,
        parameterPenaltyScale = 1.0,
        RESOLUTION = 4
    )
    val overlay = ImageOverlayImpl(
        arrayOf(
            warper,
            AffineTransformer(parameterScale = 1.0, parameterPenaltyScale = 0.1)
        ),
        PixelSimilarity(parameterPenaltyWeight = 1.0, ignoreBorderWidth = 0.15),
        precision = 1e-4
    )

    val result = overlay.findBestOverlay(base, sample)
    val parameters = result.point
    val transformed = overlay.applyTransformations(sample, parameters)
    val overlayed = ImageTools.overlay(base, transformed)
    warper.drawMarks(overlayed, parameters)
    DisplayImage(overlayed)
}
