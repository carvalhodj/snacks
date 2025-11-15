package io.caveira.snacks.data

import kotlinx.coroutines.flow.Flow

class SnackRepository(
    private val dao: SnackOrderDao
) {
    val allSnackOrders: Flow<List<SnackOrder>> = dao.getAllSnackOrders()

    suspend fun saveSnackOrder(snackOrder: SnackOrder) = dao.saveSnackOrder(snackOrder = snackOrder)
    suspend fun deleteSnackOrder(snackOrder: SnackOrder) = dao.deleteSnackOrder(snackOrder = snackOrder)
    suspend fun updateSnackOrder(snackOrder: SnackOrder) = dao.saveSnackOrder(snackOrder = snackOrder)
}