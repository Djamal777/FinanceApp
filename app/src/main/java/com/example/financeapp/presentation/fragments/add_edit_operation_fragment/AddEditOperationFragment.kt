package com.example.financeapp.presentation.fragments.add_edit_operation_fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.databinding.FragmentAddOrEditOperationBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddEditOperationFragment : Fragment() {

    private lateinit var binding: FragmentAddOrEditOperationBinding
    private val viewModel: AddEditOperationViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<Account>
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddOrEditOperationBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.title = if (viewModel.operation != null) {
                moneyAmount.setText(viewModel.operation!!.money.toString())
                val calendar=Calendar.getInstance()
                calendar.timeInMillis = viewModel.operation!!.date
                date.setText("${calendar.get(Calendar.DATE)} ${
                    calendar.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        Locale.getDefault()
                    )
                } ${viewModel.year!!}")
                requireContext().resources.getString(R.string.edit_operation)
            } else requireContext().resources.getString(R.string.add_operation)
            toolbar.menu.findItem(R.id.delete).isVisible = false
            toolbar.menu.findItem(R.id.confirm)
                .setOnMenuItemClickListener { confirmListener(); true }
            toolbar.setNavigationOnClickListener { backListener() }
            date.setOnClickListener { dateListener() }
        }
        setupSpinner()
        collectEvents()
    }

    private fun setupSpinner() {
        viewModel.accounts.observe(viewLifecycleOwner) {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                it
            )
            val index = it.indexOf(it.find { acc ->
                acc.accId == viewModel.operation?.accountId
            })
            binding.acc.adapter = adapter
            binding.acc.setSelection(index)
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect {
                when (it) {
                    is AddEditOperationViewModel.Event.ShowDatePicker -> {
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, viewModel.year!!)
                        calendar.set(Calendar.MONTH, viewModel.month!! - 1)
                        val thisMonthAndYear = calendar.timeInMillis
                        val constraintsBuilder =
                            CalendarConstraints.Builder()
                                .setStart(thisMonthAndYear)
                                .setEnd(thisMonthAndYear)
                        val dialog = MaterialDatePicker.Builder.datePicker()
                            .setCalendarConstraints(constraintsBuilder.build()).build()
                        dialog.show(parentFragmentManager, "tag")
                        dialog.addOnPositiveButtonClickListener { selection ->
                            Log.d("AddEditOperation", "collectEvents: $selection")
                            calendar.timeInMillis = selection
                            viewModel.selection=selection
                            binding.date.setText(
                                "${calendar.get(Calendar.DATE)} ${
                                    calendar.getDisplayName(
                                        Calendar.MONTH,
                                        Calendar.LONG,
                                        Locale.getDefault()
                                    )
                                } ${viewModel.year!!}"
                            )
                        }
                    }
                    is AddEditOperationViewModel.Event.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    is AddEditOperationViewModel.Event.NavigateBackAfterAdding -> {
                        binding.apply {
                            if (moneyAmount.text?.isBlank() == true ||
                                date.text?.isBlank() == true ||
                                acc.selectedItem == null
                            ) {
                                snackbar = Snackbar.make(
                                    requireView(),
                                    "Заполните все поля!",
                                    Snackbar.LENGTH_SHORT
                                ).apply {
                                    show()
                                }
                            } else {
                                if (viewModel.operation == null) {
                                    viewModel.insertOperation(
                                        Operation(
                                            date = viewModel.selection,
                                            money = moneyAmount.text.toString().toDouble(),
                                            categoryId = viewModel.categoryAndMoney!!.categoryId,
                                            moneyId = viewModel.categoryAndMoney!!.moneyId,
                                            accountId = (acc.selectedItem as Account).accId
                                        )
                                    )
                                    if (binding.yes.isChecked) {
                                        viewModel.updateAccMoneyAfterInsertion(
                                            moneyAmount.text.toString().toDouble(),
                                            (acc.selectedItem as Account).accId
                                        )
                                    }
                                    viewModel.updateCategoryMoneyAfterInsertion(
                                        moneyAmount.text.toString().toDouble(),
                                        viewModel.categoryAndMoney!!.moneyId
                                    )
                                } else {
                                    viewModel.updateOperation(
                                        Operation(
                                            id = viewModel.operation!!.id,
                                            date = viewModel.selection,
                                            money = moneyAmount.text.toString().toDouble(),
                                            categoryId = viewModel.categoryAndMoney!!.categoryId,
                                            moneyId = viewModel.categoryAndMoney!!.moneyId,
                                            accountId = (acc.selectedItem as Account).accId
                                        )
                                    )
                                    if (binding.yes.isChecked) {
                                        viewModel.updateAccMoneyAfterUpdating(
                                            moneyAmount.text.toString().toDouble(),
                                            (acc.selectedItem as Account).accId
                                        )
                                    }
                                    viewModel.updateCategoryMoneyAfterUpdating(
                                        moneyAmount.text.toString().toDouble(),
                                        viewModel.categoryAndMoney!!.moneyId
                                    )
                                }
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun dateListener() {
        viewModel.onDateClick()
    }

    private fun backListener() {
        viewModel.onBackClick()
    }

    private fun confirmListener() {
        viewModel.onConfirmClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.takeIf { it.isShown }?.dismiss()
    }
}