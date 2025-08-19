package com.example.testusoandroidstudio_1_usochicamocha.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.auth.LogoutUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form.GetPendingFormsUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form.SyncFormUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.machine.SyncMachinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado de la UI para la pantalla principal
data class MainUiState(
    val showLogoutDialog: Boolean = false,
    val logoutCompleted: Boolean = false,
    val pendingForms: List<Form> = emptyList(),
    val isSyncingMachines: Boolean = false,
    val syncMachinesMessage: String? = null,
    // --- ESTADOS AÑADIDOS ---
    val isSyncingForms: Boolean = false,
    val syncFormsMessage: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getPendingFormsUseCase: GetPendingFormsUseCase,
    private val syncMachinesUseCase: SyncMachinesUseCase,
    private val syncFormUseCase: SyncFormUseCase // <-- Inyectamos el nuevo UseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observePendingForms()
    }

    private fun observePendingForms() {
        getPendingFormsUseCase().onEach { forms ->
            _uiState.update { it.copy(pendingForms = forms) }
        }.launchIn(viewModelScope)
    }

    fun onLogoutClick() {
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onDismissLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun onConfirmLogout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.update { it.copy(showLogoutDialog = false, logoutCompleted = true) }
        }
    }

    fun onLogoutCompleted() {
        _uiState.update { it.copy(logoutCompleted = false) }
    }

    fun onSyncMachinesClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncingMachines = true, syncMachinesMessage = null) }
            val result = syncMachinesUseCase()
            val message = if (result.isSuccess) "Máquinas sincronizadas con éxito" else result.exceptionOrNull()?.message ?: "Error desconocido"
            _uiState.update { it.copy(isSyncingMachines = false, syncMachinesMessage = message) }
        }
    }

    fun clearSyncMachinesMessage() {
        _uiState.update { it.copy(syncMachinesMessage = null) }
    }

    // --- FUNCIÓN AÑADIDA PARA SINCRONIZAR FORMULARIOS ---
    fun onSyncFormsClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncingForms = true, syncFormsMessage = null) }

            val pendingForms = _uiState.value.pendingForms
            if (pendingForms.isEmpty()) {
                _uiState.update { it.copy(isSyncingForms = false, syncFormsMessage = "No hay formularios para sincronizar.") }
                return@launch
            }

            var successCount = 0
            var errorCount = 0

            pendingForms.forEach { form ->
                val result = syncFormUseCase(form)
                if (result.isSuccess) {
                    successCount++
                } else {
                    errorCount++
                }
            }

            val message = "Sincronización completa. Éxitos: $successCount, Fallos: $errorCount."
            _uiState.update { it.copy(isSyncingForms = false, syncFormsMessage = message) }
        }
    }

    fun clearSyncFormsMessage() {
        _uiState.update { it.copy(syncFormsMessage = null) }
    }
}
