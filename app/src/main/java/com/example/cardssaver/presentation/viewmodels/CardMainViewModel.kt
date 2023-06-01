package com.example.cardssaver.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cardssaver.data.local.CardSaverDatabase
import com.example.cardssaver.data.local.entity.CardEntity
import com.example.cardssaver.domain.Card
import com.example.cardssaver.domain.toLocal
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardMainViewModel @Inject constructor(private val db: CardSaverDatabase) : ViewModel() {
    val allCards = db.cardDao().getAll().asLiveData()

    fun insertCard(
        name: String,
        value: String,
        type: BarcodeFormat,
        image: String = "",
        info: String = ""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val card = CardEntity(
                id = 0,
                name = name,
                value = value,
                info = info,
                type = type,
                image = image
            )
            db.cardDao().insertCard(card)
        }
    }

    fun updateCard(card: Card) {
        viewModelScope.launch(Dispatchers.IO) {
            db.cardDao().updateCard(card.toLocal())
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch(Dispatchers.IO) {
            db.cardDao().deleteCard(card.toLocal())
        }
    }

    fun purge() {
        viewModelScope.launch(Dispatchers.IO) {
            db.cardDao().deleteAll()
        }
    }
}