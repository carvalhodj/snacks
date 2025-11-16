package io.caveira.snacks.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SnackOrderDao {
    @Query("SELECT * FROM snack_order")
    fun getAllSnackOrders(): Flow<List<SnackOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSnackOrder(snackOrder: SnackOrder)

    @Delete
    suspend fun deleteSnackOrder(snackOrder: SnackOrder)
}
