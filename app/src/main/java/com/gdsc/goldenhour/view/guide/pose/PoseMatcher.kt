package com.gdsc.goldenhour.view.guide.pose

import android.util.Log
import com.gdsc.goldenhour.view.guide.pose.model.TargetPose
import com.gdsc.goldenhour.view.guide.pose.model.TargetShape
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.abs
import kotlin.math.atan2

class PoseMatcher {
    fun match(pose: Pose, targetPose: TargetPose): Boolean {
        targetPose.targets.forEach { target ->
            val (firstLandmark, middleLandmark, lastLandmark) = extractLandmark(pose, target)

            // Check landmark is null
            if (landmarkNotFound(firstLandmark, middleLandmark, lastLandmark)) {
                return false
            }

            // Check three points are in a row.
            val angle = calculateAngle(firstLandmark!!, middleLandmark!!, lastLandmark!!)
            Log.e("PoseDetection", "세 점의 각도: ${angle}")

            val targetAngle = target.angle
            if (abs(angle - targetAngle) > offset) {
                return false
            }
        }

        return true
    }

    private fun extractLandmark(
        pose: Pose,
        target: TargetShape
    ): Triple<PoseLandmark?, PoseLandmark?, PoseLandmark?> {
        return Triple(
            extractLandmarkFromType(pose, target.firstLandmarkType),
            extractLandmarkFromType(pose, target.middleLandmarkType),
            extractLandmarkFromType(pose, target.lastLandmarkType)
        )
    }

    private fun extractLandmarkFromType(pose: Pose, landmarkType: Int): PoseLandmark? {
        return pose.getPoseLandmark(landmarkType)
    }

    private fun landmarkNotFound(
        firstLandmark: PoseLandmark?,
        middleLandmark: PoseLandmark?,
        lastLandmark: PoseLandmark?
    ): Boolean {
        return firstLandmark == null || middleLandmark == null || lastLandmark == null
    }

    private fun calculateAngle(
        firstLandmark: PoseLandmark,
        middleLandmark: PoseLandmark,
        lastLandmark: PoseLandmark
    ): Double {
        val angle = Math.toDegrees(
            (atan2(
                lastLandmark.position.y - middleLandmark.position.y,
                lastLandmark.position.x - middleLandmark.position.x
            ) - atan2(
                firstLandmark.position.y - middleLandmark.position.y,
                firstLandmark.position.x - middleLandmark.position.x
            )).toDouble()
        )

        var absoluteAngle = abs(angle)
        if (absoluteAngle > 180) {
            absoluteAngle = 360 - absoluteAngle
        }
        return absoluteAngle
    }

    companion object {
        private const val offset = 20.0
    }
}