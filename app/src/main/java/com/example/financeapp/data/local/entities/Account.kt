package com.example.financeapp.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("accounts")
@Parcelize
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accId:Int=0,
    val accName:String="",
    val icon:Int=0,
    val money:Double=0.0
):Parcelable
