package com.example.financeapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.example.financeapp.data.InitialDataTest.accounts
import com.example.financeapp.data.InitialDataTest.categories
import com.example.financeapp.data.InitialDataTest.categoryAndMoney
import com.example.financeapp.data.InitialDataTest.money
import com.example.financeapp.data.InitialDataTest.operationAndCategoryAndAccount
import com.example.financeapp.data.InitialDataTest.operations
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeFinanceRepository : FinanceRepository {

    private val accountItems = accounts
    private val operationItems = operations
    private val categoryItems = categories
    private val moneyItems = money

    private val observableAccounts = MutableLiveData<List<Account>>(accountItems)
    private val observableCategoryAndMoney = MutableLiveData<List<CategoryAndMoney>>(categoryAndMoney)
    private val observableOperationAndCategoryAndAccount =
        MutableLiveData<List<OperationAndCategoryAndAccount>>(operationAndCategoryAndAccount)
    private val observableCategories = MutableLiveData<List<Category>>(categoryItems)
    private val observableOperations=MutableLiveData<List<Operation>>(operationItems)
    private val observableMoney=MutableLiveData<List<Money>>(moneyItems)

    override fun getAccounts(): Flow<List<Account>> {
        return observableAccounts.asFlow()
    }

    fun getMoney():Flow<List<Money>>{
        return observableMoney.asFlow()
    }

    override fun getCategoriesByTypeAndMonthYear(
        type: CategoryType,
        month: Int,
        year: Int
    ): Flow<List<CategoryAndMoney>> {
        return observableCategoryAndMoney.asFlow()
    }

    override fun getOperations(): Flow<List<OperationAndCategoryAndAccount>> {
        return observableOperationAndCategoryAndAccount.asFlow()
    }

    fun getOperationsTest():Flow<List<Operation>>{
        return observableOperations.asFlow()
    }

    fun getCategories():Flow<List<Category>>{
        return observableCategories.asFlow()
    }

    override suspend fun deleteAccount(account: Account) {
        accountItems.remove(account)
        observableAccounts.postValue(accountItems)
    }

    override suspend fun insertAccount(account: Account) {
        accountItems.add(account)
        observableAccounts.postValue(accountItems)
    }

    override suspend fun updateAccount(account: Account) {
        accountItems[accountItems.indexOf(accountItems.find{it.accId==account.accId})] = account
        observableAccounts.postValue(accountItems)
    }

    override suspend fun updateAccountSub(money: Double, accId: Int) {
        accountItems[accountItems.indexOf(accountItems.find{it.accId==accId})].money-=money
        observableAccounts.postValue(accountItems)
    }

    override suspend fun updateAccountSum(money: Double, accId: Int) {
        accountItems[accountItems.indexOf(accountItems.find{it.accId==accId})].money+=money
        observableAccounts.postValue(accountItems)
    }

    override suspend fun deleteCategoryById(categoryId: Int) {
        categoryItems.remove(categoryItems.find { it.categoryId==categoryId })
        observableCategories.postValue(categoryItems)
    }

    override suspend fun insertCategory(category: Category): Long {
        categoryItems.add(category)
        observableCategories.postValue(categoryItems)
        return 0
    }

    override suspend fun updateCategoryById(categoryId: Int, categoryName: String, icon: String) {
        categoryItems[categoryItems.indexOf(categoryItems.find{it.categoryId==categoryId})].categoryName=categoryName
        categoryItems[categoryItems.indexOf(categoryItems.find{it.categoryId==categoryId})].icon=icon
        observableCategories.postValue(categoryItems)
    }

    override suspend fun deleteOperation(operationId: Int) {
        operationItems.remove(operationItems.find{it.id==operationId})
        observableOperations.postValue(operationItems)
    }

    override suspend fun insertOperation(operation: Operation) {
        operationItems.add(operation)
        observableOperations.postValue(operationItems)
    }

    override suspend fun updateOperation(operation: Operation) {
        operationItems[operationItems.indexOf(operationItems.find{it.id==operation.id})] = operation
        observableOperations.postValue(operationItems)
    }

    override fun getOperationsByAccountId(accountId: Int): Flow<List<OperationAndCategoryAndAccount>> {
        return observableOperationAndCategoryAndAccount.map{list->
            list.filter{
                it.accountId==accountId
            }
        }.asFlow()
    }

    override fun getOperationsByCategoryIdAndMoneyId(
        categoryId: Int,
        moneyId: Int
    ): Flow<List<OperationAndCategoryAndAccount>> {
        return observableOperationAndCategoryAndAccount.asFlow()
    }

    override suspend fun insertMoney(money: Money) {
        moneyItems.add(money)
        observableMoney.postValue(moneyItems)
    }

    override suspend fun updateMoneyPlan(moneyId: Int, plan: Double?) {
        moneyItems[moneyItems.indexOf(moneyItems.find{it.moneyId==moneyId})].plan=plan
        observableMoney.postValue(moneyItems)
    }

    override suspend fun updateMoneySum(moneyId: Int, money: Double) {
        moneyItems[moneyItems.indexOf(moneyItems.find{it.moneyId==moneyId})].moneyAmount+=money
        observableMoney.postValue(moneyItems)
    }

    override suspend fun updateMoneySub(moneyId: Int, money: Double) {
        moneyItems[moneyItems.indexOf(moneyItems.find{it.moneyId==moneyId})].moneyAmount-=money
        observableMoney.postValue(moneyItems)
    }

    override suspend fun deleteOperationsByAccountId(accountId: Int) {
        operationItems.removeAll(operationItems.filter{it.accountId==accountId})
        observableOperations.postValue(operationItems)
    }

    override suspend fun deleteOperationsByCategoryId(categoryId: Int) {
        operationItems.removeAll(operationItems.filter{it.categoryId==categoryId})
        observableOperations.postValue(operationItems)
    }

    override suspend fun deleteMoneyByCategoryId(categoryId: Int) {
        moneyItems.removeAll(moneyItems.filter{it.categoryId==categoryId})
        observableMoney.postValue(moneyItems)
    }
}