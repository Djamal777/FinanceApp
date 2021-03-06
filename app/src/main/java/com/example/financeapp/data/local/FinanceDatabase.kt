package com.example.financeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.InitialData.accounts
import com.example.financeapp.data.local.InitialData.categories
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [
        Account::class,
        Category::class,
        Operation::class,
        Money::class
    ], version = 1
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao

    class Callback @Inject constructor(
        private val database: Provider<FinanceDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().financeDao()
            applicationScope.launch {
                var categoryId: Int
                for (account in accounts) {
                    dao.insertAccount(account)
                }
                for(category in categories){
                    categoryId= dao.insertCategory(category).toInt()
                    dao.insertMoney(
                        Money(
                            categoryId = categoryId
                        )
                    )
                }
                repeat(6) {
                    dao.insertOperation(
                        Operation(
                            accountId = 1,
                            categoryId = 1
                        )
                    )
                    dao.insertOperation(
                        Operation(
                            accountId = 2,
                            categoryId = 1
                        )
                    )
                }
            }
        }
    }
}