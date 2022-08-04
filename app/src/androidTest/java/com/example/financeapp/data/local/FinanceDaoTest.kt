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
        val account=Account()
        dao.insertAccount(account)
        val allAccounts=dao.getAccounts().asLiveData().getOrAwaitValue()
        assertThat(allAccounts).contains(account)
    }

    @Test
    fun insertCategory()= runBlocking{
        val category=Category(type=CategoryType.INCOME)
        dao.insertCategory(category)
        val allCategories=dao.getCategories().asLiveData().getOrAwaitValue()
        assertThat(allCategories).contains(category)
    }

    @Test
    fun insertOperation()= runBlocking{
        val operation=Operation(date=1)
        dao.insertOperation(operation)
        val allOperations=dao.getOperations().asLiveData().getOrAwaitValue()
        assertThat(allOperations).contains(operation)
    }

    @Test
    fun insertMoney()= runBlocking{
        val money= Money()
        dao.insertMoney(money)
        val allMoney=dao.getMoney().asLiveData().getOrAwaitValue()
        assertThat(allMoney).contains(money)
    }

    @After
    fun teardown(){
        database.close()
    }
}