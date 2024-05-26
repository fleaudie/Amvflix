package com.fleaudie.amvflix.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class GradientRoundedTransformation(private val radius: Float) : BitmapTransformation() {
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("GradientRoundedTransformation".toByteArray(Key.CHARSET))
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return applyGradientAndRoundedCorners(toTransform)
    }

    private fun applyGradientAndRoundedCorners(source: Bitmap): Bitmap {
        val width = source.width
        val height = source.height

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true

        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, 0f, 0f, paint)

        val gradientPaint = Paint()
        val gradient = LinearGradient(
            0f, height.toFloat(), 0f, height / 2f,
            Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP
        )
        gradientPaint.shader = gradient
        gradientPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint)

        return bitmap
    }
}