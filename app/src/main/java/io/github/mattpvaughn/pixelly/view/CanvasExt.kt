package io.github.mattpvaughn.pixelly.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

/**
 * Method allowing drawing of rects with coordinates + size. Draws a rectangle of dimens [width]
 * by [height] which is [x] pixels from the left of the canvas and [y] pixels from the bottom
 */
fun Canvas.drawRectSane(x: Float, y: Float, height: Float, width: Float, paint: Paint) {
    drawRect(x, y + height, x + width, y, paint)
}

/**
 * Returns a boolean indicating whether a pixel at coordinates (x, y) is inside the bounds
 * of the bitmap
 */
fun Bitmap.isInBounds(x: Int, y: Int) : Boolean {
    return x >= 0 && y >= 0 && x < width && y < height
}