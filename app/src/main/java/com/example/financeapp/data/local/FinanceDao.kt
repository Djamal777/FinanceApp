package com.example.financeapp.data.local

import androidx.room.*
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.data.local.entities.relations.AccountWithOperations
import com.example.financeapp.data.local.entities.relations.CategoryWithMoney
import com.example.financeapp.data.local.entities.relations.CategoryWithOperations
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT * from accounts")
    fun getAccounts(): Flow<List<Account>>

    @Query("SELECT * from categories WHERE type=:type AND categoryId IN (:ids)")
    fun getCategoriesByTypeAndIds(type:CategoryType, ids:List<Int>):Flow<List<CategoryWithMoney>>

    @Query("SELECT operations.id," +
            "operations.date," +
            "operations.money," +
            "account.accName," +
            "accounts.icon," +
            "categories.category_name" +
            "from operations" +
            "LEFT JOIN categories" +
            "ON categories.categoryId=operations.categoryId" +
            "LEFT JOIN accounts" +
            "ON accounts.accId=operations.accountId" +
            "ORDER BY operations.date DESC")
    fun getOperations():Flow<List<OperationAndCategoryAndAccount>>

    @Query("SELECT * from money WHERE month=:month AND year=:year")
    fun getMoneyByMonthAndYear(month:Int, year:Int):Flow<List<Money>>

    @Delete
    suspend fun deleteAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category):Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteOperation(operation: Operation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: Operation)

    @Update
    suspend fun updateOperation(operation: Operation)

    @Query("SELECT operations.id," +
            "operations.date," +
            "operations.money," +
            "account.accName," +
            "accounts.icon," +
            "categories.category_name" +
            "from operations" +
            "WHERE operations.accountId:=accountId" +
            "LEFT JOIN categories" +
            "ON categories.categoryId=operations.categoryId" +
            "LEFT JOIN accounts" +
            "ON accounts.accId=operations.accountId" +
            "ORDER BY operations.date DESC")
    fun getOperationsByAccountId(accountId:Int):Flow<List<OperationAndCategoryAndAccount>>

    @Transaction
    @Query("SELECT * from operations WHERE categoryId:=categoryId ORDER BY date DESC")
    fun getOperationsByCategoryId(categoryId:Int):Flow<List<CategoryWithOperations>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoney(money: Money)

    @Update
    suspend fun updateMoney(money: Money)

    @Query("DELETE * from operations WHERE accountId:=accountId")
    suspend fun deleteOperationsByAccountId(accountId:Int)

    @Query("DELETE * from operations WHERE categoryId:=categoryId")
    suspend fun deleteOperationsByCategoryId(categoryId:Int)

    @Query("DELETE * from money WHERE categoryId:=categoryId")
    suspend fun deleteMoneyByCategoryId(categoryId:Int)

    @Query("SELECT * from accounts WHERE accId:=accId")
    fun getAccountById(accId:Int):Flow<Account>

    @Query("SELECT * from money WHERE categoryId:=categoryId AND month:=month AND year:=year")
    fun getMoneyByCategoryIdAndMonthAndYear(categoryId: Int, month: Int, year:Int):Flow<Money>

}