package com.example.cardssaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardssaver.databinding.ActivityMainBinding
import com.example.cardssaver.domain.toDomain
import com.example.cardssaver.presentation.adapters.CardsAdapter
import com.example.cardssaver.presentation.adapters.OnClickListener
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var cardsListAdapter: CardsAdapter

    private val scanOptions = ScanOptions()
        .setPrompt("Scan the card")
        .setBeepEnabled(false)
        .setOrientationLocked(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardsListAdapter = CardsAdapter(OnClickListener {
            startActivity(Intent(this, CardDisplayActivity::class.java).apply {
                this.putExtra("card", it)
            })
        })
        binding.cardRecycleView.layoutManager = LinearLayoutManager(this)
        binding.cardRecycleView.adapter = cardsListAdapter

        val cardViewModel: CardMainViewModel by viewModels()
        cardViewModel.allCards.observe(this) {
            cardsListAdapter.cards = it.map { cardEntity -> cardEntity.toDomain() }
            cardsListAdapter.notifyDataSetChanged()
        }

        val barcodeLauncher = registerForActivityResult(ScanContract()) { res ->
            startActivity(
                Intent(
                    this,
                    CardCreatorActivity::class.java
                ).apply {
                    this.putExtra("cardValue", res.contents)
                    this.putExtra("cardFormat", res.formatName)
                })
        }
        binding.addcardbutton.setOnClickListener {
            barcodeLauncher.launch(scanOptions)
        }
    }
}