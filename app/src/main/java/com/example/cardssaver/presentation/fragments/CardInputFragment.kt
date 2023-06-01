package com.example.cardssaver.presentation.fragments

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import com.example.cardssaver.databinding.FragmentCardInputBinding
import com.example.cardssaver.domain.Card
import com.example.cardssaver.presentation.adapters.OnClickListener
import com.google.zxing.BarcodeFormat

private const val ARG_CARD_NAME = "card_name"
private const val ARG_CARD_VALUE = "card_value"
private const val ARG_CARD_TYPE = "card_type"
private const val ARG_CARD_INFO = "card_info"
private const val ARG_CARD_IMAGE = "card_image"

class CardInputFragment : Fragment() {
    private var cardName: String? = null
    private var cardValue: String? = null
    private var cardType: BarcodeFormat? = null
    private var cardInfo: String? = null
    private var cardImage: String? = null

    private var _binding: FragmentCardInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cardName = it.getString(ARG_CARD_NAME)
            cardValue = it.getString(ARG_CARD_VALUE)
            cardType = it.getSerializable(ARG_CARD_TYPE) as? BarcodeFormat
            cardInfo = it.getString(ARG_CARD_INFO)
            cardImage = it.getString(ARG_CARD_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerItems = BarcodeFormat.values()
        binding.codeSpinner.adapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, spinnerItems)

        if (cardName != null)
            binding.cardNameEditText.setText(cardName)
        if (cardValue != null)
            binding.cardValueEditText.setText(cardValue)
        if (cardType != null)
            binding.codeSpinner.setSelection(spinnerItems.indexOf(cardType))
        var retCardImage = cardImage ?: ""

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    requireActivity().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    )
                    retCardImage = uri.toString()
                }
            }
        binding.imagePickButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.proceedButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("cardName", binding.cardNameEditText.text.toString())
            bundle.putString("cardValue", binding.cardValueEditText.text.toString())
            bundle.putString("cardType", binding.codeSpinner.selectedItem.toString())
            bundle.putString("cardInfo", binding.cardInfoText.text.toString())
            bundle.putString("cardImage", binding.imagePickButton.text.toString())
            setFragmentResult("cardKey", bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(card: Card) =
            CardInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CARD_NAME, card.name)
                    putString(ARG_CARD_VALUE, card.value)
                    putSerializable(ARG_CARD_TYPE, card.type)
                    putString(ARG_CARD_INFO, card.info)
                    putString(ARG_CARD_IMAGE, card.image)
                }
            }

        @JvmStatic
        fun newInstance(
            name: String? = null,
            value: String? = null,
            type: String? = null,
            info: String = "",
            image: String = ""
        ) =
            CardInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CARD_NAME, name)
                    putString(ARG_CARD_VALUE, value)
                    putSerializable(ARG_CARD_TYPE, type?.let { BarcodeFormat.valueOf(it) })
                    putString(ARG_CARD_INFO, info)
                    putString(ARG_CARD_IMAGE, image)
                }
            }
    }
}