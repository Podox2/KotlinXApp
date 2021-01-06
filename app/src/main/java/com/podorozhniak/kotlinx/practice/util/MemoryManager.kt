package com.podorozhniak.kotlinx.practice.util

import android.app.ActivityManager
import android.os.Environment
import android.os.StatFs
import android.util.Log

object MemoryManager {
    fun getAvailableInternalMemorySize(): String? {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return formatSize(availableBlocks * blockSize)
    }

    fun getTotalInternalMemorySize(): String? {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return formatSize(totalBlocks * blockSize)
    }

    private fun formatSize(_size: Long): String? {
        var size = _size
        var suffix: String? = null
        if (size >= 1024) {
            suffix = "KB"
            size /= 1024
            if (size >= 1024) {
                suffix = "MB"
                size /= 1024
            }
        }
        val resultBuffer =
            StringBuilder(java.lang.Long.toString(size))
        var commaOffset = resultBuffer.length - 3
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',')
            commaOffset -= 3
        }
        if (suffix != null) resultBuffer.append(suffix)
        return resultBuffer.toString()
    }

    //should check RAM
    fun checkMemory(activityManager: ActivityManager){
        val mi = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(mi)
        val availableMegs: Long = mi.availMem / 0x100000L
        val percentAvail: Long = mi.availMem / mi.totalMem * 100L
        Log.i(
            "TAG_TEST", "$availableMegs mb"
        )

        Log.i(
            "TAG_TEST", "$percentAvail %"
        )
    }
}