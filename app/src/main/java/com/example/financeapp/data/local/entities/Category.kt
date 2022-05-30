package com.example.financeapp.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financeapp.data.local.CategoryType
import kotlinx.parcelize.Parcelize

@Entity("categories")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int=0,
    val categoryName:String="",
    val type:CategoryType,
    val icon:Int=0
):Parcelable
