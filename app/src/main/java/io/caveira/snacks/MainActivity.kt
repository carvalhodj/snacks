package io.caveira.snacks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import io.caveira.snacks.data.SnackDatabase
import io.caveira.snacks.data.SnackRepository
import io.caveira.snacks.ui.SnackOrderScreen
import io.caveira.snacks.viewmodel.SnackViewModel
import io.caveira.snacks.viewmodel.SnackViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            context = applicationContext,
            klass = SnackDatabase::class.java,
            name = "snack_db"
        ).build()

        val repository = SnackRepository(db.snackOrderDao())
        val factory = SnackViewModelFactory(repository)

        setContent {
            val viewModel: SnackViewModel = viewModel(factory = factory)
            SnackOrderScreen(viewModel)
        }
    }
}
