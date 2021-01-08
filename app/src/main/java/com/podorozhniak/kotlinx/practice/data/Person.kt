package com.podorozhniak.kotlinx.practice.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    @SerializedName("name")
    val name: String,
    @SerializedName("age")
    val age: Int
): Parcelable