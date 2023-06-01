package com.example.cardssaver.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.zxing.BarcodeFormat

@Entity
data class CardEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String,
    val value: String,
    var info: String = "",
    val type: BarcodeFormat,
    var image: String = ""
)
