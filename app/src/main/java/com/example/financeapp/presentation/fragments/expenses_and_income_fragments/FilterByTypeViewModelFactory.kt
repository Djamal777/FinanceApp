package com.example.financeapp.presentation.fragments.expenses_and_income_fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.domain.repository.FinanceRepository

class FilterByTypeViewModelFactory(
    private val repository: FinanceRepository,
    private val type: CategoryType
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(type, repository) as T
    }
}