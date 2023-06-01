package com.example.cardssaver.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cardssaver.databinding.FragmentCardDisplayBinding
import com.journeyapps.barcodescanner.BarcodeEncoder

class CardDisplayFragment : Fragment() {
    private var _binding: FragmentCardDisplayBinding? = null
    private val binding get() = _binding!!

    private val args: CardDisplayFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val card = args.cardToDisplay

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
            val action = CardDisplayFragmentDirections.editCardAction(card)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}