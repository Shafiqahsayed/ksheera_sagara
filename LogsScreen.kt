package com.ksheera.sagara.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ksheera.sagara.R

@Composable
fun LogsScreen(viewModel: LogsViewModel = hiltViewModel()) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Income (Milk Slip)", "Expense")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
            if (selectedTabIndex == 0) {
                IncomeLogForm(viewModel)
            } else {
                ExpenseLogForm(viewModel)
            }
        }
    }
}

@Composable
fun IncomeLogForm(viewModel: LogsViewModel) {
    val context = LocalContext.current
    var liters by remember { mutableStateOf("") }
    var pricePerLiter by remember { mutableStateOf("") }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            OutlinedTextField(
                value = liters,
                onValueChange = { liters = it },
                label = { Text(stringResource(R.string.liters)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pricePerLiter,
                onValueChange = { pricePerLiter = it },
                label = { Text(stringResource(R.string.price_per_liter)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val l = liters.toDoubleOrNull()
                    val p = pricePerLiter.toDoubleOrNull()
                    if (l != null && p != null) {
                        viewModel.addIncome(l, p)
                        liters = ""
                        pricePerLiter = ""
                        Toast.makeText(context, context.getString(R.string.income_saved_success), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, context.getString(R.string.invalid_numbers), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.save_income))
            }

            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseLogForm(viewModel: LogsViewModel) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Fodder") }
    val categories = listOf("Fodder", "Medical", "Labor", "Other")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.category)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(R.string.amount)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(stringResource(R.string.note_optional)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val a = amount.toDoubleOrNull()
                    if (a != null) {
                        viewModel.addExpense(selectedCategory, a, note)
                        amount = ""
                        note = ""
                        Toast.makeText(context, context.getString(R.string.expense_saved_success), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, context.getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.save_expense))
            }

            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
