package com.example.financeapp.presentation.fragments.add_edit_account_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.databinding.FragmentAddOrEditAccBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditAccountFragment : Fragment() {

    private lateinit var binding: FragmentAddOrEditAccBinding
    private val accountViewModel: AddEditAccountViewModel by viewModels()
    var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddOrEditAccBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents()
        binding.apply {
            if(accountViewModel.canBeDeleted != true){
                binding.toolbar.menu.findItem(R.id.delete).isVisible=false
            }
            if (accountViewModel.account == null) {
                title.text = requireActivity().resources.getString(R.string.add_account)
                accImage.setImageResource(R.drawable.ic_baseline_account_balance_wallet_24)
                accImage.tag = R.drawable.ic_baseline_account_balance_wallet_24
                toolbar.menu.findItem(R.id.delete).isVisible = false
            } else {
                accountViewModel.account?.let {
                    title.text = requireActivity().resources.getString(R.string.edit_account)
                    accImage.setImageResource(
                        requireContext().resources.getIdentifier(
                            it.icon,
                            "drawable",
                            requireContext().packageName
                        )
                    )
                    accImage.tag = it.icon
                    accName.setText(it.accName)
                    money.setText(it.money.toString())
                }
            }
            toolbar.setNavigationOnClickListener {
                backClickListener()
            }
            toolbar.menu.findItem(R.id.delete).setOnMenuItemClickListener {
                deleteListener()
                true
            }
            toolbar.menu.findItem(R.id.confirm).setOnMenuItemClickListener {
                confirmListener()
                true
            }
            accImage.setOnClickListener {
                accImageListener()
            }
            setFragmentResultListener("icon_request"){_,bundle->
                val result=bundle.getString("icon_result")
                binding.accImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        requireContext().resources.getIdentifier(result, "drawable", requireContext().packageName)
                    )
                )
                accountViewModel.tag=result
            }
        }
    }

    private fun accImageListener() {
        accountViewModel.onIconClick()
    }

    private fun confirmListener() {
        accountViewModel.onConfirmClick()
    }

    private fun deleteListener() {
        accountViewModel.onDeleteClick()
    }

    private fun backClickListener() {
        accountViewModel.onBackClick()
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            accountViewModel.event.collect {
                when (it) {
                    is AddEditAccountViewModel.Event.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    is AddEditAccountViewModel.Event.ShowConfirmationDialog -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Удалить ${accountViewModel.account?.accName}?")
                            .setPositiveButton("Да") { dialog, which ->
                                accountViewModel.deleteAccount(accountViewModel.account!!)
                                accountViewModel.deleteOperationsByAccount(accountViewModel.account!!)
                                findNavController().popBackStack()
                            }
                            .setNegativeButton("Отмена") { dialog, which ->
                                dialog.cancel()
                            }
                            .show()
                    }
                    is AddEditAccountViewModel.Event.NavigateBackAfterAdding -> {
                        binding.apply {
                            if (accName.text?.isBlank() == true || money.text?.isBlank() == true) {
                                snackbar = Snackbar.make(
                                    requireView(),
                                    "Заполните все поля!",
                                    Snackbar.LENGTH_SHORT
                                ).apply {
                                    show()
                                }
                            } else {
                                if (accountViewModel.account == null) {
                                    accountViewModel.tag?.let { it1 ->
                                        Account(
                                            accName = accName.text.toString(),
                                            money = money.text.toString().toDouble(),
                                            icon = it1
                                        )
                                    }?.let { it2 ->
                                        accountViewModel.insertAccount(
                                            it2
                                        )
                                    }
                                } else {
                                    accountViewModel.tag?.let { it1 ->
                                        Account(
                                            accId= accountViewModel.account!!.accId,
                                            accName = accName.text.toString(),
                                            money = money.text.toString().toDouble(),
                                            icon = it1
                                        )
                                    }?.let { it2 ->
                                        accountViewModel.updateAccount(
                                            it2
                                        )
                                    }
                                }
                                findNavController().popBackStack()
                            }
                        }
                    }
                    is AddEditAccountViewModel.Event.ShowIconsDialog -> {
                        val action=AddEditAccountFragmentDirections.actionGlobalIconsDialogFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.takeIf { it.isShown }?.dismiss()
    }
}