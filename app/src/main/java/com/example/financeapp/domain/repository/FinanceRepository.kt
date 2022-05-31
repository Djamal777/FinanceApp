package com.example.financeapp.domain.repository

import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {

    fun getAccounts(): Flow<List<Account>>

    fun getCategoriesByTypeAndMonthYear(
        type: CategoryType,
        month: Int,
        year: Int
    ): Flow<List<CategoryAndMoney>>

    fun getOperations(): Flow<List<OperationAndCategoryAndAccount>>

    suspend fun deleteAccount(account: Account)

    suspend fun insertAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun deleteCategoryById(categoryId: Int)

    suspend fun insertCategory(category: Category)

    suspend fun updateCategoryById(categoryId: Int, categoryName: String, icon: Int)

    suspend fun deleteOperation(operation: Operation)

    suspend fun insertOperation(operation: Operation)

    suspend fun updateOperation(operation: Operation)

    fun getOperationsByAccountId(accountId: Int): Flow<List<OperationAndCategoryAndAccount>>

    fun getOperationsByCategoryIdAndMoneyId(
        categoryId: Int,
        moneyId: Int
    ): Flow<List<OperationAndCategoryAndAccount>>

    suspend fun insertMoney(money: Money)

    suspend fun updateMoneyPlan(moneyId: Int, plan: Double)

    suspend fun updateMoneySum(moneyId: Int, money: Int)

    suspend fun updateMoneySub(moneyId: Int, money: Int)

    suspend fun deleteOperationsByAccountId(accountId: Int)

    suspend fun deleteOperationsByCategoryId(categoryId: Int)

    suspend fun deleteMoneyByCategoryId(categoryId: Int)
}