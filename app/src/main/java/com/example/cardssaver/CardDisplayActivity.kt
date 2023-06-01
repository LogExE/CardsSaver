package com.example.cardssaver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.cardssaver.databinding.ActivityCardDisplayBinding
import com.example.cardssaver.domain.Card
import com.journeyapps.barcodescanner.BarcodeEncoder

class CardDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val card = intent.getSerializableExtra("card")!! as Card

        //название
        binding.cardTextView.text = card.name

        //генерация картинки кода
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(card.value, card.type, 1024, 1024)
        binding.codeImageView.setImageBitmap(bitmap)
        //значение кода
        binding.valueTextView.text = card.value

        //если указана информация, отображаем ее
        if (card.info.isNotBlank()) {
            binding.infoLayout.visibility = View.VISIBLE
            binding.infoTextView.text = card.info
        } else binding.infoLayout.visibility = View.INVISIBLE

        //пользователь может изменить карту при нажатии на Edit
        binding.editCardButton.setOnClickListener {
            startActivity(Intent(this, CardEditorActivity::class.java).apply {
                this.putExtra("card", card)
            })
            finish()
        }
    }
}