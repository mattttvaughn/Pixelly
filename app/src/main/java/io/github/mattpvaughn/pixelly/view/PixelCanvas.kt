package io.github.mattpvaughn.pixelly.view

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import io.github.mattpvaughn.pixelly.application.APP_NAME
import kotlin.math.floor


class PixelCanvas : View {

    // These constructors allow us to place our canvas in XML
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    )

    companion object {
        const val DEFAULT_HEIGHT = 16
        const val DEFAULT_WIDTH = 16

        enum class Tool {
            PAINT_BRUSH, FILL
        }

    }

    var tool: Tool = Tool.PAINT_BRUSH

    private val pixelHeight = DEFAULT_HEIGHT
    private val pixelWidth = DEFAULT_WIDTH

    private var pixelBitmap: Bitmap =
        Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888).apply {
            for (x in 0 until pixelWidth) {
                for (y in 0 until pixelHeight) {
                    setPixel(x, y, Color.WHITE)
                }
            }
        }
    private val pixelCanvas: Canvas by lazy {
        Canvas(pixelBitmap)
    }
    private val paint = Paint().apply {
        isFilterBitmap = true
        color = Color.BLACK
        strokeWidth = 1F
    }

    fun setColor(@ColorInt color: Int): PixelCanvas {
        paint.color = color
        return this
    }

    /**
     * Define the scaled canvas and related elements `by lazy` because it allows us to use the
     * [getMeasuredWidth] and [getMeasuredHeight] without having to explicitly wait until
     * [onMeasure] to define them.
     *
     * Note: this will fail if any of the elements below is lazily loaded before [onMeasure] is
     * called
     */
    private val scaleX by lazy { measuredWidth / pixelWidth }
    private val scaleY by lazy { measuredHeight / pixelHeight }
    private val scalePixelSize by lazy { measuredHeight.toFloat() / pixelHeight }
    private val scaleBitmap by lazy {
        createPixelScaledBitmap(pixelBitmap, measuredWidth, measuredHeight)
    }
    private val scaleCanvas by lazy { Canvas(scaleBitmap) }
    private val canvasRect by lazy { Rect(0, height, width, 0) }
    private val scaleBitmapRect by lazy { Rect(0, height, width, 0) }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }

        val x = event.x
        val y = event.y
        val pixelX = floor(pixelWidth * (x / measuredWidth))
        val pixelY = floor(pixelHeight * (y / measuredHeight))
        draw(pixelX, pixelY)
        invalidate()

        return true
    }

    private fun draw(pixelX: Float, pixelY: Float) {
        when (tool) {
            Tool.PAINT_BRUSH -> drawPoint(pixelX, pixelY)
            Tool.FILL -> fill(pixelX.toInt(), pixelY.toInt())
        }
    }

    private fun fill(pixelX: Int, pixelY: Int) {
        val fillablePixelSet = mutableSetOf<Pair<Int, Int>>()
        val traveledPixelSet = mutableSetOf<Pair<Int, Int>>()

        val pixelColor = pixelBitmap.getPixel(pixelX, pixelY)

        // BFS outwards, capture all pixels matching the clicked pixel color
        fillAdjacentPixels(fillablePixelSet, traveledPixelSet, pixelColor, pixelX, pixelY)

        for (pixel in fillablePixelSet) {
            drawPoint(pixel.first.toFloat(), pixel.second.toFloat())
        }
    }

    private fun fillAdjacentPixels(
        fillablePixels: MutableSet<Pair<Int, Int>>,
        traveledPixels: MutableSet<Pair<Int, Int>>,
        pixelColor: Int,
        pixelX: Int,
        pixelY: Int
    ) {
        if (traveledPixels.contains(Pair(pixelX, pixelY))) {
            return
        }

        traveledPixels.add(Pair(pixelX, pixelY))

        val left = Pair(pixelX - 1, pixelY)
        val right = Pair(pixelX + 1, pixelY)
        val up = Pair(pixelX, pixelY + 1)
        val down = Pair(pixelX, pixelY - 1)

        val pixels = arrayOf(left, right, up, down)

        for (pixel in pixels) {
            if (pixelBitmap.isInBounds(
                    pixel.first, pixel.second
                ) && pixelColor == pixelBitmap.getPixel(pixel.first, pixel.second)
            ) {
                fillablePixels.add(pixel)
                fillAdjacentPixels(
                    fillablePixels, traveledPixels, pixelColor, pixel.first, pixel.second
                )
            }
        }
    }

    private fun drawPoint(pixelX: Float, pixelY: Float) {
        pixelCanvas.drawPoint(pixelX, pixelY, paint)
        scaleCanvas.drawRectSane(
            pixelX * scaleX, pixelY * scaleX, scalePixelSize, scalePixelSize, paint
        )
    }

    private fun createPixelScaledBitmap(pixelBM: Bitmap, width: Int, height: Int) : Bitmap {
        val scaled = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(scaled)
        val tempPaint = Paint(paint)
        for (x in 0 until pixelBM.width) {
            for (y in 0 until pixelBM.height) {
                tempPaint.color = pixelBM.getPixel(x, y)
                tempCanvas.drawRectSane(
                    x.toFloat() * scaleX,
                    y.toFloat() * scaleX,
                    scalePixelSize,
                    scalePixelSize,
                    tempPaint
                )
            }
        }
        return scaled
    }

    override fun onDraw(canvas: Canvas?) {
        requireNotNull(canvas)
        canvas.drawBitmap(scaleBitmap, scaleBitmapRect, canvasRect, paint)
        super.onDraw(canvas)
    }


    public override fun onSaveInstanceState(): Parcelable? {
        Log.i(APP_NAME, "SAVED INSTANCE")
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putParcelable("pixels", pixelBitmap) // ... save stuff
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        Log.i(APP_NAME, "RESTORE")
        var superState: Parcelable? = state
        if (state is Bundle) {
            superState = state.getParcelable("superState")
            val restoredBitmap = state.getParcelable<Bitmap>("pixels")
            pixelBitmap = restoredBitmap ?: pixelBitmap
        }
        super.onRestoreInstanceState(superState)
    }
}
