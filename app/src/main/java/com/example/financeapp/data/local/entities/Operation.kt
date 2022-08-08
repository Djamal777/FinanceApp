package com.example.financeapp.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "operations")
@Parcelize
data class Operation(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val date:Long,
    var money:Double=0.0,
    val categoryId:Int=0,
    val moneyId:Int=0,
    val accountId:Int=0
):Parcelable