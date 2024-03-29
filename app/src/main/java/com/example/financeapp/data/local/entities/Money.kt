package com.example.financeapp.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "money")
@Parcelize
data class Money(
    @PrimaryKey(autoGenerate = true)
    val moneyId:Int=0,
    var moneyAmount:Double=0.0,
    var plan:Double?=null,
    val month:Int= Calendar.getInstance().get(Calendar.MONTH)+1,
    val year:Int=Calendar.getInstance().get(Calendar.YEAR),
    val categoryId: Int=0
):Parcelable
