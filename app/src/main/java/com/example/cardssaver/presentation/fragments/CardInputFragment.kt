package com.example.cardssaver.presentation.fragments

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.cardssaver.databinding.FragmentCardInputBinding
import com.example.cardssaver.domain.Card
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

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

    private val scanOptions = ScanOptions()
        .setPrompt("Scan the card")
        .setBeepEnabled(false)
        .setOrientationLocked(false)

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
        if (cardInfo != null)
            binding.cardInfoText.setText(cardInfo)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    requireActivity().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    retCardImage = uri.toString()
                }
            }
        binding.imagePickButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.proceedButton.setOnClickListener {
            if (!checkInputOk()) {
                Toast.makeText(requireContext(), "Wrong card value format!", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            val bundle = Bundle()
            bundle.putString("cardName", binding.cardNameEditText.text.toString())
            bundle.putString("cardValue", binding.cardValueEditText.text.toString())
            bundle.putString("cardType", binding.codeSpinner.selectedItem.toString())
            bundle.putString("cardInfo", binding.cardInfoText.text.toString())
            bundle.putString("cardImage", retCardImage)
            setFragmentResult("cardKey", bundle)
        }

        //сканирование
        val barcodeLauncher = registerForActivityResult(ScanContract()) { res ->
            if (res.contents != null) {
                binding.cardValueEditText.setText(res.contents)
                binding.codeSpinner.setSelection(spinnerItems.indexOf(BarcodeFormat.valueOf(res.formatName)))
            }
        }
        binding.scanButton.setOnClickListener {
            barcodeLauncher.launch(scanOptions)
        }
    }

    private fun checkInputOk(): Boolean {
        val value = binding.cardValueEditText.text.toString()
        val type = BarcodeFormat.valueOf(binding.codeSpinner.selectedItem.toString())
        return try {
            BarcodeEncoder().encodeBitmap(value, type, 1024, 1024)
            true
        } catch (e: WriterException) {
            false
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
    }
}