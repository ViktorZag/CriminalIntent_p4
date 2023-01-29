package com.viktor_zet.criminalintent_p4.utils

import android.graphics.Bitmap
import android.graphics.Matrix

class Util

fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}