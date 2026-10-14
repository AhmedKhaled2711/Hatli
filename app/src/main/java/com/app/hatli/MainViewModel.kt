package com.app.hatli

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.hatli.core.domain.usecase.user.ResolveStartDestinationUseCase
import com.app.hatli.core.domain.usecase.user.ResolveStartDestinationUseCase.StartDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val resolveStartDestination: ResolveStartDestinationUseCase,
) : ViewModel() {

    private val _startDestination = MutableStateFlow<StartDestination?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            _startDestination.value = resolveStartDestination()
        }
    }
}
