package com.ksheera.sagara.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksheera.sagara.data.local.entity.ExpenseLog
import com.ksheera.sagara.data.local.entity.IncomeLog
import com.ksheera.sagara.data.repository.KsheeraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val repository: KsheeraRepository
) : ViewModel() {

    fun addIncome(liters: Double, pricePerLiter: Double) {
        viewModelScope.launch {
            val totalAmount = liters * pricePerLiter
            repository.insertIncome(
                IncomeLog(
                    timestamp = System.currentTimeMillis(),
                    liters = liters,
                    fatPercentage = 0.0,
                    pricePerLiter = pricePerLiter,
                    totalAmount = totalAmount
                )
            )
        }
    }

    fun addExpense(category: String, amount: Double, note: String) {
        viewModelScope.launch {
            repository.insertExpense(
                ExpenseLog(
                    timestamp = System.currentTimeMillis(),
                    category = category,
                    amount = amount,
                    note = note
                )
            )
        }
    }
}
