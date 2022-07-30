package com.example.financeapp.domain.model

import android.os.Parcelable
import com.example.financeapp.data.local.CategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryAndMoney(
    var categoryId:Int=0,
    var categoryName:String="",
    var icon:String="",
    var moneyId:Int=0,
    var moneyAmount:Double=0.0,
    var plan:Double?=null,
    var type: CategoryType
):Parcelable
