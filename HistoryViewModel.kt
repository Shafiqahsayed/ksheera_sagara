package com.ksheera.sagara.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksheera.sagara.data.local.entity.ExpenseLog
import com.ksheera.sagara.data.local.entity.IncomeLog
import com.ksheera.sagara.data.repository.KsheeraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class LogItem {
    abstract val timestamp: Long
    data class IncomeItem(val log: IncomeLog) : LogItem() {
        override val timestamp: Long = log.timestamp
    }
    data class ExpenseItem(val log: ExpenseLog) : LogItem() {
        override val timestamp: Long = log.timestamp
    }
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: KsheeraRepository
) : ViewModel() {

    val logs: StateFlow<List<LogItem>> = repository.getAllIncomeLogs()
        .combine(repository.getAllExpenseLogs()) { incomes, expenses ->
            val combinedLogs = incomes.map { LogItem.IncomeItem(it) } +
                    expenses.map { LogItem.ExpenseItem(it) }
            combinedLogs.sortedByDescending { it.timestamp }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
