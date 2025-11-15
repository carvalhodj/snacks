package io.caveira.snacks.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SnackOrder::class], version = 1)
abstract class SnackDatabase : RoomDatabase() {
    abstract fun snackOrderDao(): SnackOrderDao
}