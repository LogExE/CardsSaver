package com.example.cardssaver.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cardssaver.R
import com.example.cardssaver.databinding.FragmentCardChooserBinding
import com.example.cardssaver.databinding.FragmentCardCreatorBinding
import com.example.cardssaver.databinding.FragmentCardEditorBinding
import com.example.cardssaver.presentation.viewmodels.CardMainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardEditorFragment : Fragment() {
    private var _binding: FragmentCardEditorBinding? = null
    private val binding get() = _binding!!

    private val args: CardEditorFragmentArgs by navArgs()

    private val cardViewModel: CardMainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val card = args.cardToEdit

        childFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.cardInputFragView.id, CardInputFragment.newInstance(card))
        }

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage("You sure?")
            .setTitle("Confirmation")
            .setPositiveButton("yes") { dialog, _ ->
                dialog.dismiss()
                cardViewModel.deleteCard(card)
                findNavController().popBackStack(R.id.cardChooserFragment, false)
            }
            .setNegativeButton("no") { dialog, _ ->
                dialog.dismiss()
            }

        //спрашиваем подтверждение при удалении
        binding.delCardButton.setOnClickListener {
            alertDialog.create().show()
        }

        //proceed
        childFragmentManager.setFragmentResultListener("cardKey", this) { _, bundle ->
            cardViewModel.updateCard(
                card.copy(
                    name = bundle.getString("cardName")!!,
                    value = bundle.getString("cardValue")!!,
                    type = BarcodeFormat.valueOf(bundle.getString("cardType")!!),
                    image = bundle.getString("cardImage")!!,
                    info = bundle.getString("cardInfo")!!
                )
            )
            findNavController().popBackStack(R.id.cardChooserFragment, false)
        }
    }
}