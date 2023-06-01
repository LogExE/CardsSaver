package com.example.cardssaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardssaver.databinding.ActivityMainBinding
import com.example.cardssaver.domain.toDomain
import com.example.cardssaver.presentation.fragments.adapters.CardsAdapter
import com.example.cardssaver.presentation.fragments.adapters.OnClickListener
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}