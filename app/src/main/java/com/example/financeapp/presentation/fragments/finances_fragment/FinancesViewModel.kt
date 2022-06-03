package com.example.financeapp.presentation.fragments.finances_fragment

import android.util.Log
import androidx.lifecycle.*
import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancesViewModel @Inject constructor(
    private val repository: FinanceRepository
): ViewModel() {

    private val _accounts=repository.getAccounts()
    val accounts=_accounts.asLiveData()

    private val accountId=MutableLiveData(-1)

    private val _operations=accountId.asFlow().flatMapLatest {
        if(it==-1){
            repository.getOperations()
        }else{
            repository.getOperationsByAccountId(it)
        }
    }
    val operations=_operations.asLiveData()

    val overallMoney=_accounts.map { accounts->
        accounts.map{ account->
            account.money
        }.sum()
    }.asLiveData()

    private val eventChannel= Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun setAccountId(accId:Int){
        accountId.value=accId
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