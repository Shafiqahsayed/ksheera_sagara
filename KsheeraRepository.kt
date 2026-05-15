package com.ksheera.sagara.data.repository

import com.ksheera.sagara.data.local.dao.CowDao
import com.ksheera.sagara.data.local.dao.ExpenseDao
import com.ksheera.sagara.data.local.dao.IncomeDao
import com.ksheera.sagara.data.local.entity.Cow
import com.ksheera.sagara.data.local.entity.ExpenseLog
import com.ksheera.sagara.data.local.entity.IncomeLog
import com.ksheera.sagara.data.local.prefs.UserManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KsheeraRepository @Inject constructor(
    private val cowDao: CowDao,
    private val incomeDao: IncomeDao,
    private val expenseDao: ExpenseDao,
    private val userManager: UserManager
) {
    private val ownerId: String
        get() = userManager.getCurrentUser()
    // Cows
    fun getAllCows(): Flow<List<Cow>> = cowDao.getAllCows(ownerId)
    suspend fun insertCow(cow: Cow) = cowDao.insertCow(cow.copy(ownerId = ownerId))

    // Income
    fun getAllIncomeLogs(): Flow<List<IncomeLog>> = incomeDao.getAllIncomeLogs(ownerId)
    fun getTotalIncome(): Flow<Double?> = incomeDao.getTotalIncome(ownerId)
    fun getTotalIncomeForPeriod(startTime: Long, endTime: Long): Flow<Double?> = incomeDao.getTotalIncomeForPeriod(startTime, endTime, ownerId)
    fun getIncomeLogsForPeriod(startTime: Long, endTime: Long): Flow<List<IncomeLog>> = incomeDao.getIncomeLogsForPeriod(startTime, endTime, ownerId)
    suspend fun insertIncome(income: IncomeLog) = incomeDao.insertIncome(income.copy(ownerId = ownerId))

    // Expense
    fun getAllExpenseLogs(): Flow<List<ExpenseLog>> = expenseDao.getAllExpenseLogs(ownerId)
    fun getTotalExpenses(): Flow<Double?> = expenseDao.getTotalExpenses(ownerId)
    fun getTotalExpensesForPeriod(startTime: Long, endTime: Long): Flow<Double?> = expenseDao.getTotalExpensesForPeriod(startTime, endTime, ownerId)
    fun getExpenseLogsForPeriod(startTime: Long, endTime: Long): Flow<List<ExpenseLog>> = expenseDao.getExpenseLogsForPeriod(startTime, endTime, ownerId)
    suspend fun insertExpense(expense: ExpenseLog) = expenseDao.insertExpense(expense.copy(ownerId = ownerId))
}
