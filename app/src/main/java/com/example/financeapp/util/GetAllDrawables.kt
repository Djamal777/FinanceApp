package com.example.financeapp.util

import android.content.Context
import com.example.financeapp.R
import java.lang.Exception
import java.lang.reflect.Field


fun getAllDrawable(c: Context):List<String>{
    val drawablesFields: Array<Field> = R.drawable::class.java.declaredFields
    val drawables = mutableListOf<String>()

    for (field in drawablesFields) {
        try {
            if(c.resources.getResourceName(field.getInt(null)).contains("ic_baseline")) {
                drawables.add(c.resources.getResourceName(field.getInt(null)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return drawables
}