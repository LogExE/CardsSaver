package com.example.cardssaver.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cardssaver.R
import com.example.cardssaver.databinding.FragmentCardChooserBinding
import com.example.cardssaver.databinding.FragmentCardCreatorBinding
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardCreatorFragment : Fragment() {
    private var _binding: FragmentCardCreatorBinding? = null
    private val binding get() = _binding!!

    private val cardViewModel: CardMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.commit {
            setReorderingAllowed(true)
            add<CardInputFragment>(binding.cardInputFragView.id)
        }
        //при нажатии на proceed
        childFragmentManager.setFragmentResultListener(
            "cardKey", this
        ) { _, bundle ->
            cardViewModel.insertCard(
                bundle.getString("cardName")!!,
                bundle.getString("cardValue")!!,
                BarcodeFormat.valueOf(bundle.getString("cardType")!!),
                bundle.getString("cardImage")!!,
                bundle.getString("cardInfo")!!
            )
            findNavController().popBackStack()
        }
    }
}