package com.example.financeapp.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "accounts")
@Parcelize
data class Account(
    @PrimaryKey(autoGenerate = true)
    var accId:Int=0,
    var accName:String="",
    var icon:String="",
    var money:Double=0.0,
    var selected:Boolean=false
):Parcelable{
    override fun toString(): String {
        return accName
    }
}
