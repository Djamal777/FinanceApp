package com.example.financeapp.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Category
import com.example.financeapp.data.local.entities.Operation

data class CategoryWithOperations(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val operations: List<Operation>
)
