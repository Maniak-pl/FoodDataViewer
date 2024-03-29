package pl.maniak.fooddataviewer.scan

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.fotoapparat.parameter.Resolution
import io.fotoapparat.preview.Frame

fun getFrame(context: Context, assetLocation: String): Frame {
    val inputStream = context.assets.open(assetLocation)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val height = bitmap.height
    val width = bitmap.width
    val byteArray = getNV21(height, width, bitmap)

    return Frame(size = Resolution(width, height), image = byteArray, rotation = 270)
}

private fun getNV21(height: Int, width: Int, scaled: Bitmap): ByteArray {
    val argb = IntArray(width * height)
    scaled.getPixels(argb, 0, width, 0, 0, width, height)
    val yuv = ByteArray(width * height + 2 * Math.ceil(width / 2.0).toInt() * Math.ceil(height / 2.0).toInt())
    encodeYUV420SP(yuv, argb, width, height)
    scaled.recycle()
    return yuv
}

private fun encodeYUV420SP(yuv420sp: ByteArray, argb: IntArray, width: Int, height: Int) {
    val frameSize = width * height
    var yIndex = 0
    var uvIndex = frameSize
    var a: Int
    var R: Int
    var G: Int
    var B: Int
    var Y: Int
    var U: Int
    var V: Int
    var index = 0
    for (j in 0 until height) {
        for (i in 0 until width) {
            a = argb[index] and -0x1000000 shr 24 // a is not used obviously
            R = argb[index] and 0xff0000 shr 16
            G = argb[index] and 0xff00 shr 8
            B = argb[index] and 0xff shr 0

            // well known RGB to YUV algorithm
            Y = (66 * R + 129 * G + 25 * B + 128 shr 8) + 16
            U = (-38 * R - 74 * G + 112 * B + 128 shr 8) + 128
            V = (112 * R - 94 * G - 18 * B + 128 shr 8) + 128

            // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
            //    meaning for every 4 Y pixels there are 1 V and 1 U.  Note the sampling is every other
            //    pixel AND every other scanline.
            yuv420sp[yIndex++] = (if (Y < 0) 0 else if (Y > 255) 255 else Y).toByte()
            if (j % 2 == 0 && index % 2 == 0) {
                yuv420sp[uvIndex++] = (if (V < 0) 0 else if (V > 255) 255 else V).toByte()
                yuv420sp[uvIndex++] = (if (U < 0) 0 else if (U > 255) 255 else U).toByte()
            }
            index++
        }
    }
}