package com.pri.architecture_boilerplate.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Images
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


object BitmapUtils {


    fun getResizedBitmap(image:Bitmap, maxWidth:Int, maxHeight:Int):Bitmap{
        if (maxHeight > 0 && maxWidth > 0) {
            val width = image.width
            val height = image.height
            val ratioBitmap =  width / height.toFloat()
            val ratioMax = maxWidth /  maxHeight.toFloat()

            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
        } else {

            return image
        }
    }

    /**
     * returns the bytesize of the give bitmap
     */
    fun byteSizeOf(bitmap: Bitmap): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            bitmap.byteCount
        } else {
            bitmap.rowBytes * bitmap.height
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    fun combineImagesHorizontally(
            first: Bitmap,
            second: Bitmap
    ): Bitmap { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        var cs: Bitmap? = null

        val width: Int
        var height = 0

        if (first.width > second.width) {
            width = first.width + second.width
            height = first.height
        } else {
            width = second.width + second.width
            height = first.height
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(cs!!)

        comboImage.drawBitmap(first, 0f, 0f, null)
        comboImage.drawBitmap(second, first.width.toFloat(), 0f, null)


        return cs
    }
    fun combineImagesVertically(
            first: Bitmap,
            second: Bitmap
    ): Bitmap { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        var cs: Bitmap? = null

        val firstResized = getResizedBitmap(first, 800, 750)
        val secondResized = getResizedBitmap(second, 800, 750)

        cs = Bitmap.createBitmap(
                firstResized.width,
                firstResized.height + secondResized.height,
                Bitmap.Config.ARGB_8888
        )

        val comboImage = Canvas(cs!!)

        comboImage.drawBitmap(firstResized, 0f, 0f, null)
        comboImage.drawBitmap(secondResized, 0f, firstResized.height.toFloat(), null)


        return cs
    }


    fun createSingleImageFromMultipleImages(firstImage: Bitmap, secondImage: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(
                firstImage.width,
                firstImage.height + secondImage.height,
                firstImage.config
        )
        val canvas = Canvas(result)
        canvas.drawBitmap(firstImage, 0f, 0f, null)
        canvas.drawBitmap(secondImage, 0f, firstImage.height.toFloat(), null)
        return result
    }

    fun getStringFromBitmap(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun getImageUriFromBitmap(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun fileFromBitmap(bitmap: Bitmap?,context: Context): File{
        //create a file to write bitmap customerMenu
        val f = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
        f.createNewFile()

        //Convert bitmap to byte array
        val os = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, os)
        val bitmapdata = os.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return f
    }

    fun uriFromBitmap(bitmap: Bitmap, context: Context) = Uri.parse(fileFromBitmap(bitmap, context).absolutePath)
}
