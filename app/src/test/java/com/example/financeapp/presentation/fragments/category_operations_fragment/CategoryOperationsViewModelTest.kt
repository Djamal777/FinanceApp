package com.example.financeapp.presentation.fragments.category_operations_fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.financeapp.MainCoroutineRule
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.repository.FakeFinanceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryOperationsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CategoryOperationsViewModel
    private lateinit var repository: FakeFinanceRepository

    @Before
    fun setup() {
        repository = FakeFinanceRepository()
        viewModel = CategoryOperationsViewModel(
            SavedStateHandle(
                mapOf(
                    "categoryAndMoney" to CategoryAndMoney(
                        categoryId = 1,
                        type = CategoryType.INCOME
                    ),
                    "month" to 1,
                    "year" to 2000
                )
            ),
            repository
        )
    }

    @Test
    fun `initializing viewModel, sets operation items`() {
        val value = viewModel.operations.getOrAwaitValueTest()
        assertThat(value).hasSize(3)
    }

    @Test
    fun `delete operation`() {
        viewModel.deleteOperation(1)
        val value = repository.getOperationsTest().asLiveData().getOrAwaitValueTest()
        assertThat(value).doesNotContain(Operation(id=1, date=0))
    }

    @Test
    fun `update account money, expense type category, sum`() {
        val viewModelTest=CategoryOperationsViewModel(
            SavedStateHandle(
                mapOf(
                    "categoryAndMoney" to CategoryAndMoney(
                        categoryId = 1,
                        type = CategoryType.EXPENSE
                    ),
                    "month" to 1,
                    "year" to 2000
                )
            ),
            repository
        )
        viewModelTest.updateAccMoney(2.0, 0)
        val value = repository.getAccounts().asLiveData().getOrAwaitValueTest().find{it.accId==0}
        assertThat(value?.money).isEqualTo(2.0)
    }

    @Test
    fun `update account money, income type category, sub`() {
        viewModel.updateAccMoney(3.0, 1)
        val value = repository.getAccounts().asLiveData().getOrAwaitValueTest().find{it.accId==1}
        assertThat(value?.money).isEqualTo(-3.0)
    }

    @Test
    fun `update category money, expense type category, sub`() {
        val viewModelTest=CategoryOperationsViewModel(
            SavedStateHandle(
                mapOf(
                    "categoryAndMoney" to CategoryAndMoney(
                        categoryId = 1,
                        type = CategoryType.EXPENSE
                    ),
                    "month" to 1,
                    "year" to 2000
                )
            ),
            repository
        )
        viewModelTest.updateCategoryMoney(2.0, 1)
        val value = repository.getMoney().asLiveData().getOrAwaitValueTest().find{it.moneyId==1}
        assertThat(value?.moneyAmount).isEqualTo(-2.0)
    }

    @Test
    fun `update category money, income type category, sum`() {
        viewModel.updateCategoryMoney(3.0, 2)
        val value = repository.getMoney().asLiveData().getOrAwaitValueTest().find{it.moneyId==2}
        assertThat(value?.moneyAmount).isEqualTo(3.0)
    }

    @Test
    fun `click on add, set NavigateToAddOperationScreen to eventChannel`(){
        viewModel.onAddClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryOperationsViewModel.Event.NavigateToAddOperationScreen::class.java)
    }

    @Test
    fun `click on back, set NavigateBack to eventChannel`(){
        viewModel.onBackClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryOperationsViewModel.Event.NavigateBack::class.java)
    }

    @Test
    fun `click on operation, set NavigateToEditOperationScreen to eventChannel`(){
        viewModel.onOperationClick(OperationAndCategoryAndAccount(date=0, categoryType = CategoryType.INCOME))
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryOperationsViewModel.Event.NavigateToEditOperationScreen::class.java)
    }
}