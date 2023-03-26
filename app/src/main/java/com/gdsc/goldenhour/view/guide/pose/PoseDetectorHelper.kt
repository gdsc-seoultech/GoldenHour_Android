package com.gdsc.goldenhour.view.guide.pose

import android.app.Activity
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.gdsc.goldenhour.databinding.FragmentPoseDetectorBinding
import com.gdsc.goldenhour.view.guide.pose.model.TargetPose
import com.gdsc.goldenhour.view.guide.pose.model.TargetShape
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions

class PoseDetectorHelper(
    private val activity: Activity?,
    private val binding: FragmentPoseDetectorBinding
) {
    private val options by lazy {
        PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
    }

    private val poseDetector by lazy {
        PoseDetection.getClient(options)
    }

    inner class UpperBodyAnalyzer : ImageAnalysis.Analyzer {
        @androidx.camera.core.ExperimentalGetImage
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val inputImage =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                // Pass image to an ML Kit Vision API
                poseDetector.process(inputImage)
                    .addOnSuccessListener { pose ->
                        onUpperBodyDetected(pose, inputImage)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to process inputImage")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                        mediaImage.close()
                    }
            }
        }
    }

    inner class ArmPoseAnalyzer : ImageAnalysis.Analyzer {
        @androidx.camera.core.ExperimentalGetImage
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val inputImage =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                // Pass image to an ML Kit Vision API
                poseDetector.process(inputImage)
                    .addOnSuccessListener { pose ->
                        onArmPoseDetected(pose, inputImage)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to process inputImage")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                        mediaImage.close()
                    }
            }
        }
    }

    private val onUpperBodyDetected: (pose: Pose, inputImage: InputImage) -> Unit =
        { pose, inputImage ->

            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)

            val upperBodyLandmarks = listOf(leftShoulder, rightShoulder, leftHip, rightHip)
            drawUpperBodyFrame(
                upperBodyLandmarks,
                inputImage.height,
                inputImage.width
            )
        }

    private val onArmPoseDetected: (pose: Pose, inputImage: InputImage) -> Unit =
        { pose, inputImage ->
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

            val armLandmarks = listOf(
                leftShoulder,
                leftElbow,
                leftWrist,
                rightShoulder,
                rightElbow,
                rightWrist
            )

            drawArmFrame(armLandmarks, inputImage.height, inputImage.width)
            checkTwoArmAngle(pose)
        }

    private fun drawUpperBodyFrame(
        upperBodyLandmarks: List<PoseLandmark?>,
        imageHeight: Int,
        imageWidth: Int
    ) {
        if (landmarkNotFound(upperBodyLandmarks)) {
            Log.e(TAG, "4개의 꼭짓점이 모두 감지되지 않았습니다.")
            return
        }

        activity?.runOnUiThread {
            // Pass necessary information to OverlayView for drawing on the canvas
            binding.overlay.setUpperBodyLandmarks(
                upperBodyLandmarks,
                imageHeight,
                imageWidth
            )

            // Force a redraw
            binding.overlay.invalidate()
        }
    }

    private fun drawArmFrame(armLandmarks: List<PoseLandmark?>, imageHeight: Int, imageWidth: Int) {
        if (landmarkNotFound(armLandmarks)) {
            Log.e(TAG, "양쪽 팔이 모두 감지되지 않았습니다.")
            return
        }

        activity?.runOnUiThread {
            binding.overlay.setArmLandmarks(
                armLandmarks,
                imageHeight,
                imageWidth
            )
            binding.overlay.invalidate()
        }
    }

    private fun checkTwoArmAngle(pose: Pose) {
        val leftArmTargetPose = TargetPose(
            listOf(
                TargetShape(
                    PoseLandmark.LEFT_SHOULDER,
                    PoseLandmark.LEFT_ELBOW,
                    PoseLandmark.LEFT_WRIST,
                    MIN_ARM_ANGLE
                )
            )
        )
        val rightArmTargetPose = TargetPose(
            listOf(
                TargetShape(
                    PoseLandmark.RIGHT_SHOULDER,
                    PoseLandmark.RIGHT_ELBOW,
                    PoseLandmark.RIGHT_WRIST,
                    MIN_ARM_ANGLE
                )
            )
        )

        val poseMatcher = PoseMatcher()
        val isLeftArmStraighted = poseMatcher.match(pose, leftArmTargetPose)
        val isRightArmStraighted = poseMatcher.match(pose, rightArmTargetPose)
        Log.e(TAG, "left: ${isLeftArmStraighted}, right:  ${isRightArmStraighted}")

        // 양쪽 팔 중에 하나라도 140도 보다 작아지면 경고음 울리기
        if(!isLeftArmStraighted || !isRightArmStraighted){
            val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 70)
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 200)
        }
    }

    private fun landmarkNotFound(landmarks: List<PoseLandmark?>): Boolean {
        for (landmark in landmarks) {
            if (landmark == null) return true
        }
        return false
    }

    companion object {
        private const val TAG = "PoseDetection"
        private const val MIN_ARM_ANGLE = 140.0
    }
}
