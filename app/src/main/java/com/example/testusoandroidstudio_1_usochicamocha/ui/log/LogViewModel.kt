package com.example.testusoandroidstudio_1_usochicamocha.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.LogEntry
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.log.GetLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    getLogsUseCase: GetLogsUseCase
) : ViewModel() {
    // Convertimos el Flow frío a un StateFlow caliente
    val logs: StateFlow<List<LogEntry>> = getLogsUseCase()
        .stateIn(
            scope = viewModelScope, // El StateFlow vivirá mientras viva el ViewModel
            started = SharingStarted.WhileSubscribed(5000), // Empieza a escuchar 5s después de que la UI se suscriba
            initialValue = emptyList() // El valor inicial mientras se cargan los datos
        )
}