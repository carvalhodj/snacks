package io.caveira.snacks.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.caveira.snacks.data.model.PaymentModel
import io.caveira.snacks.viewmodel.SnackViewModel
import kotlinx.datetime.LocalDateTime

@Composable
fun SnackOrderScreen(viewModel: SnackViewModel) {
    val snackOrders by viewModel.snackOrders.collectAsState()
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Enter a text") },
                modifier = Modifier.weight(1f)
            )
            Button(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.height(50.dp),
                onClick = {
                    if (text.isNotBlank()) {
                        val elements = text.split(",")
                        viewModel.saveSnackOrder(
                            elements[0], elements[1].toInt(), LocalDateTime.parse(elements[2]),
                            PaymentModel.valueOf(elements[3])
                        )
                        text = ""
                    }
                }
            ) {
                Text("Save")
            }
        }

        LazyColumn(modifier = Modifier.padding(top = 20.dp)) {
            items(snackOrders) { snackOrder ->
                SnackOrderItem(
                    snackOrder = snackOrder,
                    onDelete = { viewModel.deleteSnackOrder(snackOrder) }
                )
            }
        }
    }
}