package com.example.testusoandroidstudio_1_usochicamocha.ui.form

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testusoandroidstudio_1_usochicamocha.data.local.TokenManager
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Form
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.form.SaveFormUseCase
import com.example.testusoandroidstudio_1_usochicamocha.domain.usecase.machine.GetLocalMachinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

data class FormUiState(
    val horometro: String = "",
    val observaciones: String = "",
    val estadoFugas: String = "",
    val estadoFrenos: String = "",
    val estadoCorreasPoleas: String = "",
    val estadoLlantasCarriles: String = "",
    val estadoEncendido: String = "",
    val estadoElectrico: String = "",
    val estadoMecanico: String = "",
    val estadoTemperatura: String = "",
    val estadoAceite: String = "",
    val estadoHidraulico: String = "",
    val estadoRefrigerante: String = "",
    val estadoEstructural: String = "",
    val vigenciaExtintor: String = "",

    val previewingImageUri: Uri? = null,
    val selectedImageUris: List<Uri> = emptyList(),
    val machines: List<Machine> = emptyList(),
    val selectedMachine: Machine? = null,
    val saveCompleted: Boolean = false
)

@HiltViewModel
class FormViewModel @Inject constructor(
    private val saveFormUseCase: SaveFormUseCase,
    private val getLocalMachinesUseCase: GetLocalMachinesUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getLocalMachinesUseCase().collect { machines ->
                _uiState.update { it.copy(machines = machines) }
            }
        }
    }

    fun onMachineSelected(machine: Machine) {
        _uiState.update { it.copy(selectedMachine = machine) }
    }

    fun onHorometroChange(value: String) {
        _uiState.update { it.copy(horometro = value) }
    }

    fun onObservacionesChange(value: String) {
        _uiState.update { it.copy(observaciones = value) }
    }


    fun onExtinguisherDateChange(year: Int, month: Int) {
        val formattedMonth = String.format("%02d", month + 1)
        _uiState.update { it.copy(vigenciaExtintor = "$year-$formattedMonth") }
    }

    fun onImageSelected(uri: Uri) {
        _uiState.update { it.copy(selectedImageUris = it.selectedImageUris + uri) }
    }

    fun onImageRemoved(uri: Uri) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedImageUris = currentState.selectedImageUris.filter { it != uri },
                previewingImageUri = if (currentState.previewingImageUri == uri) null else currentState.previewingImageUri
            )
        }
    }
    fun onPreviewImageClicked(uri: Uri) {
        _uiState.update { it.copy(previewingImageUri = uri) }
    }

    fun onDismissPreview() {
        _uiState.update { it.copy(previewingImageUri = null) }
    }




    fun onEstadoChange(fieldName: String, value: String) {
        _uiState.update { currentState ->
            when (fieldName) {
                "Fugas" -> currentState.copy(estadoFugas = value)
                "Frenos" -> currentState.copy(estadoFrenos = value)
                "CorreasPoleas" -> currentState.copy(estadoCorreasPoleas = value)
                "LlantasCarriles" -> currentState.copy(estadoLlantasCarriles = value)
                "Encendido" -> currentState.copy(estadoEncendido = value)
                "Electrico" -> currentState.copy(estadoElectrico = value)
                "Mecanico" -> currentState.copy(estadoMecanico = value)
                "Temperatura" -> currentState.copy(estadoTemperatura = value)
                "Aceite" -> currentState.copy(estadoAceite = value)
                "Hidraulico" -> currentState.copy(estadoHidraulico = value)
                "Refrigerante" -> currentState.copy(estadoRefrigerante = value)
                "Estructural" -> currentState.copy(estadoEstructural = value)
                else -> currentState
            }
        }
    }

    fun onSaveClick() {
        if (_uiState.value.selectedMachine == null) {
            Log.d("FormViewModel", "Intento de guardado sin seleccionar máquina.")
            // TODO: Idealmente, aquí deberías actualizar el UiState con un mensaje de error
            // para mostrarlo en un SnackBar o similar, en lugar de un Log.
            return // Salimos de la función
        }
        // Validación: Asegurarse de que se ha seleccionado una máquina.
        val selectedMachineId = _uiState.value.selectedMachine?.id ?: return

        viewModelScope.launch {
            Log.d("FormViewModel", "Iniciando guardado de formulario...")
            val currentState = _uiState.value
            val currentUserId = tokenManager.getUserId().firstOrNull()

            if (currentUserId == null) {
                Log.e("FormViewModel", "Error: No se pudo obtener el ID del usuario.")
                return@launch
            }

            val form = Form(
                localId = 0,
                uuid = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis(),
                equipoId = selectedMachineId,
                usuarioId = currentUserId,
                horometro = currentState.horometro.toDoubleOrNull() ?: 0.0,
                observaciones = currentState.observaciones,
                estadoFugas = currentState.estadoFugas,
                estadoFrenos = currentState.estadoFrenos,
                estadoCorreasPoleas = currentState.estadoCorreasPoleas,
                estadoLlantasCarriles = currentState.estadoLlantasCarriles,
                estadoEncendido = currentState.estadoEncendido,
                estadoElectrico = currentState.estadoElectrico,
                estadoMecanico = currentState.estadoMecanico,
                estadoTemperatura = currentState.estadoTemperatura,
                estadoAceite = currentState.estadoAceite,
                estadoHidraulico = currentState.estadoHidraulico,
                estadoRefrigerante = currentState.estadoRefrigerante,
                estadoEstructural = currentState.estadoEstructural,
                vigenciaExtintor = currentState.vigenciaExtintor
            )
            saveFormUseCase(form)
            Log.d("FormViewModel", "Formulario guardado localmente. Actualizando UI.")
            _uiState.update { it.copy(saveCompleted = true) }
        }
    }

    /**
     * Resetea el estado de guardado para que la navegación no se dispare de nuevo
     * si la pantalla se recompone.
     */
    fun onNavigationDone() {
        _uiState.update { it.copy(saveCompleted = false) }
    }
}
