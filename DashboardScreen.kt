package com.ksheera.sagara.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ksheera.sagara.R
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DashboardScreen(
    onNavigateToLogs: () -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val netProfit by viewModel.netProfit.collectAsState()
    val profitPerLiter by viewModel.profitPerLiter.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()

    val isProfit = netProfit >= 0
    val healthColor = if (isProfit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val healthText = if (isProfit) "PROFIT" else "LOSS"

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.dashboard_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LanguageSelector()
                TextButton(onClick = {
                    viewModel.logout()
                    onLogout()
                }) {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                }
            }
        }
        
        val todayProfit by viewModel.todayProfit.collectAsState()
        val isTodayProfit = todayProfit >= 0
        val todayHealthColor = if (isTodayProfit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        val todayHealthText = if (isTodayProfit) "PROFIT" else "LOSS"

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Profit Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            colors = CardDefaults.cardColors(containerColor = todayHealthColor),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.todays_profit), style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(todayHealthText, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.todays_profit_desc, String.format("%.2f", todayProfit)), style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Profit Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            colors = CardDefaults.cardColors(containerColor = healthColor),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.weekly_profit), style = MaterialTheme.typography.titleMedium, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(healthText, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Black, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.weekly_profit_desc, String.format("%.2f", netProfit)), style = MaterialTheme.typography.titleMedium, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Overview Chart
        val total = totalIncome + totalExpense
        if (total > 0) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Chart
                    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                        androidx.compose.foundation.Canvas(modifier = Modifier.size(100.dp)) {
                            val incomeAngle = ((totalIncome / total) * 360).toFloat()
                            val expenseAngle = ((totalExpense / total) * 360).toFloat()
                            
                            drawArc(color = Color(0xFF4CAF50), startAngle = -90f, sweepAngle = incomeAngle, useCenter = true)
                            drawArc(color = Color(0xFFF44336), startAngle = -90f + incomeAngle, sweepAngle = expenseAngle, useCenter = true)
                        }
                        Box(modifier = Modifier.size(50.dp).background(MaterialTheme.colorScheme.surface, androidx.compose.foundation.shape.CircleShape))
                    }
                    
                    // Legend
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).background(Color(0xFF4CAF50), androidx.compose.foundation.shape.CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${stringResource(R.string.income_label)}$${String.format("%.2f", totalIncome)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).background(Color(0xFFF44336), androidx.compose.foundation.shape.CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${stringResource(R.string.expense_label)}$${String.format("%.2f", totalExpense)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profit Per Liter Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.profit_per_liter), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text("$${String.format("%.2f", profitPerLiter)}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = healthColor)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onNavigateToLogs,
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_milk_slip))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.add_milk_slip))
            }

            Button(
                onClick = onNavigateToLogs,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.add_expense))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.add_expense))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Share button
        val context = androidx.compose.ui.platform.LocalContext.current

        Button(
            onClick = {
                val file = com.ksheera.sagara.utils.PdfGenerator.generateFinancialSummary(context, totalIncome, totalExpense)
                if (file != null) {
                    val uri = androidx.core.content.FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                        type = "application/pdf"
                        putExtra(android.content.Intent.EXTRA_STREAM, uri)
                        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(android.content.Intent.createChooser(intent, "Share Summary"))
                } else {
                    android.widget.Toast.makeText(context, context.getString(R.string.failed_to_generate_pdf), android.widget.Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.share_weekly_summary))
        }

        Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Composable
fun LanguageSelector() {
    var expanded by remember { mutableStateOf(false) }
    val currentLocales = AppCompatDelegate.getApplicationLocales()
    val currentLang = if (!currentLocales.isEmpty) currentLocales[0]?.language else "en"
    val langLabel = when(currentLang) {
        "hi" -> "HI"
        "kn" -> "KN"
        else -> "EN"
    }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        TextButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Language, contentDescription = stringResource(R.string.language))
            Spacer(modifier = Modifier.width(4.dp))
            Text(langLabel)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("English") }, onClick = { 
                expanded = false
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
            })
            DropdownMenuItem(text = { Text("हिन्दी") }, onClick = { 
                expanded = false
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("hi"))
            })
            DropdownMenuItem(text = { Text("ಕನ್ನಡ") }, onClick = { 
                expanded = false
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("kn"))
            })
        }
    }
}
