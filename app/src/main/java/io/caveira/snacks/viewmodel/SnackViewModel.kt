package io.caveira.snacks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.caveira.snacks.data.SnackOrder
import io.caveira.snacks.data.SnackRepository
import io.caveira.snacks.data.model.PaymentModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class SnackViewModel(private val repository: SnackRepository) : ViewModel() {
    val snackOrders: StateFlow<List<SnackOrder>> =
        repository.allSnackOrders.map {
            it.sortedByDescending { snackOrder -> snackOrder.id }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice: StateFlow<Double> =
        repository.allSnackOrders
            .map { orders -> orders.sumOf { it.value } / 100.0 }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun saveSnackOrder(
        store: String,
        value: Double,
        date: LocalDateTime,
        paymentModel: PaymentModel,
    ) = viewModelScope.launch {
        repository.saveSnackOrder(
            SnackOrder(
                store = store,
                value = (value * 100).toInt(),
                date = date.toString(),
                paymentModel = paymentModel.toString(),
            ),
        )
    }

    fun deleteSnackOrder(snackOrder: SnackOrder) =
        viewModelScope.launch {
            repository.deleteSnackOrder(snackOrder = snackOrder)
        }

    fun updateSnackOrder(snackOrder: SnackOrder) =
        viewModelScope.launch {
            repository.updateSnackOrder(snackOrder = snackOrder)
        }
}
