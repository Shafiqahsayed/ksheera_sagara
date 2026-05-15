package com.ksheera.sagara.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ksheera.sagara.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val logs by viewModel.logs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.history), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))

        if (logs.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_entries_found))
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs) { logItem ->
                    LogCard(logItem)
                }
            }
        }
    }
}

@Composable
fun LogCard(logItem: LogItem) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(logItem.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(dateString, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                when (logItem) {
                    is LogItem.IncomeItem -> {
                        Text(stringResource(R.string.milk_income), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("${logItem.log.liters} L @ $${logItem.log.pricePerLiter}", style = MaterialTheme.typography.bodyMedium)
                    }
                    is LogItem.ExpenseItem -> {
                        Text("${stringResource(R.string.expense_label)}${logItem.log.category}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(logItem.log.note ?: "", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
            }

            when (logItem) {
                is LogItem.IncomeItem -> {
                    Text("+$${String.format("%.2f", logItem.log.totalAmount)}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                is LogItem.ExpenseItem -> {
                    Text("-$${String.format("%.2f", logItem.log.amount)}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
