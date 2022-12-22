package com.example.financeapp.data.local

import android.content.Context
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import dagger.hilt.android.qualifiers.ApplicationContext

object InitialData {

    val accounts = listOf(
        Account(
            accName = "Наличные",
            money=0.0
        )
    )

    val categories = listOf(
        Category(
            categoryName = "Зарплата",
            type = CategoryType.INCOME,
        ),
        Category(
            categoryName = "Продукты",
            type = CategoryType.EXPENSE,
        ),
        Category(
            categoryName = "Кафе",
            type = CategoryType.EXPENSE,
        ),
        Category(
            categoryName = "Досуг",
            type = CategoryType.EXPENSE,
        ),
        Category(
            categoryName = "Семья",
            type = CategoryType.EXPENSE,
        ),
        Category(
            categoryName = "Здоровье",
            type = CategoryType.EXPENSE,
        ),
        Category(
            categoryName = "Налоги",
            type = CategoryType.EXPENSE,
        )
    )

}