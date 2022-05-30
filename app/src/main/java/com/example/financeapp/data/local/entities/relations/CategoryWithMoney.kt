package com.example.financeapp.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Money

data class CategoryWithMoney(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val money:List<Money>
)
