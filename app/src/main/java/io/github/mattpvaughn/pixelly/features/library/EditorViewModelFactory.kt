package io.github.mattpvaughn.pixelly.features.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class EditorViewModelFactory @Inject constructor() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(EditorViewModel::class.java!!)) {
            EditorViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
