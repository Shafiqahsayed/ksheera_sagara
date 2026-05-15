package com.ksheera.sagara.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    val expenseBreakdown by viewModel.expenseBreakdown.collectAsState()

    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        Color(0xFFFF9800),
        Color(0xFF9C27B0)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.analytics), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        if (expenseBreakdown.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_expense_data))
            }
        } else {
            val totalExpense = expenseBreakdown.sumOf { it.amount }
            
            // Pie Chart
            Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    var startAngle = -90f
                    expenseBreakdown.forEachIndexed { index, data ->
                        val sweepAngle = ((data.amount / totalExpense) * 360).toFloat()
                        drawArc(
                            color = colors[index % colors.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true
                        )
                        startAngle += sweepAngle
                    }
                }
                // Donut hole
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Legend and List
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(expenseBreakdown.size) { index ->
                    val data = expenseBreakdown[index]
                    val percentage = (data.amount / totalExpense) * 100
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(colors[index % colors.size], CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(data.category, style = MaterialTheme.typography.bodyLarge)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("$${String.format("%.2f", data.amount)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text("${String.format("%.1f", percentage)}%", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
