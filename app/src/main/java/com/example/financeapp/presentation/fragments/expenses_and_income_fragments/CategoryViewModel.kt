package com.example.financeapp.presentation.fragments.expenses_and_income_fragments

import android.util.Log
import androidx.lifecycle.*
import com.example.financeapp.data.local.CategoryType
import com.example.financeapp.domain.model.CategoryAndMoney
import com.example.financeapp.domain.repository.FinanceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class CategoryViewModel @AssistedInject constructor(
    @Assisted type: CategoryType,
    repository: FinanceRepository
) : ViewModel() {
    val month = MutableLiveData(Calendar.getInstance().get(Calendar.MONTH) + 1)
    val year = MutableLiveData(Calendar.getInstance().get(Calendar.YEAR))

    private val _categories = combine(
        month.asFlow(),
        year.asFlow()
    ) { month, year ->
        Pair(month, year)
    }.flatMapLatest { (month, year) ->
        repository.getCategoriesByTypeAndMonthYear(type, month, year)
    }
    val categories = _categories.asLiveData()

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    fun onCategoryClick(categoryAndMoney: CategoryAndMoney) = viewModelScope.launch {
        eventChannel.send(
            Event.NavigateToOperationsScreen(
                month.value!!,
                year.value!!,
                categoryAndMoney
            )
        )
    }

    fun onCategoryLongClick(categoryAndMoney: CategoryAndMoney, type: CategoryType) =
        viewModelScope.launch {
            eventChannel.send(Event.NavigateToEditCategoryScreen(categoryAndMoney, type))
        }

    fun onAddCategoryClick(month: Int, year: Int, type: CategoryType) = viewModelScope.launch {
        eventChannel.send(Event.NavigateToAddCategoryScreen(month, year, type))
    }

    fun setMonthAndYear(_month: Int, _year: Int) {
        month.value = _month
        year.value = _year
    }

    fun onDateClick() = viewModelScope.launch {
        eventChannel.send(Event.ShowDatePicker)
    }

    sealed class Event {
        data class NavigateToEditCategoryScreen(
            val categoryAndMoney: CategoryAndMoney,
            val type: CategoryType
        ) : Event()

        data class NavigateToAddCategoryScreen(
            val month: Int,
            val year: Int,
            val type: CategoryType
        ) : Event()

        data class NavigateToOperationsScreen(
            val month: Int,
            val year: Int,
            val categoryAndMoney: CategoryAndMoney
        ) : Event()

        object ShowDatePicker : Event()
    }
}