package com.example.cardssaver.domain

import com.example.cardssaver.data.local.entity.CardEntity

fun CardEntity.toDomain() = Card(
    id = this.id,
    name = this.name,
    value = this.value,
    info = this.info,
    type = this.type,
    image = this.image
)

fun Card.toLocal() = CardEntity(
    id = this.id,
    name = this.name,
    value = this.value,
    info = this.info,
    type = this.type,
    image = this.image
)