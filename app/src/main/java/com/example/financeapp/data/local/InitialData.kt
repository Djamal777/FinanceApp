package com.example.financeapp.data.local

import com.example.financeapp.R
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category

object InitialData {
    val accounts = listOf(
        Account(
            accName = "Наличные",
            money=50.0,
            icon = R.drawable.ic_baseline_wallet_24
        ),
        Account(
            accName = "Тинькофф",
            money=21.0,
            icon = R.drawable.ic_baseline_attach_money_24
        ),
        Account(
            accName = "Наличные",
            money=50.0,
            icon = R.drawable.ic_baseline_wallet_24
        ),
        Account(
            accName = "Тинькофф",
            money=21.0,
            icon = R.drawable.ic_baseline_attach_money_24
        )
    )

    val categories = listOf(
        Category(
            categoryName = "Зарплата",
            type = CategoryType.INCOME,
            icon = R.drawable.ic_baseline_money_24
        ),
        Category(
            categoryName = "Продукты",
            type = CategoryType.EXPENSE,
            icon = R.drawable.ic_baseline_shopping_basket_24
        ),
        Category(
            categoryName = "Кафе",
            type = CategoryType.EXPENSE,
            icon = R.drawable.ic_baseline_fastfood_24
        ),
        Category(
            categoryName = "Досуг",
            type = CategoryType.EXPENSE,
            icon = R.drawable.ic_baseline_movie_24
        ),
        Category(
            categoryName = "Семья",
            type = CategoryType.EXPENSE,
            icon = R.drawable.ic_baseline_child_care_24
        ),
        Category(
            categoryName = "Здоровье",
            type = CategoryType.EXPENSE,
            icon = R.drawable.ic_baseline_local_hospital_24
        ),
        Category(
            categoryName = "Налоги",
            type = CategoryType.EXPENSE,
            icon = R.drawable.ic_baseline_gavel_24
        )
    )

}