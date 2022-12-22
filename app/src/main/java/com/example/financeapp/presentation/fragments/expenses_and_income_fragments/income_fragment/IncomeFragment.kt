package com.example.financeapp.presentation.fragments.expenses_and_income_fragments.income_fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.financeapp.R
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.databinding.FragmentIncomeOrExpensesBinding
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.repository.FinanceRepository
import com.example.financeapp.presentation.MainActivity
import com.example.financeapp.presentation.fragments.expenses_and_income_fragments.CategoriesAdapter
import com.example.financeapp.presentation.fragments.expenses_and_income_fragments.CategoryViewModel
import com.example.financeapp.presentation.fragments.expenses_and_income_fragments.FilterByTypeViewModelFactory
import com.whiteelephant.monthpicker.MonthPickerDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class IncomeFragment : Fragment(), CategoriesAdapter.OnCategoryClickListener {

    private lateinit var binding: FragmentIncomeOrExpensesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var repository: FinanceRepository
    private val incomeViewModel: CategoryViewModel by viewModels {
        FilterByTypeViewModelFactory(
            repository,
            CategoryType.INCOME
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIncomeOrExpensesBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesAdapter = CategoriesAdapter(requireContext(), this)

        setupRecyclerView()
        observeCategories()
        collectEvents()
        addCategoriesOnListener()

        binding.apply {
            toolbar.menu.findItem(R.id.add_category).setOnMenuItemClickListener {
                incomeViewModel.onAddCategoryClick(
                    incomeViewModel.month.value!!,
                    incomeViewModel.year.value!!,
                    CategoryType.EXPENSE
                )
                true
            }
            toolbar.setOnClickListener {
                incomeViewModel.onDateClick()
            }
            val calendar=Calendar.getInstance()
            calendar.set(Calendar.YEAR, incomeViewModel.year.value!!)
            calendar.set(Calendar.MONTH, incomeViewModel.month.value!!-1)
            val curDate = "${
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, Locale.getDefault())
            } " + calendar.get(Calendar.YEAR)
            date.text = curDate
        }
    }

    private fun addCategoriesOnListener() {
        binding.categoriesRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (gridLayoutManager.findLastVisibleItemPosition() == categoriesAdapter.differ.currentList.size - 1 &&
                    dy>0
                ) {
                    (activity as MainActivity).binding.bottomNavigationView.apply {
                        clearAnimation()
                        animate().translationY((height * 2).toFloat()).duration = 200
                    }
                } else {
                    (activity as MainActivity).binding.bottomNavigationView.apply {
                        clearAnimation()
                        animate().translationY(0F).duration = 100
                    }
                }
            }
        })
    }

    private fun observeCategories() {
        incomeViewModel.categories.observe(viewLifecycleOwner) {
            categoriesAdapter.differ.submitList(it)
            if(it.isEmpty()){
                binding.emptyRecyclerCategories.visibility=View.VISIBLE
            }else binding.emptyRecyclerCategories.visibility=View.GONE
        }
    }

    private fun setupRecyclerView() {
        binding.categoriesRecyclerView.apply {
            adapter = categoriesAdapter
            gridLayoutManager=GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            incomeViewModel.event.collect {
                when (it) {
                    is CategoryViewModel.Event.NavigateToEditCategoryScreen -> {
                        val action =
                            IncomeFragmentDirections.actionIncomeFragmentToAddEditCategoryFragment(
                                categoryAndMoney = it.categoryAndMoney,
                                type = CategoryType.INCOME
                            )
                        findNavController().navigate(action)
                    }
                    is CategoryViewModel.Event.NavigateToOperationsScreen -> {
                        val action =
                            IncomeFragmentDirections.actionIncomeFragmentToCategoryOperationsFragment(
                                categoryAndMoney = it.categoryAndMoney,
                                month = it.month,
                                year = it.year
                            )
                        findNavController().navigate(action)
                    }
                    is CategoryViewModel.Event.NavigateToAddCategoryScreen -> {
                        val action =
                            IncomeFragmentDirections.actionIncomeFragmentToAddEditCategoryFragment(
                                month = it.month,
                                year = it.year,
                                type = CategoryType.INCOME
                            )
                        findNavController().navigate(action)
                    }
                    is CategoryViewModel.Event.ShowDatePicker -> {
                        MonthPickerDialog.Builder(
                            requireContext(),
                            { selectedMonth, selectedYear ->
                                incomeViewModel.setMonthAndYear(selectedMonth + 1, selectedYear)
                                val calendar = Calendar.getInstance()
                                calendar.set(Calendar.YEAR, selectedYear)
                                calendar.set(Calendar.MONTH, selectedMonth)
                                val curDate = "${
                                    calendar.getDisplayName(
                                        Calendar.MONTH,
                                        Calendar.LONG_STANDALONE,
                                        Locale.getDefault()
                                    )
                                } " + calendar.get(Calendar.YEAR)
                                binding.date.text = curDate
                            }, incomeViewModel.year.value!!, incomeViewModel.month.value!!-1
                        ).setMinYear(2010)
                            .setActivatedYear(incomeViewModel.year.value!!)
                            .setActivatedMonth(incomeViewModel.month.value!! - 1)
                            .setMaxYear(2030)
                            .setMinMonth(Calendar.JANUARY)
                            .setTitle("Выберите месяц и год")
                            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                            .build()
                            .show()
                    }
                }
            }
        }
    }

    override fun onCategoryItemClick(category: CategoryAndMoney) {
        incomeViewModel.onCategoryClick(category)
    }

    override fun onCategoryItemLongClick(category: CategoryAndMoney) {
        incomeViewModel.onCategoryLongClick(category, CategoryType.INCOME)
    }
}