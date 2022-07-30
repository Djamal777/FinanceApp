package com.example.financeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.financeapp.R
import com.example.financeapp.data.local.InitialData.accounts
import com.example.financeapp.data.local.InitialData.categories
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
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
        @ApplicationScope private val applicationScope: CoroutineScope,
        @ApplicationContext private val c: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().financeDao()
            applicationScope.launch {
                var categoryId: Int
                for (account in accounts) {
                    if (account.accName == "Наличные") {
                        account.icon = c.resources.getResourceName(R.drawable.ic_baseline_wallet_24)
                    } else {
                        account.icon =
                            c.resources.getResourceName(R.drawable.ic_baseline_attach_money_24)
                    }
                    dao.insertAccount(account)
                }
                categories[0].icon=c.resources.getResourceName(R.drawable.ic_baseline_money_24)
                categories[1].icon=c.resources.getResourceName(R.drawable.ic_baseline_shopping_basket_24)
                categories[2].icon=c.resources.getResourceName(R.drawable.ic_baseline_fastfood_24)
                categories[3].icon=c.resources.getResourceName(R.drawable.ic_baseline_movie_24)
                categories[4].icon=c.resources.getResourceName(R.drawable.ic_baseline_child_care_24)
                categories[5].icon=c.resources.getResourceName(R.drawable.ic_baseline_local_hospital_24)
                categories[6].icon=c.resources.getResourceName(R.drawable.ic_baseline_gavel_24)
                for(category in categories) {
                    categoryId = dao.insertCategory(category).toInt()
                    dao.insertMoney(
                        Money(
                            categoryId = categoryId
                        )
                    )
                }
            }
        }
    }
}