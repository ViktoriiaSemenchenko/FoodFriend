package com.semenchenko.foodfriend.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream


class PhotoManager {
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun getBitmapFromUri(uri: Uri, context: Context): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
}
