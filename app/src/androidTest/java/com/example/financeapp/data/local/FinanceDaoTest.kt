package com.example.financeapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FinanceDaoTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    private lateinit var database: FinanceDatabase
    private lateinit var dao:FinanceDao

    @Before
    fun setup(){
        database= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FinanceDatabase::class.java
        ).allowMainThreadQueries().build()
        dao=database.financeDao()
    }

    @Test
    fun insertAccount()= runBlocking{
        val account=Account(accId = 1)
        dao.insertAccount(account)
        val allAccounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(allAccounts).contains(account)
    }

    @Test
    fun insertCategory()= runBlocking{
        val category=Category(categoryId = 1,type=CategoryType.INCOME)
        dao.insertCategory(category)
        val allCategories=dao.getCategories().asLiveData().getOrAwaitValue()
        assertThat(allCategories).contains(category)
    }

    @Test
    fun insertOperation()= runBlocking{
        val operation=Operation(id=1,date=1)
        dao.insertOperation(operation)
        val allOperations=dao.getOperationsTest().asLiveData().getOrAwaitValue()
        assertThat(allOperations).contains(operation)
    }

    @Test
    fun insertMoney()= runBlocking{
        val money= Money(moneyId = 1)
        dao.insertMoney(money)
        val allMoney=dao.getMoney().asLiveData().getOrAwaitValue()
        assertThat(allMoney).contains(money)
    }

    @Test
    fun getCategoriesByTypeAndMonthYear(): Unit = runBlocking {
        val category1=Category(categoryId = 1, type=CategoryType.INCOME)
        val category2=Category(categoryId = 2, type=CategoryType.EXPENSE)
        val money1=Money(moneyId = 1,month=2, year = 2022, categoryId = 1)
        val money2=Money(moneyId = 2,month=3, year = 2021, categoryId = 2)
        val item=CategoryAndMoney(categoryId = 1, moneyId = 1, type = CategoryType.INCOME)
        dao.insertMoney(money1)
        dao.insertMoney(money2)
        dao.insertCategory(category1)
        dao.insertCategory(category2)
        val categories=dao.getCategoriesByTypeAndMonthYear(CategoryType.INCOME,2,2022).asLiveData().getOrAwaitValue()
        assertThat(categories).containsExactly(item)
    }

    @Test
    fun getOperations()= runBlocking {
        val operation=Operation(id=1, categoryId = 1, accountId = 1, date=30)
        val category=Category(categoryId = 1, type = CategoryType.INCOME)
        val account=Account(accId=1)
        dao.insertCategory(category)
        dao.insertAccount(account)
        dao.insertOperation(operation)
        val item=OperationAndCategoryAndAccount(id=1, date=30, categoryType = CategoryType.INCOME, accountId = 1)
        val operations=dao.getOperations().asLiveData().getOrAwaitValue()
        assertThat(operations).contains(item)
    }

    @Test
    fun deleteAccount()= runBlocking {
        val account=Account()
        dao.insertAccount(account)
        dao.deleteAccount(account)
        val accounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(accounts).doesNotContain(account)
    }

    @Test
    fun updateAccount()= runBlocking {
        val account=Account(accId = 1)
        dao.insertAccount(account)
        account.selected=true
        dao.updateAccount(account)
        val accounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(accounts).contains(account)
    }

    @Test
    fun updateAccountSub()= runBlocking {
        val account=Account(accId = 1, money = 40.0)
        dao.insertAccount(account)
        dao.updateAccountSub(20.0,1)
        account.money-=20.0
        val accounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(accounts).contains(account)
    }

    @Test
    fun updateAccountSum()= runBlocking {
        val account=Account(accId = 1, money = 40.0)
        dao.insertAccount(account)
        dao.updateAccountSum(20.0,1)
        account.money+=20.0
        val accounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(accounts).contains(account)
    }

    @Test
    fun deleteCategoryById()= runBlocking {
        val category=Category(categoryId = 1, type=CategoryType.INCOME)
        dao.insertCategory(category)
        dao.deleteCategoryById(1)
        val accounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(accounts).doesNotContain(category)
    }

    @Test
    fun updateCategoryById()= runBlocking {
        val category=Category(categoryId = 1, type = CategoryType.INCOME)
        dao.insertCategory(category)
        category.categoryName="дом"
        dao.updateCategoryById(1,"дом","")
        val categories=dao.getCategories().asLiveData().getOrAwaitValue()
        assertThat(categories).contains(category)
    }

    @Test
    fun deleteOperation()= runBlocking {
        val operation=Operation(id = 1, date=45)
        dao.insertOperation(operation)
        dao.deleteOperation(1)
        val operations=dao.getOperationsTest().asLiveData().getOrAwaitValue()
        assertThat(operations).doesNotContain(operation)
    }

    @Test
    fun updateOperation()= runBlocking {
        val operation=Operation(id = 1, date=49)
        dao.insertOperation(operation)
        operation.money=40.0
        dao.updateOperation(operation)
        val operations=dao.getOperationsTest().asLiveData().getOrAwaitValue()
        assertThat(operations).contains(operation)
    }

    @Test
    fun getOperationsByAccountId(): Unit = runBlocking {
        val operation1=Operation(id=1, categoryId = 1, accountId = 1, date=30)
        val category=Category(categoryId = 1, type = CategoryType.INCOME)
        val account1=Account(accId=1)
        val operation2=Operation(id=2, categoryId = 1, accountId = 2, date=30)
        val account2=Account(accId=2)
        dao.insertCategory(category)
        dao.insertAccount(account1)
        dao.insertAccount(account2)
        dao.insertOperation(operation1)
        dao.insertOperation(operation2)
        val item=OperationAndCategoryAndAccount(id=1, date=30, categoryType = CategoryType.INCOME, accountId = 1)
        val operations=dao.getOperationsByAccountId(1).asLiveData().getOrAwaitValue()
        assertThat(operations).containsExactly(item)
    }

    @Test
    fun getOperationsByCategoryIdAndMoneyId(): Unit = runBlocking {
        val operation1=Operation(id=1, categoryId = 1, accountId = 1, date=30, moneyId = 1)
        val category1=Category(categoryId = 1, categoryName="дом", type = CategoryType.INCOME)
        val money1=Money(moneyId = 1, categoryId = 1)
        val account=Account(accId=1)
        val operation2=Operation(id=2, categoryId = 1, accountId = 2, date=30, moneyId = 2)
        val category2=Category(categoryId = 2, categoryName="дом", type = CategoryType.INCOME)
        val money2=Money(moneyId = 2, categoryId = 2)
        dao.insertCategory(category1)
        dao.insertCategory(category2)
        dao.insertAccount(account)
        dao.insertMoney(money1)
        dao.insertMoney(money2)
        dao.insertOperation(operation1)
        dao.insertOperation(operation2)
        val item=OperationAndCategoryAndAccount(id=1, date=30, categoryType = CategoryType.INCOME, categoryName = "дом", accountId = 1)
        val operations=dao.getOperationsByCategoryIdAndMoneyId(1, 1).asLiveData().getOrAwaitValue()
        assertThat(operations).containsExactly(item)
    }

    @Test
    fun updateMoneyPlan()= runBlocking {
        val money=Money(moneyId = 1)
        dao.insertMoney(money)
        money.plan=5.0
        dao.updateMoneyPlan(1, 5.0)
        val allMoney=dao.getMoney().asLiveData().getOrAwaitValue()
        assertThat(allMoney).contains(money)
    }

    @Test
    fun updateMoneySum()= runBlocking {
        val money=Money(moneyId = 1)
        dao.insertMoney(money)
        money.moneyAmount=5.0
        dao.updateMoneySum(1, 5.0)
        val allMoney=dao.getMoney().asLiveData().getOrAwaitValue()
        assertThat(allMoney).contains(money)
    }

    @Test
    fun updateMoneySub()= runBlocking {
        val money=Money(moneyId = 1)
        dao.insertMoney(money)
        money.moneyAmount=-5.0
        dao.updateMoneySub(1, 5.0)
        val allMoney=dao.getMoney().asLiveData().getOrAwaitValue()
        assertThat(allMoney).contains(money)
    }

    @Test
    fun deleteOperationsByAccountId()= runBlocking {
        val operation1=Operation(1, accountId = 1, date=40)
        val operation2=Operation(1, accountId = 2, date=40)
        dao.insertOperation(operation1)
        dao.insertOperation(operation2)
        dao.deleteOperationsByAccountId(1)
        val operations=dao.getOperationsTest().asLiveData().getOrAwaitValue()
        assertThat(operations).doesNotContain(operation1)
    }

    @Test
    fun deleteOperationsByCategoryId()= runBlocking {
        val operation1=Operation(1, categoryId = 1, date=40)
        val operation2=Operation(1, categoryId = 2, date=40)
        dao.insertOperation(operation1)
        dao.insertOperation(operation2)
        dao.deleteOperationsByCategoryId(1)
        val operations=dao.getOperationsTest().asLiveData().getOrAwaitValue()
        assertThat(operations).doesNotContain(operation1)
    }

    @Test
    fun deleteMoneyByCategoryId()= runBlocking {
        val money1=Money(1, categoryId = 1)
        val money2=Money(1, categoryId = 2)
        dao.insertMoney(money1)
        dao.insertMoney(money2)
        dao.deleteMoneyByCategoryId(1)
        val allMoney=dao.getMoney().asLiveData().getOrAwaitValue()
        assertThat(allMoney).doesNotContain(money1)
    }

    @After
    fun teardown(){
        database.close()
    }
}