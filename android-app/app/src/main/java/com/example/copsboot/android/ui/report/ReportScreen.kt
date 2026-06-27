package com.example.copsboot.android.ui.report

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext


@Composable
fun ReportScreen(
    onBackClicked: () -> Unit,
    reportViewModel: ReportViewModel = viewModel()
){

    val uiState = reportViewModel.uiState
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri -> reportViewModel.onImageSelected(uri)
    }
    ReportContent(
        uiState = uiState,
        onDescriptionChanged = reportViewModel::onDescriptionChanged,
        onTrafficIncidentChanged = reportViewModel::onTrafficIncidentChanged,
        onNumberOfInvolvedCarsChanged = reportViewModel::onNumberOfInvolvedCarsChanged,
        onPickImageClicked = {
            imagePickerLauncher.launch("image/*")
        },
        onCreateReportClicked = {
            reportViewModel.createReport(context)
        },
        onCreateAnotherClicked = reportViewModel::clearCreatedReport,
        onBackClicked = onBackClicked
    )
}

@Composable
fun ReportContent(
    uiState: ReportUiState,
    onDescriptionChanged: (String) -> Unit,
    onTrafficIncidentChanged: (Boolean) -> Unit,
    onNumberOfInvolvedCarsChanged: (String) -> Unit,
    onPickImageClicked: () -> Unit,
    onCreateReportClicked: () -> Unit,
    onCreateAnotherClicked: () -> Unit,
    onBackClicked: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ){
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Create Crime Report",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Description must contain the word suspect.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = onDescriptionChanged,
                    label = {Text("Description")},
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Traffic incident",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Switch(
                        checked = uiState.trafficIncident,
                        onCheckedChange = onTrafficIncidentChanged
                    )
                }

                if (uiState.trafficIncident) {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = uiState.numberOfInvolvedCars,
                        onValueChange = onNumberOfInvolvedCarsChanged,
                        label = { Text("Number of involved cars") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onPickImageClicked,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select image")
                }

                if (uiState.selectedImageUri != null) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Image selected.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onCreateReportClicked,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Create report")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onBackClicked,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading
                ) {
                    Text("Back to home")
                }

                if (uiState.message.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (uiState.createdReport != null) {
                    val report = uiState.createdReport

                    val reportId = report.id?.id
                        ?: report.id?.value
                        ?: "Unknown id"

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Created report",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Report ID: $reportId")
                    Text("Reporter: ${report.reporter ?: "Unknown reporter"}")
                    Text("Date/time: ${report.dateTime ?: "Unknown date"}")
                    Text("Description: ${report.description ?: "No description"}")

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onCreateAnotherClicked,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create another report")
                    }
                }

            }
        }
    }
}