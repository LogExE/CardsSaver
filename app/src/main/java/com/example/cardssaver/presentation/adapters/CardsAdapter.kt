package com.example.cardssaver.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardssaver.R
import com.example.cardssaver.databinding.ItemCardBinding
import com.example.cardssaver.domain.Card

class CardsAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

    var cards: List<Card> = listOf()

    class CardsViewHolder(
        val binding: ItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(inflater, parent, false)
        return CardsViewHolder(binding)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val card: Card = cards[position]
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(card)
        }
        with(holder.binding) {
            cardNameTextView.text = card.name
            if (card.image.isNotBlank()) {
                Glide.with(photoImageView.context)
                    .load(card.image)
                    .placeholder(R.drawable.ic_credcard)
                    .error(R.drawable.ic_credcard)
                    .into(photoImageView)
            } else {
                photoImageView.setImageResource(R.drawable.ic_credcard)
            }
        }
    }
}