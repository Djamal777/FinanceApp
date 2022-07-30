package com.example.financeapp.presentation.fragments.add_edit_account_fragment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAccountViewModel @Inject constructor(
    state: SavedStateHandle,
    private val repository: FinanceRepository
) : ViewModel() {

    val account: Account? = state["account"]
    val canBeDeleted:Boolean? = state["canBeDeleted"]

    var tag:String?=account?.icon?:"com.example.financeapp:drawable/ic_baseline_wallet_24"

    private val eventChannel = Channel<AddEditAccountViewModel.Event>()
    val event = eventChannel.receiveAsFlow()

    fun insertAccount(account: Account) = viewModelScope.launch {
        repository.insertAccount(account)
    }

    fun updateAccount(account: Account) = viewModelScope.launch {
        repository.updateAccount(account)
    }

    fun deleteAccount(account: Account) = viewModelScope.launch {
        repository.deleteAccount(account)
    }

    fun deleteOperationsByAccount(account: Account) = viewModelScope.launch {
        repository.deleteOperationsByAccountId(account.accId)
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