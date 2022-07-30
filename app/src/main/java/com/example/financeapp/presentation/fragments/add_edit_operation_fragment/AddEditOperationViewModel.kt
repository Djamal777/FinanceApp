package com.example.financeapp.presentation.fragments.add_edit_operation_fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditOperationViewModel @Inject constructor(
    state: SavedStateHandle,
    private val repository: FinanceRepository
) : ViewModel() {

    val operation: OperationAndCategoryAndAccount? = state["operation"]
    val categoryAndMoney: CategoryAndMoney? = state["categoryAndMoney"]
    val month: Int? = state["month"]
    val year: Int? = state["year"]
    var selection:Long=0

    val accounts = repository.getAccounts().asLiveData()

    private val eventChannel = Channel<AddEditOperationViewModel.Event>()
    val event = eventChannel.receiveAsFlow()

    fun insertOperation(operation: Operation) = viewModelScope.launch {
        repository.insertOperation(operation)
    }

    fun updateOperation(operation: Operation) = viewModelScope.launch {
        repository.updateOperation(operation)
    }

    fun deleteOperation(operationId: Int) = viewModelScope.launch {
        repository.deleteOperation(operationId)
    }

    fun updateAccMoneyAfterInsertion(money: Double, accId: Int) = viewModelScope.launch {
        if (categoryAndMoney?.type == CategoryType.INCOME) {
            repository.updateAccountSum(money, accId)
        } else repository.updateAccountSub(money, accId)
    }

    fun updateCategoryMoneyAfterInsertion(money: Double, moneyId: Int) = viewModelScope.launch {
        repository.updateMoneySum(moneyId, money)
    }

    fun updateAccMoneyAfterUpdating(money: Double, accId: Int) = GlobalScope.launch {
        if (categoryAndMoney?.type == CategoryType.INCOME) {
            repository.updateAccountSub(operation!!.money, operation.accountId)
            repository.updateAccountSum(money, accId)
        } else {
            repository.updateAccountSum(operation!!.money, operation.accountId)
            repository.updateAccountSub(money, accId)
        }
    }

    fun updateCategoryMoneyAfterUpdating(money: Double, moneyId: Int) = GlobalScope.launch {
        repository.updateMoneySub(moneyId, operation!!.money)
        repository.updateMoneySum(moneyId, money)
    }

    fun onConfirmClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateBackAfterAdding)
    }

    fun onBackClick() = viewModelScope.launch {
        eventChannel.send(Event.NavigateBack)
    }

    fun onDateClick() = viewModelScope.launch {
        eventChannel.send(Event.ShowDatePicker)
    }

    sealed class Event {
        object ShowDatePicker : Event()
        object NavigateBack : Event()
        object NavigateBackAfterAdding : Event()
    }
}