package com.example.financeapp.presentation.fragments.add_edit_category_fragment

import android.os.Bundle
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
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.databinding.FragmentAddOrEditCategoryBinding
import com.example.financeapp.presentation.fragments.add_edit_account_fragment.AddEditAccountFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddOrEditCategoryBinding
    private val categoryViewModel: AddEditCategoryViewModel by viewModels()
    var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddOrEditCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvents()
        binding.apply {
            if (categoryViewModel.type == CategoryType.INCOME) {
                binding.moneyPlan.visibility = View.GONE
            } else {
                moneyPlan.setText(categoryViewModel.categoryAndMoney?.plan?.toString())
            }
            if (categoryViewModel.categoryAndMoney == null) {
                title.text = requireActivity().resources.getString(R.string.add_category)
                categoryImage.setImageResource(R.drawable.ic_baseline_shopping_basket_24)
                categoryImage.tag = R.drawable.ic_baseline_shopping_basket_24
                toolbar.menu.findItem(R.id.delete).isVisible = false
            } else {
                categoryViewModel.categoryAndMoney?.let {
                    title.text = requireActivity().resources.getString(R.string.edit_category)
                    categoryImage.setImageResource(
                        requireContext().resources.getIdentifier(
                            it.icon,
                            "drawable",
                            requireContext().packageName
                        )
                    )
                    categoryImage.tag = it.icon
                    categoryName.setText(it.categoryName)
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
            categoryImage.setOnClickListener {
                categoryImageListener()
            }
        }
        setFragmentResultListener("icon_request"){_,bundle->
            val result=bundle.getString("icon_result")
            binding.categoryImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    requireContext().resources.getIdentifier(result, "drawable", requireContext().packageName)
                )
            )
            categoryViewModel.tag=result
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            categoryViewModel.event.collect {
                when (it) {
                    is AddEditCategoryViewModel.Event.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    is AddEditCategoryViewModel.Event.ShowConfirmationDialog -> {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Удалить ${categoryViewModel.categoryAndMoney?.categoryName}?")
                            .setPositiveButton("Да") { dialog, which ->
                                categoryViewModel.deleteCategory(categoryViewModel.categoryAndMoney!!.categoryId)
                                categoryViewModel.deleteMoney(categoryViewModel.categoryAndMoney!!.categoryId)
                                categoryViewModel.deleteOperationsByCategory(categoryViewModel.categoryAndMoney!!.categoryId)
                                findNavController().popBackStack()
                            }
                            .setNegativeButton("Отмена") { dialog, which ->
                                dialog.cancel()
                            }
                            .show()
                    }
                    is AddEditCategoryViewModel.Event.NavigateBackAfterAdding -> {
                        binding.apply {
                            if (categoryName.text?.isBlank() == true) {
                                snackbar = Snackbar.make(
                                    requireView(),
                                    "Заполните все поля!",
                                    Snackbar.LENGTH_SHORT
                                ).apply {
                                    show()
                                }
                            } else {
                                if (categoryViewModel.categoryAndMoney == null) {
                                    val money = if (moneyPlan.text?.isBlank() == true) {
                                        null
                                    } else moneyPlan.text.toString().toDouble()
                                    categoryViewModel.tag?.let { it1 ->
                                        Category(
                                            categoryName = categoryName.text.toString(),
                                            type = categoryViewModel.type!!,
                                            icon = it1
                                        )
                                    }?.let { it2 ->
                                        categoryViewModel.insertCategory(
                                            it2, money
                                        )
                                    }
                                } else {
                                    categoryViewModel.tag?.let { it1 ->
                                        categoryViewModel.updateCategory(
                                            categoryViewModel.categoryAndMoney!!.categoryId,
                                            categoryName.text.toString(),
                                            it1
                                        )
                                    }
                                    if (categoryViewModel.type == CategoryType.EXPENSE) {
                                        if (!moneyPlan.text.isNullOrBlank()) {
                                            categoryViewModel.updateMoney(
                                                categoryViewModel.categoryAndMoney!!.moneyId,
                                                moneyPlan.text.toString().toDouble()
                                            )
                                        } else {
                                            categoryViewModel.updateMoney(
                                                categoryViewModel.categoryAndMoney!!.moneyId,
                                                null
                                            )
                                        }
                                    } else {
                                        categoryViewModel.updateMoney(
                                            categoryViewModel.categoryAndMoney!!.moneyId,
                                            null
                                        )
                                    }
                                }
                                findNavController().popBackStack()
                            }
                        }
                    }
                    is AddEditCategoryViewModel.Event.ShowIconsDialog -> {
                        val action= AddEditCategoryFragmentDirections.actionGlobalIconsDialogFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun categoryImageListener() {
        categoryViewModel.onIconClick()
    }

    private fun confirmListener() {
        categoryViewModel.onConfirmClick()
    }

    private fun deleteListener() {
        categoryViewModel.onDeleteClick()
    }

    private fun backClickListener() {
        categoryViewModel.onBackClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.takeIf { it.isShown }?.dismiss()
    }
}