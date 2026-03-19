package io.caveira.snacks.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.caveira.snacks.data.model.PaymentModel
import io.caveira.snacks.viewmodel.SnackViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnackOrderScreen(viewModel: SnackViewModel) {
    val snackOrders by viewModel.snackOrders.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add snack")
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider()
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Total: R$ %.2f".format(totalPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
        ) {
            items(snackOrders) { snackOrder ->
                SnackOrderItem(
                    snackOrder = snackOrder,
                    onDelete = { viewModel.deleteSnackOrder(snackOrder) },
                )
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
        ) {
            AddSnackForm(
                onSave = { store, price, date, payment ->
                    viewModel.saveSnackOrder(store, price, date, payment)
                    showSheet = false
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSnackForm(onSave: (String, Double, LocalDateTime, PaymentModel) -> Unit) {
    var store by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf(PaymentModel.entries.first()) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier =
            Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .imePadding(),
    ) {
        Text(
            text = "New Snack",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = store,
            onValueChange = { store = it },
            label = { Text("Store name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { new ->
                if (new.count { it == '.' } <= 1 && new.all { it.isDigit() || it == '.' }) {
                    price = new
                }
            },
            label = { Text("Price") },
            prefix = { Text("R$ ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value =
                    selectedDate?.let {
                        "%02d/%02d/%04d".format(it.day, it.monthNumber, it.year)
                    } ?: "",
                onValueChange = {},
                label = { Text("Date") },
                placeholder = { Text("DD/MM/YYYY") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .clickable { showDatePicker = true },
            )
        }

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = it },
        ) {
            OutlinedTextField(
                value = selectedPayment.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Payment method") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier =
                    Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
            ) {
                PaymentModel.entries.forEach { payment ->
                    DropdownMenuItem(
                        text = { Text(payment.name) },
                        onClick = {
                            selectedPayment = payment
                            dropdownExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val priceValue = price.toDoubleOrNull() ?: return@Button
                val date = selectedDate ?: return@Button
                if (store.isBlank()) return@Button
                onSave(store, priceValue, LocalDateTime(date, LocalTime(0, 0)), selectedPayment)
            },
            enabled = store.isNotBlank() && price.toDoubleOrNull() != null && selectedDate != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save Snack")
        }

        Spacer(Modifier.height(8.dp))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate =
                                Instant.fromEpochMilliseconds(millis)
                                    .toLocalDateTime(TimeZone.UTC).date
                        }
                        showDatePicker = false
                    },
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
