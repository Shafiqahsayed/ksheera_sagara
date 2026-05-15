package com.ksheera.sagara.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ksheera.sagara.data.local.dao.CowDao
import com.ksheera.sagara.data.local.dao.ExpenseDao
import com.ksheera.sagara.data.local.dao.IncomeDao
import com.ksheera.sagara.data.local.entity.Cow
import com.ksheera.sagara.data.local.entity.ExpenseLog
import com.ksheera.sagara.data.local.entity.IncomeLog

@Database(
    entities = [Cow::class, IncomeLog::class, ExpenseLog::class],
    version = 2,
    exportSchema = false
)
abstract class KsheeraDatabase : RoomDatabase() {
    abstract val cowDao: CowDao
    abstract val incomeDao: IncomeDao
    abstract val expenseDao: ExpenseDao
}
