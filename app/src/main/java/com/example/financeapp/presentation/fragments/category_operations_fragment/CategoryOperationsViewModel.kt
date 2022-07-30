package com.example.financeapp.presentation.fragments.category_operations_fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryOperationsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val repository: FinanceRepository
) : ViewModel() {

    val categoryAndMoney: CategoryAndMoney? = state["categoryAndMoney"]
    val month: Int? = state["month"]
    val year: Int? = state["year"]

    val operations = repository.getOperationsByCategoryIdAndMoneyId(
        categoryAndMoney!!.categoryId,
        categoryAndMoney.moneyId
    ).asLiveData()

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun deleteOperation(operationId: Int) = viewModelScope.launch {
        repository.deleteOperation(operationId)
    }

    fun updateAccMoney(money: Double, accId: Int) = viewModelScope.launch {
        if (categoryAndMoney?.type == CategoryType.EXPENSE) {
            repository.updateAccountSum(money, accId)
        } else repository.updateAccountSub(money, accId)
    }

    fun updateCategoryMoney(money: Double, moneyId: Int) = viewModelScope.launch {
        if (categoryAndMoney?.type == CategoryType.EXPENSE) {
            repository.updateMoneySub(moneyId, money)
        } else repository.updateMoneySum(moneyId, money)
    }

    fun onAddClick() = viewModelScope.launch {
        eventChannel.send(
            Event.NavigateToAddOperationScreen(
                month!!,
                year!!,
                categoryAndMoney!!
            )
        )
    }

    fun onBackClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateBack)
    }

    fun onOperationClick(operation: OperationAndCategoryAndAccount) = viewModelScope.launch {
        eventChannel.send(
            Event.NavigateToEditOperationScreen(
                month!!,
                year!!,
                operation,
                categoryAndMoney!!
            )
        )
    }

    sealed class Event {
        object NavigateBack : Event()
        data class NavigateToAddOperationScreen(
            val month: Int,
            val year: Int,
            val categoryAndMoney: CategoryAndMoney
        ) : Event()

        data class NavigateToEditOperationScreen(
            val month: Int,
            val year: Int,
            val operation: OperationAndCategoryAndAccount,
            val categoryAndMoney: CategoryAndMoney
        ) : Event()
    }
}