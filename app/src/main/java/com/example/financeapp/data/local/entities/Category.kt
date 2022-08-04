package com.example.financeapp.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.financeapp.data.local.CategoryType
import kotlinx.parcelize.Parcelize

@Entity(tableName = "categories")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    var categoryId:Int=0,
    var categoryName:String="",
    var type:CategoryType,
    var icon:String=""
):Parcelable
