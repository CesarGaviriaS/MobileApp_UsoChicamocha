package com.example.testusoandroidstudio_1_usochicamocha.ui.form

import android.Manifest
import android.app.DatePickerDialog
import android.net.Uri
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.testusoandroidstudio_1_usochicamocha.domain.model.Machine
import java.io.File
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: FormViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- LÓGICA PARA SELECCIONAR IMÁGENES ---

    // 1. Launcher para la galería de fotos
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onImageSelected(it) }
        }
    )

    // 2. Launcher para la cámara
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempImageUri?.let { viewModel.onImageSelected(it) }
            }
        }
    )

    // 3. Launcher para pedir permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Si se concede el permiso, lanza la cámara
                val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                tempImageUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                takePictureLauncher.launch(tempImageUri)
            }
        }
    )

    // Navega hacia atrás cuando el guardado se completa.
    LaunchedEffect(uiState.saveCompleted) {
        if (uiState.saveCompleted) {
            onNavigateBack()
            viewModel.onNavigationDone() // Resetea el estado para evitar re-navegación
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inspección Maquinaria") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dropdown para seleccionar la máquina
            item {
                MachineSelector(
                    machines = uiState.machines,
                    selectedMachine = uiState.selectedMachine,
                    onMachineSelected = { viewModel.onMachineSelected(it) }
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.horometro,
                    onValueChange = { viewModel.onHorometroChange(it) },
                    label = { Text("Escriba el HOROMETRO Actual") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --- Lista completa de todos los StatusSelector ---
            item { StatusSelector("Fugas en el Sistema", uiState.estadoFugas) { viewModel.onEstadoChange("Fugas", it) } }
            item { StatusSelector("Sistema de Frenos", uiState.estadoFrenos) { viewModel.onEstadoChange("Frenos", it) } }
            item { StatusSelector("Estado de Correas y Poleas", uiState.estadoCorreasPoleas) { viewModel.onEstadoChange("CorreasPoleas", it) } }
            item { StatusSelector("Estado de Llantas y/o Carriles", uiState.estadoLlantasCarriles) { viewModel.onEstadoChange("LlantasCarriles", it) } }
            item { StatusSelector("Sistema de Encendido", uiState.estadoEncendido) { viewModel.onEstadoChange("Encendido", it) } }
            item { StatusSelector("Sistema Eléctrico en General", uiState.estadoElectrico) { viewModel.onEstadoChange("Electrico", it) } }
            item { StatusSelector("Sistema Mecánico en General", uiState.estadoMecanico) { viewModel.onEstadoChange("Mecanico", it) } }
            item { StatusSelector("Nivel de Temperatura", uiState.estadoTemperatura) { viewModel.onEstadoChange("Temperatura", it) } }
            item { StatusSelector("Nivel de Aceite", uiState.estadoAceite) { viewModel.onEstadoChange("Aceite", it) } }
            item { StatusSelector("Nivel de Hidraulico", uiState.estadoHidraulico) { viewModel.onEstadoChange("Hidraulico", it) } }
            item { StatusSelector("Nivel de Refrigerante", uiState.estadoRefrigerante) { viewModel.onEstadoChange("Refrigerante", it) } }
            item { StatusSelector("Estado Estructural en General", uiState.estadoEstructural) { viewModel.onEstadoChange("Estructural", it) } }

            item {
                ExtinguisherDatePicker(
                    selectedDate = uiState.vigenciaExtintor,
                    onDateSelected = { year, month ->
                        viewModel.onExtinguisherDateChange(year, month)
                    }
                )
            }
            item {
                OutlinedTextField(
                    value = uiState.observaciones,
                    onValueChange = { viewModel.onObservacionesChange(it) },
                    label = { Text("Observaciones y/o Aspectos a Revisar") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )
            }
            item {
                ImageUploadSection(
                    selectedImageUris = uiState.selectedImageUris,
                    onPickImageClick = { pickImageLauncher.launch("image/*") },
                    onTakePhotoClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    onRemoveImageClick = { viewModel.onImageRemoved(it) },
                    onViewImageClick = { viewModel.onPreviewImageClicked(it) }
                )
            }

            item {
                Button(
                    onClick = { viewModel.onSaveClick() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.selectedMachine != null
                ) {
                    Text("Guardar")
                }
            }
        }
    }

    if (uiState.previewingImageUri != null) {
        ImagePreviewDialog(
            uri = uiState.previewingImageUri!!,
            onDismiss = { viewModel.onDismissPreview() },
            onDelete = { viewModel.onImageRemoved(uiState.previewingImageUri!!) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineSelector(
    machines: List<Machine>,
    selectedMachine: Machine?,
    onMachineSelected: (Machine) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = selectedMachine?.let { "${it.name} - ${it.model} - ${it.identifier}" } ?: "Seleccione una máquina"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Máquina") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            machines.forEach { machine ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(text = machine.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Model: ${machine.model} - ID: ${machine.identifier}", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    onClick = {
                        onMachineSelected(machine)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StatusSelector(
    label: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val options = listOf("Óptimo", "Regular", "Malo")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                val backgroundColor = when (option) {
                    "Óptimo" -> Color(0xFF4CAF50)
                    "Regular" -> Color(0xFFFFA000)
                    "Malo" -> Color(0xFFD32F2F)
                    else -> Color.Gray
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) backgroundColor else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onOptionSelected(option) },
                    shape = RoundedCornerShape(8.dp),
                    color = backgroundColor.copy(alpha = if (isSelected) 0.9f else 0.3f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = option,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExtinguisherDatePicker(
    selectedDate: String,
    onDateSelected: (year: Int, month: Int) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = selectedDate.substringBefore("-").toIntOrNull() ?: calendar.get(Calendar.YEAR)
    val month = selectedDate.substringAfter("-").toIntOrNull()?.minus(1) ?: calendar.get(Calendar.MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, _: Int ->
            onDateSelected(selectedYear, selectedMonth)
        }, year, month, calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Vigencia EXTINTOR", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de Vencimiento (YYYY-MM)") },
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ImageUploadSection(
    selectedImageUris: List<Uri>,
    onPickImageClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onRemoveImageClick: (Uri) -> Unit,
    onViewImageClick: (Uri) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Subir imágenes", style = MaterialTheme.typography.titleLarge)

            if (selectedImageUris.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImageUris) { uri ->
                        SelectedImageItem(
                            uri = uri,
                            onRemoveClick = { onRemoveImageClick(uri) },
                            onViewClick = { onViewImageClick(uri) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onPickImageClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Subir foto")
                }
                OutlinedButton(
                    onClick = onTakePhotoClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Tomar foto")
                }
            }
        }
    }
}

@Composable
fun SelectedImageItem(
    uri: Uri,
    onRemoveClick: () -> Unit,
    onViewClick: () -> Unit
) {
    val fileName = uri.lastPathSegment ?: "imagen.jpg"

    ListItem(
        headlineContent = { Text(fileName, maxLines = 1) },
        leadingContent = {
            IconButton(onClick = onViewClick) {
                Icon(Icons.Default.Visibility, contentDescription = "Ver imagen")
            }
        },
        trailingContent = {
            TextButton(onClick = onRemoveClick) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Eliminar", color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

@Composable
fun ImagePreviewDialog(
    uri: Uri,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Previsualización",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(8.dp))

                AsyncImage(
                    model = uri,
                    contentDescription = "Imagen previsualizada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Volver")
                    }
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
