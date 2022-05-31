package com.example.financeapp.data.repository

import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.FinanceDao
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val dao: FinanceDao
) : FinanceRepository {
    override fun getAccounts(): Flow<List<Account>> {
        return dao.getAccounts()
    }

    override fun getCategoriesByTypeAndMonthYear(
        type: CategoryType,
        month: Int,
        year: Int
    ): Flow<List<CategoryAndMoney>> {
        return dao.getCategoriesByTypeAndMonthYear(type, month, year)
    }

    override fun getOperations(): Flow<List<OperationAndCategoryAndAccount>> {
        return dao.getOperations()
    }

    override suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }

    override suspend fun insertAccount(account: Account) {
        dao.insertAccount(account)
    }

    override suspend fun updateAccount(account: Account) {
        dao.updateAccount(account)
    }

    override suspend fun deleteCategoryById(categoryId: Int) {
        dao.deleteCategoryById(categoryId)
    }

    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(category)
    }

    override suspend fun updateCategoryById(categoryId: Int, categoryName: String, icon: Int) {
        dao.updateCategoryById(categoryId, categoryName, icon)
    }

    override suspend fun deleteOperation(operation: Operation) {
        dao.deleteOperation(operation)
    }

    override suspend fun insertOperation(operation: Operation) {
        dao.insertOperation(operation)
    }

    override suspend fun updateOperation(operation: Operation) {
        dao.updateOperation(operation)
    }

    override fun getOperationsByAccountId(accountId: Int): Flow<List<OperationAndCategoryAndAccount>> {
        return dao.getOperationsByAccountId(accountId)
    }

    override fun getOperationsByCategoryIdAndMoneyId(
        categoryId: Int,
        moneyId: Int
    ): Flow<List<OperationAndCategoryAndAccount>> {
        return dao.getOperationsByCategoryIdAndMoneyId(categoryId, moneyId)
    }

    override suspend fun insertMoney(money: Money) {
        dao.insertMoney(money)
    }

    override suspend fun updateMoneyPlan(moneyId: Int, plan: Double) {
        dao.updateMoneyPlan(moneyId, plan)
    }

    override suspend fun updateMoneySum(moneyId: Int, money: Int) {
        dao.updateMoneySum(moneyId, money)
    }

    override suspend fun updateMoneySub(moneyId: Int, money: Int) {
        dao.updateMoneySub(moneyId, money)
    }

    override suspend fun deleteOperationsByAccountId(accountId: Int) {
        dao.deleteOperationsByAccountId(accountId)
    }

    override suspend fun deleteOperationsByCategoryId(categoryId: Int) {
        dao.deleteOperationsByCategoryId(categoryId)
    }

    override suspend fun deleteMoneyByCategoryId(categoryId: Int) {
        dao.deleteMoneyByCategoryId(categoryId)
    }
}