package com.podorozhniak.kotlinx.practice.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.podorozhniak.kotlinx.R
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.random.Random

object FileManager {

    fun createImageFile(context: Context, name: String): File {
        return createFile(createFolderForImages(context), name)
    }

    private fun createFolderForImages(context: Context): File {
        val appContext = context.applicationContext

        val mediaDir = appContext.externalMediaDirs.firstOrNull()?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) {
            mediaDir
        } else {
            appContext.filesDir
        }
    }

    private fun createFile(baseFolder: File, name: String) =
        File(baseFolder, "${name.toLowerCase().capitalize()}_${Random.nextInt(0, 999)}.png")

    fun saveToStorageAsync(
        context: Context, stateImage: Bitmap, imageFileName: String
    ): String {
        try {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE)
                .use { fileOutputStream ->
                    stateImage.compress(Bitmap.CompressFormat.PNG, 50, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }
        } catch (e: IOException) {
            Log.d("STATE", "saveToStorage: ", e.fillInStackTrace())
        } catch (e: Exception) {
            Log.d("STATE", "saveToStorage: ", e.fillInStackTrace())
        }
        return context.filesDir.absolutePath
    }

    fun saveToStoragePreviewAsync(
        context: Context,
        previewBitmap: Bitmap?,
        imageFileName: String
    ): String {
        if (previewBitmap == null)
            return ""
        try {
            context.openFileOutput(imageFileName, Context.MODE_PRIVATE)
                .use { fileOutputStream ->
                    previewBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }
        } catch (e: IOException) {
            Log.d("STATE", "saveToStorage: ", e.fillInStackTrace())
        } catch (e: Exception) {
            Log.d("STATE", "saveToStorage: ", e.fillInStackTrace())
        }

        return context.filesDir.absolutePath
    }

    fun loadImageBitmap(
        context: Context,
        imageFileName: String,
        onDecodeBitmap: (Bitmap) -> Unit,
        onErrorAction: (Exception) -> Unit = {}
    ) {
        try {
            val file = File(context.filesDir, imageFileName)
            val b: Bitmap = BitmapFactory.decodeStream(FileInputStream(file))
            onDecodeBitmap(b)
        } catch (e: FileNotFoundException) {
            Log.d("STATE", "loadImageBitmap: ", e.fillInStackTrace())
            onErrorAction(e)
        } catch (e: Exception) {
            Log.d("STATE", "loadImageBitmap: ", e.fillInStackTrace())
            onErrorAction(e)
        }
    }


}