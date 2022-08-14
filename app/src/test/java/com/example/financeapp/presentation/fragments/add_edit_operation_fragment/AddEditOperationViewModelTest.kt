package com.example.financeapp.presentation.fragments.add_edit_operation_fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.financeapp.MainCoroutineRule
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.presentation.fragments.category_operations_fragment.CategoryOperationsViewModel
import com.example.financeapp.repository.FakeFinanceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runners.Parameterized

@ExperimentalCoroutinesApi
class AddEditOperationViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddEditOperationViewModel
    private lateinit var repository: FakeFinanceRepository

    @Before
    fun setup() {
        repository = FakeFinanceRepository()
        viewModel = AddEditOperationViewModel(
            SavedStateHandle(
                mapOf(
                    "operation" to OperationAndCategoryAndAccount(
                        id = 1,
                        categoryType = CategoryType.EXPENSE,
                        date=0
                    ),
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
    }

    @Test
    fun `initializing viewModel, sets accounts`(){
        val value=viewModel.accounts.getOrAwaitValueTest()
        assertThat(value).hasSize(4)
    }

    @Test
    fun `insert operation`(){
        viewModel.insertOperation(Operation(id=4, date=0))
        val value=repository.getOperationsTest().asLiveData().getOrAwaitValueTest()
        assertThat(value).hasSize(4)
    }

    @Test
    fun `update operation`(){
        viewModel.updateOperation(Operation(id=1, date=2020))
        val value=repository.getOperationsTest().asLiveData().getOrAwaitValueTest().find{it.id==1}
        assertThat(value?.date).isEqualTo(2020)
    }

    @Test
    fun `delete operation`(){
        viewModel.deleteOperation(4)
        val value=repository.getOperationsTest().asLiveData().getOrAwaitValueTest()
        assertThat(value).hasSize(3)
    }

    @Test
    fun `update account money after insertion, expense category type, sub`(){
        viewModel.updateAccMoneyAfterInsertion(2.0,0)
        val value=repository.getAccounts().asLiveData().getOrAwaitValueTest().find{it.accId==0}
        assertThat(value?.money).isEqualTo(-2.0)
    }

    @Test
    fun `update account money after insertion, income category type, sum`(){
        val viewModelTest = AddEditOperationViewModel(
            SavedStateHandle(
                mapOf(
                    "operation" to OperationAndCategoryAndAccount(
                        id = 1,
                        categoryType = CategoryType.INCOME,
                        date=0
                    ),
                    "categoryAndMoney" to CategoryAndMoney(
                        categoryId = 1,
                        type = CategoryType.INCOME
                    )
                )
            ),
            repository
        )
        viewModelTest.updateAccMoneyAfterInsertion(3.0,1)
        val value=viewModelTest.accounts.getOrAwaitValueTest().find{it.accId==1}
        assertThat(value?.money).isEqualTo(3.0)
    }

    @Test
    fun `update category money after insertion, sum`(){
        viewModel.updateCategoryMoneyAfterInsertion(2.0, 1)
        val value=repository.getMoney().asLiveData().getOrAwaitValueTest().find{it.moneyId==1}
        assertThat(value?.moneyAmount).isEqualTo(2.0)
    }

    @Test
    fun `update account money after updating, expense category type, sum-sub`()= runTest{
        val job=viewModel.updateAccMoneyAfterUpdating(2.0,2)
        job.join()
        val value=viewModel.accounts.getOrAwaitValueTest().find{it.accId==2}
        assertThat(value?.money).isEqualTo(-2.0)
    }

    @Test
    fun `update account money after updating, income category type, sub-sum`()=runTest{
        val viewModelTest = AddEditOperationViewModel(
            SavedStateHandle(
                mapOf(
                    "operation" to OperationAndCategoryAndAccount(
                        id = 1,
                        categoryType = CategoryType.INCOME,
                        date=0
                    ),
                    "categoryAndMoney" to CategoryAndMoney(
                        categoryId = 1,
                        type = CategoryType.INCOME
                    )
                )
            ),
            repository
        )
        val job=viewModelTest.updateAccMoneyAfterUpdating(2.0,3)
        job.join()
        val value=viewModelTest.accounts.getOrAwaitValueTest().find{it.accId==3}
        assertThat(value?.money).isEqualTo(2.0)
    }

    @Test
    fun `update category money after updating, sub-sum`()= runTest{
        val job=viewModel.updateCategoryMoneyAfterUpdating(2.0,2)
        job.join()
        val value=repository.getMoney().asLiveData().getOrAwaitValueTest().find{it.moneyId==2}
        assertThat(value?.moneyAmount).isEqualTo(2.0)
    }

    @Test
    fun `click on confirm, sets NavigateBackAfterAdding to eventChannel`(){
        viewModel.onConfirmClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditOperationViewModel.Event.NavigateBackAfterAdding::class.java)
    }

    @Test
    fun `click on back, sets NavigateBack to eventChannel`(){
        viewModel.onBackClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditOperationViewModel.Event.NavigateBack::class.java)
    }

    @Test
    fun `click on date, sets ShowDatePicker to eventChannel`(){
        viewModel.onDateClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditOperationViewModel.Event.ShowDatePicker::class.java)
    }
}