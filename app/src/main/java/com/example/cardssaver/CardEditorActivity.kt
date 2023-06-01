package com.example.cardssaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.cardssaver.databinding.ActivityCardEditorBinding
import com.example.cardssaver.domain.Card
import com.example.cardssaver.presentation.fragments.CardInputFragment
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val card = intent.getSerializableExtra("card") as Card

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.cardInputFragView.id, CardInputFragment.newInstance(card))
        }

        val cardViewModel: CardMainViewModel by viewModels()
        val alertDialog = MaterialAlertDialogBuilder(this)
            .setMessage("You sure?")
            .setTitle("Confirmation")
            .setPositiveButton("yes") { dialog, _ ->
                dialog.dismiss()
                cardViewModel.deleteCard(card)
                finish()
            }
            .setNegativeButton("no") { dialog, _ ->
                dialog.dismiss()
            }

        //спрашиваем подтверждение при удалении
        binding.delCardButton.setOnClickListener {
            alertDialog.create().show()
        }

        //proceed
        supportFragmentManager.setFragmentResultListener("cardKey", this) { _, bundle ->
            cardViewModel.updateCard(
                card.copy(
                    name = bundle.getString("cardName")!!,
                    value = bundle.getString("cardValue")!!,
                    type = BarcodeFormat.valueOf(bundle.getString("cardType")!!),
                    image = bundle.getString("cardImage")!!,
                    info = bundle.getString("cardInfo")!!
                )
            )
            finish()
        }
    }
}