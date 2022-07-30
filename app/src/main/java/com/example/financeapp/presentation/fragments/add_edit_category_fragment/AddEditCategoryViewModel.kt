package com.example.financeapp.presentation.fragments.add_edit_category_fragment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    state: SavedStateHandle,
    private val repository: FinanceRepository
) : ViewModel() {

    val categoryAndMoney: CategoryAndMoney? = state["categoryAndMoney"]
    val type: CategoryType? = state["type"]
    val month: Int? = state["month"]
    val year: Int? = state["year"]
    var tag:String?=categoryAndMoney?.icon?:"com.example.financeapp:drawable/ic_baseline_shopping_basket_24"

    private val eventChannel = Channel<AddEditCategoryViewModel.Event>()
    val event = eventChannel.receiveAsFlow()

    fun insertCategory(category: Category, plan: Double?) = GlobalScope.launch {
        val categoryId = repository.insertCategory(category).toInt()
        insertMoney(
            Money(
                plan = plan,
                month = month!!,
                year = year!!,
                categoryId = categoryId
            )
        )
    }

    fun updateCategory(categoryId: Int, categoryName: String, icon: String) =
        viewModelScope.launch {
            repository.updateCategoryById(categoryId, categoryName, icon)
        }

    fun deleteCategory(categoryId: Int) = viewModelScope.launch {
        repository.deleteCategoryById(categoryId)
    }

    fun deleteOperationsByCategory(categoryId: Int) = viewModelScope.launch {
        repository.deleteOperationsByCategoryId(categoryId)
    }

    fun deleteMoney(categoryId: Int) = viewModelScope.launch {
        repository.deleteMoneyByCategoryId(categoryId)
    }

    fun insertMoney(money: Money) = GlobalScope.launch {
        repository.insertMoney(money)
    }

    fun updateMoney(moneyId: Int, plan: Double?) = viewModelScope.launch {
        repository.updateMoneyPlan(moneyId, plan)
    }

    fun onConfirmClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateBackAfterAdding)
    }

    fun onDeleteClick() = viewModelScope.launch {
        eventChannel.send(Event.ShowConfirmationDialog)
    }

    fun onIconClick() = viewModelScope.launch {
        eventChannel.send(Event.ShowIconsDialog)
    }

    fun onBackClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateBack)
    }

    sealed class Event {
        object ShowIconsDialog : Event()
        object NavigateBack : Event()
        object NavigateBackAfterAdding : Event()
        object ShowConfirmationDialog : Event()
    }
}