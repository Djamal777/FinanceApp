package com.example.financeapp.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.financeapp.data.local.entities.Account
import com.example.financeapp.data.local.entities.Operation

data class AccountWithOperations(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "accId",
        entityColumn = "accountId"
    )
    val operations: List<Operation>
)
