package io.caveira.snacks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snack_order")
data class SnackOrder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val store: String,
    val value: Int,
    val date: String,
    val paymentModel: String
)