package com.example.financeapp.domain.model

import android.os.Parcelable
import com.example.financeapp.data.local.CategoryType
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class OperationAndCategoryAndAccount(
    var id:Int=0,
    var date:Long,
    var money:Double=0.0,
    var categoryType: CategoryType,
    var accName:String="",
    var icon:String="",
    var categoryName:String="",
    var accountId:Int=0
):Parcelable
