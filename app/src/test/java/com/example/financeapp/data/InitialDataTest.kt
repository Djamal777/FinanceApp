package com.example.financeapp.data

import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.data.local.InitialData
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money
import com.example.financeapp.data.local.entities.Operation
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.model.OperationAndCategoryAndAccount

object InitialDataTest {

    val accounts=InitialData.accounts.toMutableList().apply {
        add(
            Account(
                accId = 1,
                accName = "Карта",
                money=0.0,
            )
        )
        add(
            Account(
                accId = 2,
                accName = "Тинькофф",
                money=0.0,
            )
        )
        add(
            Account(
                accId = 3,
                accName = "Сбербанк",
                money=0.0,
            )
        )
    }

    val categories=InitialData.categories.toMutableList().apply {
        add(
            Category(
                categoryId = 1,
                type = CategoryType.EXPENSE
            )
        )
        add(
            Category(
                categoryId = 2,
                type = CategoryType.EXPENSE
            )
        )
    }

    val operations= mutableListOf<Operation>(
        Operation(id=1, date=0),
        Operation(id=2, date=0, accountId = 1),
        Operation(id=3, date=0, categoryId = 1)
    )

    val money= mutableListOf<Money>(
        Money(moneyId = 1),
        Money(moneyId = 2),
        Money(moneyId = 3, categoryId = 1)
    )

    val operationAndCategoryAndAccount= listOf<OperationAndCategoryAndAccount>(
        OperationAndCategoryAndAccount(id=1, categoryType = CategoryType.INCOME, accountId = 1, date=0),
        OperationAndCategoryAndAccount(id=2, categoryType = CategoryType.INCOME, accountId = 2, date=0),
        OperationAndCategoryAndAccount(id=3, categoryType = CategoryType.EXPENSE, accountId = 3, date=0)
    )

    val categoryAndMoney = listOf<CategoryAndMoney>(
        CategoryAndMoney(categoryId = 1, type=CategoryType.INCOME),
        CategoryAndMoney(categoryId = 2, type=CategoryType.INCOME),
        CategoryAndMoney(categoryId = 3, type=CategoryType.INCOME)
    )
}