package com.example.financeapp.presentation.fragments.finances_fragment

import androidx.lifecycle.*
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancesViewModel @Inject constructor(
    private val repository: FinanceRepository
): ViewModel() {

    private val _accounts:MutableLiveData<List<Account>> = MutableLiveData()
    val accounts:LiveData<List<Account>> =_accounts

    private val _operations:MutableLiveData<List<OperationAndCategoryAndAccount>> =MutableLiveData()
    val operations:LiveData<List<OperationAndCategoryAndAccount>> =_operations

    private val _overallMoney:MutableLiveData<Double> =MutableLiveData()
    val overallMoney:LiveData<Double> =_overallMoney

    init{
        getAllAccounts()
        getAllOperations()
        getOverallMoney()
    }

    fun getOverallMoney()=viewModelScope.launch {
        _overallMoney.postValue(
            _accounts.value?.map { it.money }?.sum()
        )
    }

    private val eventChannel= Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun getAllAccounts()=viewModelScope.launch{
        _accounts.postValue(repository.getAccounts().asLiveData().value)
    }

    fun getAllOperations()=viewModelScope.launch {
        _operations.postValue(repository.getOperations().asLiveData().value)
    }

    fun getOperationsByAccId(accId:Int)=viewModelScope.launch {
        _operations.postValue(repository.getOperationsByAccountId(accId).asLiveData().value)
    }

    fun onAccountLongClick(account: Account)=viewModelScope.launch {
        eventChannel.send(Event.NavigateToEditAccountScreen(account))
    }

    fun onFabAddClick()=viewModelScope.launch{
        eventChannel.send(Event.NavigateToAddAccountScreen)
    }

    sealed class Event{
        data class NavigateToEditAccountScreen(val account: Account):Event()
        object NavigateToAddAccountScreen:Event()
    }
}