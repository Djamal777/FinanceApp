package com.example.financeapp.presentation.fragments.expenses_and_income_fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.financeapp.MainCoroutineRule
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.repository.FakeFinanceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CategoryViewModel

    @Before
    fun setup() {
        viewModel = CategoryViewModel(CategoryType.INCOME, FakeFinanceRepository())
    }

    @Test
    fun `initializing viewModel, sets category items`(){
        val value=viewModel.categories.getOrAwaitValueTest()
        assertThat(value).hasSize(3)
    }

    @Test
    fun `click on category, sets NavigateToOperationsScreen event to eventChannel`(){
        viewModel.onCategoryClick(CategoryAndMoney(type=CategoryType.EXPENSE))
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryViewModel.Event.NavigateToOperationsScreen::class.java)
    }

    @Test
    fun `long click on category, sets NavigateToEditCategoryScreen event to eventChannel`(){
        viewModel.onCategoryLongClick(CategoryAndMoney(type=CategoryType.EXPENSE), CategoryType.EXPENSE)
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryViewModel.Event.NavigateToEditCategoryScreen::class.java)
    }

    @Test
    fun `click on addCategory, sets NavigateToAddCategoryScreen event to eventChannel`(){
        viewModel.onAddCategoryClick(1,2000,CategoryType.EXPENSE)
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryViewModel.Event.NavigateToAddCategoryScreen::class.java)
    }

    @Test
    fun `set month and year, sets month and year`(){
        viewModel.setMonthAndYear(1,2000)
        assertThat(viewModel.month.getOrAwaitValueTest()).isEqualTo(1)
        assertThat(viewModel.year.getOrAwaitValueTest()).isEqualTo(2000)
    }

    @Test
    fun `click on date, sets ShowDatePicker event to eventChannel`(){
        viewModel.onDateClick()
        val value=viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(CategoryViewModel.Event.ShowDatePicker::class.java)
    }
}