package com.example.cardssaver.domain

import com.google.zxing.BarcodeFormat
import java.io.Serializable

data class Card(
    val id: Int,
    val name: String,
    val value: String,
    val info: String = "",
    val type: BarcodeFormat,
    val image: String = ""
) : Serializable