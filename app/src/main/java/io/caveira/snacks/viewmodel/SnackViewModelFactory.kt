package io.caveira.snacks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.caveira.snacks.data.SnackRepository

class SnackViewModelFactory(private val repository: SnackRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SnackViewModel(repository = repository) as T
    }
}