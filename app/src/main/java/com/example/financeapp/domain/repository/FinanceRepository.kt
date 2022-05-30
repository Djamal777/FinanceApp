package com.example.financeapp.domain.repository

import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.data.local.entities.relations.AccountWithOperations
import com.example.financeapp.data.local.entities.relations.CategoryWithMoney
import com.example.financeapp.data.local.entities.relations.CategoryWithOperations
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {

    fun getAccounts(): Flow<List<Account>>

    fun getCategoriesByTypeAndIds(type: CategoryType, ids:List<Int>): Flow<List<CategoryWithMoney>>

    fun getOperations(): Flow<List<OperationAndCategoryAndAccount>>

    fun getMoneyByMonthAndYear(month:Int, year:Int): Flow<List<Money>>

    suspend fun deleteAccount(account: Account)

    suspend fun insertAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun deleteCategory(category: Category)

    suspend fun insertCategory(category: Category):Long

    suspend fun updateCategory(category: Category)

    suspend fun deleteOperation(operation: Operation)

    suspend fun insertOperation(operation: Operation)

    suspend fun updateOperation(operation: Operation)

    fun getOperationsByAccountId(accountId:Int): Flow<List<OperationAndCategoryAndAccount>>

    fun getOperationsByCategoryId(categoryId:Int): Flow<List<CategoryWithOperations>>

    suspend fun insertMoney(money: Money)

    suspend fun updateMoney(money: Money)

    suspend fun deleteOperationsByAccountId(accountId:Int)

    suspend fun deleteOperationsByCategoryId(categoryId:Int)

    suspend fun deleteMoneyByCategoryId(categoryId:Int)

    fun getAccountById(accId:Int): Flow<Account>

    fun getMoneyByCategoryIdAndMonthAndYear(categoryId: Int, month: Int, year:Int): Flow<Money>
}