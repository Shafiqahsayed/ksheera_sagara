package com.ksheera.sagara.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksheera.sagara.data.repository.KsheeraRepository
import com.ksheera.sagara.data.local.prefs.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: KsheeraRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val startOfWeek: Long
    private val endOfWeek: Long
    private val startOfDay: Long
    private val endOfDay: Long

    init {
        val calendar = Calendar.getInstance()
        
        // Calculate today
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        endOfDay = calendar.timeInMillis - 1
        
        // Calculate week
        calendar.timeInMillis = startOfDay
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        startOfWeek = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        endOfWeek = calendar.timeInMillis - 1
    }

    val totalIncome: StateFlow<Double> = repository.getTotalIncomeForPeriod(startOfWeek, endOfWeek)
        .combine(repository.getIncomeLogsForPeriod(startOfWeek, endOfWeek)) { sum, _ -> sum ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = repository.getTotalExpensesForPeriod(startOfWeek, endOfWeek)
        .combine(repository.getExpenseLogsForPeriod(startOfWeek, endOfWeek)) { sum, _ -> sum ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val netProfit: StateFlow<Double> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayIncome: StateFlow<Double> = repository.getTotalIncomeForPeriod(startOfDay, endOfDay)
        .combine(repository.getIncomeLogsForPeriod(startOfDay, endOfDay)) { sum, _ -> sum ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayExpense: StateFlow<Double> = repository.getTotalExpensesForPeriod(startOfDay, endOfDay)
        .combine(repository.getExpenseLogsForPeriod(startOfDay, endOfDay)) { sum, _ -> sum ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val todayProfit: StateFlow<Double> = combine(todayIncome, todayExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalLiters: StateFlow<Double> = repository.getIncomeLogsForPeriod(startOfWeek, endOfWeek)
        .combine(repository.getIncomeLogsForPeriod(startOfWeek, endOfWeek)) { logs, _ -> logs.sumOf { it.liters } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val profitPerLiter: StateFlow<Double> = combine(netProfit, totalLiters) { profit, liters ->
        if (liters > 0) profit / liters else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun logout() {
        userManager.logout()
    }
}
