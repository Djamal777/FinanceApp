package com.example.financeapp.presentation.fragments.add_edit_category_fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.example.financeapp.MainCoroutineRule
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.repository.FakeFinanceRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditCategoryViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddEditCategoryViewModel
    private lateinit var repository: FakeFinanceRepository

    @Before
    fun setup() {
        repository = FakeFinanceRepository()
        viewModel = AddEditCategoryViewModel(
            SavedStateHandle(
                mapOf(
                    "categoryAndMoney" to CategoryAndMoney(
                        categoryId = 1,
                        type = CategoryType.EXPENSE
                    ),
                    "type" to CategoryType.EXPENSE,
                    "month" to 1,
                    "year" to 2000
                )
            ),
            repository
        )
    }

    @Test
    fun `insert category`() = runTest {
        val job = viewModel.insertCategory(
            Category(categoryName = "дом", type = CategoryType.EXPENSE),
            plan = 0.0
        )
        job.join()
        val categories = repository.getCategories().asLiveData().getOrAwaitValueTest()
        val money = repository.getMoney().asLiveData().getOrAwaitValueTest()
        assertThat(categories).contains(Category(categoryName = "дом", type = CategoryType.EXPENSE))
        assertThat(money).hasSize(4)
    }

    @Test
    fun `update category`() {
        viewModel.updateCategory(1, "книги", "")
        val value = repository.getCategories().asLiveData().getOrAwaitValueTest()
        assertThat(value).contains(Category(1, "книги", CategoryType.EXPENSE))
    }

    @Test
    fun `delete category`() {
        viewModel.deleteCategory(2)
        val value = repository.getCategories().asLiveData().getOrAwaitValueTest()
        assertThat(value).doesNotContain(Category(2, type = CategoryType.EXPENSE))
    }

    @Test
    fun `delete operations by category`() {
        viewModel.deleteOperationsByCategory(1)
        val value = repository.getOperationsTest().asLiveData().getOrAwaitValueTest()
        assertThat(value).doesNotContain(Operation(id = 3, date = 0, categoryId = 1))
    }

    @Test
    fun `delete money by category`() {
        viewModel.deleteMoney(1)
        val value = repository.getMoney().asLiveData().getOrAwaitValueTest()
        assertThat(value).doesNotContain(Money(moneyId = 3, categoryId = 1))
    }

    @Test
    fun `update money`() {
        viewModel.updateMoney(1, 4.0)
        val value = repository.getMoney().asLiveData().getOrAwaitValueTest()
        assertThat(value).contains(Money(moneyId = 1, plan = 4.0))
    }

    @Test
    fun `click on confirm, sets NavigateBackAfterAdding on eventChannel`() {
        viewModel.onConfirmClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditCategoryViewModel.Event.NavigateBackAfterAdding::class.java)
    }

    @Test
    fun `click on delete, sets ShowConfirmationDialog on eventChannel`() {
        viewModel.onDeleteClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditCategoryViewModel.Event.ShowConfirmationDialog::class.java)
    }

    @Test
    fun `click on icon, sets ShowIconsDialog on eventChannel`() {
        viewModel.onIconClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditCategoryViewModel.Event.ShowIconsDialog::class.java)
    }

    @Test
    fun `click on back, sets NavigateBack on eventChannel`() {
        viewModel.onBackClick()
        val value = viewModel.event.asLiveData().getOrAwaitValueTest()
        assertThat(value).isInstanceOf(AddEditCategoryViewModel.Event.NavigateBack::class.java)
    }
}