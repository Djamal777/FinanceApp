package com.example.financeapp.presentation.fragments.add_edit_account_fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.financeapp.MainCoroutineRule
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.repository.FakeFinanceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditAccountViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddEditAccountViewModel
    private lateinit var repository: FakeFinanceRepository

    @Before
    fun setup() {
        repository = FakeFinanceRepository()
        viewModel = AddEditAccountViewModel(
            SavedStateHandle(
                mapOf(
                    "account" to Account(),
                    "canBeDeleted" to false
                )
            ),
            repository
        )
    }

    @Test
    fun `insert account`() {
        viewModel.insertAccount(Account(accId = 2, accName = "долг"))
        val value = repository.getAccounts().asLiveData().getOrAwaitValueTest()
        assertThat(value).contains(Account(accId = 2, accName = "долг"))
    }

    @Test
    fun `update account`() {
        viewModel.updateAccount(Account(accId = 2, accName = "кар"))
        val value = repository.getAccounts().asLiveData().getOrAwaitValueTest()
        assertThat(value).contains(Account(accId = 2, accName = "кар"))
    }

    @Test
    fun `delete account`() {
        viewModel.deleteAccount(Account(accId = 2, accName = "кар"))
        val value = repository.getAccounts().asLiveData().getOrAwaitValueTest()
        assertThat(value).doesNotContain(Account(accId = 2, accName = "кар"))
    }

    @Test
    fun `delete operations by account`() {
        viewModel.deleteOperationsByAccount(Account(accId = 1))
        val value = repository.getOperationsTest().asLiveData().getOrAwaitValueTest()
        assertThat(value).doesNotContain(Operation(id = 2, date = 0, accountId = 1))
    }

    @Test
    fun `click on confirm, sets NavigateBackAfterAdding on eventChannel`() {
        viewModel.onConfirmClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditAccountViewModel.Event.NavigateBackAfterAdding::class.java)
    }

    @Test
    fun `click on delete, sets ShowConfirmationDialog on eventChannel`() {
        viewModel.onDeleteClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditAccountViewModel.Event.ShowConfirmationDialog::class.java)
    }

    @Test
    fun `click on icon, sets ShowIconsDialog on eventChannel`() {
        viewModel.onIconClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditAccountViewModel.Event.ShowIconsDialog::class.java)
    }

    @Test
    fun `click on back, sets NavigateBack on eventChannel`() {
        viewModel.onBackClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditAccountViewModel.Event.NavigateBack::class.java)
    }
}