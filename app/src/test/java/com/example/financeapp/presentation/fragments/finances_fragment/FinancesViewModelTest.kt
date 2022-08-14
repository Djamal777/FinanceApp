package com.example.financeapp.presentation.fragments.finances_fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.financeapp.MainCoroutineRule
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.repository.FakeFinanceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FinancesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FinancesViewModel

    @Before
    fun setup() {
        viewModel = FinancesViewModel(FakeFinanceRepository())
    }

    @Test
    fun `set account id, sets account id`(){
        viewModel.setAccountId(1)
        val value=viewModel.accountId.getOrAwaitValueTest()
        assertThat(value).isEqualTo(1)
    }

    @Test
    fun `set account id, changes operation items`(){
        viewModel.setAccountId(1)
        val value=viewModel.operations.getOrAwaitValueTest()
        assertThat(value).hasSize(1)
    }

    @Test
    fun `initializing viewModel, sets operation items`(){
        val value=viewModel.operations.getOrAwaitValueTest()
        assertThat(value).hasSize(3)
    }

    @Test
    fun `long click on account, sets NavigateToEditAccountScreen event to eventChannel`(){
        val account=Account()
        viewModel.onAccountLongClick(account)
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(FinancesViewModel.Event.NavigateToEditAccountScreen::class.java)
    }

    @Test
    fun `click on fab, sets NavigateToAddAccountScreen event to eventChannel`(){
        viewModel.onFabAddClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(FinancesViewModel.Event.NavigateToAddAccountScreen::class.java)
    }
}