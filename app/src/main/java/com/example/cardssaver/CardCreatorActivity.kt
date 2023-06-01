package com.example.cardssaver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.example.cardssaver.databinding.ActivityCardCreatorBinding
import com.example.cardssaver.presentation.fragments.CardInputFragment
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardCreatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardCreatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(
                binding.cardInputFragView.id, CardInputFragment.newInstance(
                    value = intent.getStringExtra("cardValue"),
                    type = intent.getStringExtra("cardFormat")
                )
            )
        }

        val cardViewModel: CardMainViewModel by viewModels()
        supportFragmentManager.setFragmentResultListener("cardKey", this) { _, bundle ->
            cardViewModel.insertCard(
                bundle.getString("cardName")!!,
                bundle.getString("cardValue")!!,
                BarcodeFormat.valueOf(bundle.getString("cardType")!!),
                bundle.getString("cardImage")!!,
                bundle.getString("cardInfo")!!
            )
            finish()
        }
    }
}