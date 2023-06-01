package com.example.cardssaver.domain

import android.os.Parcelable
import com.google.zxing.BarcodeFormat
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Card(
    val id: Int,
    val name: String,
    val value: String,
    val info: String = "",
    val type: BarcodeFormat,
    val image: String = ""
) : Parcelable