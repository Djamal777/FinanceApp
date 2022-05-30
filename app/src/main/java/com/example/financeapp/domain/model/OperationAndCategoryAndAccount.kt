package com.example.financeapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class OperationAndCategoryAndAccount(
    val id:Int=0,
    val date:Int= Calendar.getInstance().get(Calendar.DATE),
    val money:Double=0.0,
    val accName:String="",
    val icon:Int=0,
    val categoryName:String=""
):Parcelable
