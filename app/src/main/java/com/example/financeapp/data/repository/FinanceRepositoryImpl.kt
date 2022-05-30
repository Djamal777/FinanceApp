package com.example.financeapp.data.repository

import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.FinanceDao
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.data.local.entities.relations.AccountWithOperations
import com.example.financeapp.data.local.entities.relations.CategoryWithMoney
import com.example.financeapp.data.local.entities.relations.CategoryWithOperations
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import com.example.financeapp.domain.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val financeDao: FinanceDao
):FinanceRepository {
    override fun getAccounts(): Flow<List<Account>> {
        return financeDao.getAccounts()
    }

    override fun getCategoriesByTypeAndIds(
        type: CategoryType,
        ids: List<Int>
    ): Flow<List<CategoryWithMoney>> {
        return financeDao.getCategoriesByTypeAndIds(type,ids)
    }

    override fun getOperations(): Flow<List<OperationAndCategoryAndAccount>> {
        return financeDao.getOperations()
    }

    override fun getMoneyByMonthAndYear(month: Int, year: Int): Flow<List<Money>> {
        return financeDao.getMoneyByMonthAndYear(month,year)
    }

    override suspend fun deleteAccount(account: Account) {
        financeDao.deleteAccount(account)
    }

    override suspend fun insertAccount(account: Account) {
        financeDao.insertAccount(account)
    }

    override suspend fun updateAccount(account: Account) {
        financeDao.updateAccount(account)
    }

    override suspend fun deleteCategory(category: Category) {
        financeDao.deleteCategory(category)
    }

    override suspend fun insertCategory(category: Category): Long {
        return financeDao.insertCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        financeDao.updateCategory(category)
    }

    override suspend fun deleteOperation(operation: Operation) {
        financeDao.deleteOperation(operation)
    }

    override suspend fun insertOperation(operation: Operation) {
        financeDao.insertOperation(operation)
    }

    override suspend fun updateOperation(operation: Operation) {
        financeDao.updateOperation(operation)
    }

    override fun getOperationsByAccountId(accountId: Int): Flow<List<OperationAndCategoryAndAccount>> {
        return financeDao.getOperationsByAccountId(accountId)
    }

    override fun getOperationsByCategoryId(categoryId: Int): Flow<List<CategoryWithOperations>> {
        return financeDao.getOperationsByCategoryId(categoryId)
    }

    override suspend fun insertMoney(money: Money) {
        return financeDao.insertMoney(money)
    }

    override suspend fun updateMoney(money: Money) {
        financeDao.updateMoney(money)
    }

    override suspend fun deleteOperationsByAccountId(accountId: Int) {
        financeDao.deleteOperationsByAccountId(accountId)
    }

    override suspend fun deleteOperationsByCategoryId(categoryId: Int) {
        financeDao.deleteOperationsByCategoryId(categoryId)
    }

    override suspend fun deleteMoneyByCategoryId(categoryId:Int) {
        financeDao.deleteMoneyByCategoryId(categoryId)
    }

    override fun getAccountById(accId: Int): Flow<Account> {
        return financeDao.getAccountById(accId)
    }

    override fun getMoneyByCategoryIdAndMonthAndYear(categoryId: Int, month: Int, year: Int): Flow<Money> {
        return financeDao.getMoneyByCategoryIdAndMonthAndYear(categoryId,month,year)
    }
}