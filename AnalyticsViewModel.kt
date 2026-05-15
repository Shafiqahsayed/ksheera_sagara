package com.ksheera.sagara.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksheera.sagara.data.repository.KsheeraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ExpenseCategoryData(
    val category: String,
    val amount: Double
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: KsheeraRepository
) : ViewModel() {

    val expenseBreakdown: StateFlow<List<ExpenseCategoryData>> = repository.getAllExpenseLogs()
        .map { logs ->
            logs.groupBy { it.category }
                .map { (category, logsList) ->
                    ExpenseCategoryData(category, logsList.sumOf { it.amount })
                }
                .sortedByDescending { it.amount }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // For a real app, cow-wise profitability would fetch logs mapped to specific cows.
    // Given the simplified MVP structure, we can mock it or leave it out if we didn't add cow IDs to logs.
}
