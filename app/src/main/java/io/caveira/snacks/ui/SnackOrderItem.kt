package io.caveira.snacks.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.caveira.snacks.data.SnackOrder

@Composable
fun SnackOrderItem(
    snackOrder: SnackOrder,
    onDelete: (SnackOrder) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val date = snackOrder.date.split("T")[0].split("-")
            Text(text = snackOrder.store, modifier = Modifier.weight(1.5f))
            Text(
                text = "R$ %.2f".format(snackOrder.value / 100.0),
                modifier = Modifier.weight(1f),
            )
            Text(text = "${date[2]}/${date[1]}/${date[0].takeLast(2)}", modifier = Modifier.weight(1f))
            Text(text = snackOrder.paymentModel, modifier = Modifier.weight(1f))
            IconButton(onClick = { onDelete(snackOrder) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
