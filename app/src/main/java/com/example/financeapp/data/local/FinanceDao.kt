package com.example.financeapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Query("SELECT * from accounts")
    fun getAccounts(): Flow<List<Account>>

    @Query("SELECT * from categories")
    fun getCategories():Flow<List<Category>>

    @Query("SELECT categories.categoryId," +
            " categories.categoryName," +
            " categories.icon," +
            " money.moneyId," +
            " money.moneyAmount," +
            " money.plan," +
            " categories.type" +
            " FROM categories" +
            " INNER JOIN money" +
            " ON money.categoryId=categories.categoryId AND" +
            " money.month=:month AND money.year=:year" +
            " WHERE categories.type=:type")
    fun getCategoriesByTypeAndMonthYear(type:CategoryType, month:Int, year:Int):Flow<List<CategoryAndMoney>>

    @Query("SELECT operations.id," +
            " operations.date," +
            " operations.money," +
            " categories.type AS categoryType," +
            " accounts.accName," +
            " accounts.icon," +
            " categories.categoryName," +
            " accounts.accId AS accountId" +
            " FROM operations" +
            " LEFT JOIN categories" +
            " ON categories.categoryId=operations.categoryId" +
            " LEFT JOIN accounts" +
            " ON accounts.accId=operations.accountId" +
            " ORDER BY operations.date DESC")
    fun getOperations():Flow<List<OperationAndCategoryAndAccount>>

    @Delete
    suspend fun deleteAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Update
    suspend fun updateAccount(account: Account)

    @Query("UPDATE accounts SET money=money-:money WHERE accId=:accId")
    suspend fun updateAccountSub(money:Double, accId:Int)

    @Query("UPDATE accounts SET money=money+:money WHERE accId=:accId")
    suspend fun updateAccountSum(money:Double, accId:Int)

    @Query("DELETE FROM categories WHERE categoryId=:categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category):Long

    @Query("UPDATE categories SET categoryName=:categoryName, " +
            "icon=:icon WHERE categoryId=:categoryId")
    suspend fun updateCategoryById(categoryId:Int, categoryName:String, icon:String)

    @Query("DELETE FROM operations WHERE id=:operationId")
    suspend fun deleteOperation(operationId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: Operation)

    @Update
    suspend fun updateOperation(operation:Operation)

    @Query("SELECT operations.id, " +
            "operations.date, " +
            "operations.money, " +
            "categories.type AS categoryType, " +
            "accounts.accName, " +
            "accounts.icon, " +
            "categories.categoryName," +
            "accounts.accId AS accountId " +
            "FROM operations " +
            "LEFT JOIN categories " +
            "ON categories.categoryId=operations.categoryId " +
            "LEFT JOIN accounts " +
            "ON accounts.accId=operations.accountId " +
            "WHERE operations.accountId=:accountId " +
            "ORDER BY operations.date DESC")
    fun getOperationsByAccountId(accountId:Int):Flow<List<OperationAndCategoryAndAccount>>

    @Query("SELECT operations.id, " +
            "operations.date, " +
            "operations.money, " +
            "categories.type AS categoryType, " +
            "accounts.accName, " +
            "accounts.icon, " +
            "categories.categoryName, " +
            "operations.accountId " +
            "FROM operations " +
            "LEFT JOIN categories " +
            "ON categories.categoryId=operations.categoryId " +
            "LEFT JOIN accounts " +
            "ON accounts.accId=operations.accountId " +
            "WHERE operations.categoryId=:categoryId AND " +
            "operations.moneyId=:moneyId " +
            "ORDER BY operations.date DESC")
    fun getOperationsByCategoryIdAndMoneyId(categoryId:Int, moneyId:Int):Flow<List<OperationAndCategoryAndAccount>>

    @Query("SELECT * from money")
    fun getMoney():Flow<List<Money>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoney(money: Money)

    @Query("UPDATE money SET plan=:plan WHERE moneyId=:moneyId")
    suspend fun updateMoneyPlan(moneyId:Int, plan: Double?)

    @Query("UPDATE money SET moneyAmount=moneyAmount + :money " +
            "WHERE moneyId=:moneyId")
    suspend fun updateMoneySum(moneyId: Int, money:Double)

    @Query("UPDATE money SET moneyAmount=moneyAmount - :money " +
            "WHERE moneyId=:moneyId")
    suspend fun updateMoneySub(moneyId: Int, money:Double)

    @Query("DELETE FROM operations WHERE accountId= :accountId")
    suspend fun deleteOperationsByAccountId(accountId:Int)

    @Query("DELETE FROM operations WHERE categoryId= :categoryId")
    suspend fun deleteOperationsByCategoryId(categoryId:Int)

    @Query("DELETE FROM money WHERE categoryId= :categoryId")
    suspend fun deleteMoneyByCategoryId(categoryId:Int)

}