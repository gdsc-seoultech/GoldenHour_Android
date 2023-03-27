package com.gdsc.goldenhour.view.guide.pose

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.mlkit.vision.pose.PoseLandmark
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var upperBodyLandmarks: List<PoseLandmark> = LinkedList<PoseLandmark>()
    private var pressureCoord: Pair<Float, Float> = Pair(0f, 0f)
    private var upperBodyCoords = MutableList(UPPER_BODY_LANDMARKS) { Pair(0f, 0f) }

    private var armLandmarks: List<PoseLandmark> = LinkedList<PoseLandmark>()
    private var armCoords = MutableList(ARM_LANDMARKS) { Pair(0f, 0f) }

    private val markerPaint = Paint()
    private val pressurePaint = Paint()
    private val linePaint = Paint()

    private var closedUpperBodyDetector = false

    init {
        initPaint()
    }

    private fun initPaint() {
        markerPaint.color = Color.WHITE
        markerPaint.style = Paint.Style.FILL
        markerPaint.strokeWidth = STROKE_WIDTH

        pressurePaint.color = Color.RED
        pressurePaint.style = Paint.Style.FILL
        pressurePaint.strokeWidth = STROKE_WIDTH

        linePaint.color = Color.WHITE
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 5f
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (!closedUpperBodyDetector) {
            drawUpperBodyFrame(canvas)
        } else {
            drawArmFrame(canvas)
        }
    }

    private fun drawArmFrame(canvas: Canvas) {
        for (coord in armCoords) {
            canvas.drawCircle(coord.first, coord.second, DOT_RADIUS, markerPaint)
        }

        // 왼쪽 팔
        canvas.drawConnectLine(armCoords, 0, 1)
        canvas.drawConnectLine(armCoords, 1, 2)

        // 오른쪽 팔
        canvas.drawConnectLine(armCoords, 3, 4)
        canvas.drawConnectLine(armCoords, 4, 5)
    }

    private fun drawUpperBodyFrame(canvas: Canvas) {
        for (coord in upperBodyCoords) {
            canvas.drawCircle(coord.first, coord.second, DOT_RADIUS, markerPaint)
        }

        canvas.drawCircle(
            pressureCoord.first,
            pressureCoord.second,
            DOT_RADIUS,
            pressurePaint
        )

        canvas.drawConnectLine(upperBodyCoords, 0, 1)
        canvas.drawConnectLine(upperBodyCoords, 0, 2)
        canvas.drawConnectLine(upperBodyCoords, 1, 3)
        canvas.drawConnectLine(upperBodyCoords, 2, 3)
    }

    private fun Canvas.drawConnectLine(coords: MutableList<Pair<Float, Float>>, a: Int, b: Int) {
        drawLine(
            coords[a].first,
            coords[a].second,
            coords[b].first,
            coords[b].second,
            linePaint
        )
    }

    fun setUpperBodyLandmarks(
        landmarks: List<PoseLandmark?>,
        imageHeight: Int,
        imageWidth: Int
    ) {
        Log.d("PoseDetection", "[input image size] ${imageWidth} ${imageHeight}")
        Log.d("PoseDetection", "[view size] ${width} ${height}")

        upperBodyLandmarks = landmarks as List<PoseLandmark>

        for (i in upperBodyLandmarks.indices) {
            val posX = upperBodyLandmarks[i].position.x * SCALE_FACTOR
            val posY = upperBodyLandmarks[i].position.y * SCALE_FACTOR
            upperBodyCoords[i] = Pair(posX, posY)
        }

        val middleX =
            ((upperBodyLandmarks[0].position.x + upperBodyLandmarks[1].position.x) / 2) * SCALE_FACTOR
        val middleY =
            ((upperBodyLandmarks[0].position.y * 2 + upperBodyLandmarks[2].position.y) / 3) * SCALE_FACTOR // 1:2 내분점
        pressureCoord = Pair(middleX, middleY)
    }

    fun setArmLandmarks(landmarks: List<PoseLandmark?>, imageHeight: Int, imageWidth: Int) {
        // 이전 분석 도구에 의한 그림은 삭제하도록
        closedUpperBodyDetector = true
        invalidate()

//        var scaleFactor = (round(width * 1f / imageWidth) + round(height * 1f / imageHeight)) / 2
//        Log.d("PoseDetection", scaleFactor.toString())

        armLandmarks = landmarks as List<PoseLandmark>

        for (i in armLandmarks.indices) {
            val posX = armLandmarks[i].position.x * SCALE_FACTOR
            val posY = armLandmarks[i].position.y * SCALE_FACTOR
            armCoords[i] = Pair(posX, posY)
        }
    }

    companion object {
        private const val DOT_RADIUS = 20.0f
        private const val STROKE_WIDTH = 10.0f
        private const val UPPER_BODY_LANDMARKS = 4
        private const val ARM_LANDMARKS = 6
        private const val SCALE_FACTOR = 3.3F
    }
}