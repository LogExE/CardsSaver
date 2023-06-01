package com.example.cardssaver.presentation.fragments.adapters

import com.example.cardssaver.domain.Card

class OnClickListener (val clickListener: (card: Card) -> Unit) {
    fun onClick(card: Card) = clickListener(card)
}