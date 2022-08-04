package com.example.financeapp.presentation.fragments.category_operations_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.R
import com.example.financeapp.databinding.FragmentCategoryOperationsBinding
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.presentation.fragments.finances_fragment.OperationsAdapter
import com.google.android.material.snackbar.Snackbar
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryOperationsFragment : Fragment(), OperationsAdapter.OnOperationClickListener {

    private lateinit var binding: FragmentCategoryOperationsBinding
    private lateinit var operationsAdapter: OperationsAdapter
    private val operationsViewModel: CategoryOperationsViewModel by viewModels()
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryOperationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeOperations()
        collectEvents()
        binding.apply {
            categoryName.text = operationsViewModel.categoryAndMoney?.categoryName
            toolbar.setNavigationOnClickListener {
                operationsViewModel.onBackClick()
            }
            fabAdd.setOnClickListener {
                operationsViewModel.onAddClick()
            }
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            operationsViewModel.event.collect {
                when (it) {
                    is CategoryOperationsViewModel.Event.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    is CategoryOperationsViewModel.Event.NavigateToEditOperationScreen -> {
                        val action = CategoryOperationsFragmentDirections
                            .actionCategoryOperationsFragmentToAddEditOperationFragment(
                                month = it.month,
                                year = it.year,
                                operation = it.operation,
                                categoryAndMoney = it.categoryAndMoney
                            )
                        findNavController().navigate(action)
                    }
                    is CategoryOperationsViewModel.Event.NavigateToAddOperationScreen -> {
                        val action = CategoryOperationsFragmentDirections
                            .actionCategoryOperationsFragmentToAddEditOperationFragment(
                                month = it.month,
                                year = it.year,
                                categoryAndMoney = it.categoryAndMoney
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }


    private fun observeOperations() {
        operationsViewModel.operations.observe(viewLifecycleOwner) {
            operationsAdapter.differ.submitList(it)
            if(it.isEmpty()){
                binding.emptyRecyclerOperations.visibility=View.VISIBLE
            }else{
                binding.emptyRecyclerOperations.visibility=View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
        operationsAdapter = OperationsAdapter(requireContext(), this)
        binding.recyclerView.apply {
            adapter = operationsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(StickyRecyclerHeadersDecoration(operationsAdapter))
            setHasFixedSize(true)
        }
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val operation =
                    operationsAdapter.differ.currentList[viewHolder.bindingAdapterPosition]
                operationsViewModel.deleteOperation(operation.id)
                operationsViewModel.updateCategoryMoney(
                    operation.money,
                    operationsViewModel.categoryAndMoney!!.moneyId
                )
                Snackbar.make(
                    requireView(),
                    "Операция удалена! Учесть изменения в счёте?",
                    Snackbar.LENGTH_LONG
                ).setAction(R.string.yes) {
                    operationsViewModel.updateAccMoney(operation.money, operation.accountId)
                }.show()
            }

        }).attachToRecyclerView(binding.recyclerView)
    }

    override fun onOperationClickListener(operation: OperationAndCategoryAndAccount) {
        operationsViewModel.onOperationClick(operation)
    }

    override fun onDestroy() {
        super.onDestroy()
        snackbar?.takeIf { it.isShown }?.dismiss()
    }
}