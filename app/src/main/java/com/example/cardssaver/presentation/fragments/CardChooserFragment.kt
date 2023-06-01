package com.example.cardssaver.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cardssaver.databinding.FragmentCardChooserBinding
import com.example.cardssaver.domain.toDomain
import com.example.cardssaver.presentation.fragments.adapters.CardsAdapter
import com.example.cardssaver.presentation.fragments.adapters.OnClickListener
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardChooserFragment : Fragment() {
    private var _binding: FragmentCardChooserBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardsListAdapter: CardsAdapter

    private val cardViewModel: CardMainViewModel by viewModels()

    //при клике на карту открывается CardDisplay
    private val adapterOnClick = OnClickListener {
        val action = CardChooserFragmentDirections.displayCardAction(it)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //настройка списка карт
        cardsListAdapter = CardsAdapter(adapterOnClick)
        binding.cardRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.cardRecycleView.adapter = cardsListAdapter

        //поддержка состояния списка карт
        cardViewModel.allCards.observe(requireActivity()) {
            cardsListAdapter.cards = it.map { cardEntity -> cardEntity.toDomain() }
            cardsListAdapter.notifyDataSetChanged()
        }

        //поведение кнопки Add a new card
        binding.addcardbutton.setOnClickListener {
            val action =
                CardChooserFragmentDirections.createCardAction()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}