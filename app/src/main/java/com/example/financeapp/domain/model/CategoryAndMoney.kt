package com.example.financeapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryAndMoney(
    val categoryId:Int=0,
    val categoryName:String="",
    val icon:Int=0,
    val moneyId:Int=0,
    val moneyAmount:Double=0.0,
    val plan:Double?=null
):Parcelable
