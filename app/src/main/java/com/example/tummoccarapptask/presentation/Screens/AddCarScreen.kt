package com.example.tummoccarapptask.presentation.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tummoccarapptask.R
import com.example.tummoccarapptask.presentation.model.SelectionItem
import com.example.tummoccarapptask.presentation.model.SubmitState
import com.example.tummoccarapptask.presentation.theme.BlueTopBar
import com.example.tummoccarapptask.presentation.theme.Blue_10
import com.example.tummoccarapptask.presentation.theme.Blue_dark
import com.example.tummoccarapptask.presentation.theme.GrayHeading
import com.example.tummoccarapptask.presentation.theme.HeadingColorTitle
import com.example.tummoccarapptask.presentation.theme.UnselectedRadioButtonColor
import com.example.tummoccarapptask.presentation.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: CarViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()

    var showBrandSheet by remember { mutableStateOf(false) }
    var openFuelSheet by remember { mutableStateOf(false) }
    var openModelSheet by remember { mutableStateOf(false) }
    val submitState by viewModel.submitState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(submitState) {
        when (submitState) {
            is SubmitState.Error -> {
                Toast.makeText(
                    context,
                    (submitState as SubmitState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetSubmitState()
            }

            is SubmitState.Success -> {
                Toast.makeText(
                    context,
                    "Vehicle added successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
                viewModel.resetSubmitState()
            }

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                },
                title = { Text("Add Vehicle") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            SelectableInputField(
                label = "Brand",
                value = formState.brand,
                onClick = { showBrandSheet = true }
            )

            Spacer(Modifier.height(12.dp))

            SelectableInputField(
                label = "Model",
                value = formState.model,
                onClick = { openModelSheet = true }
            )

            Spacer(Modifier.height(12.dp))

            SelectableInputField(
                label = "Fuel Type",
                value = formState.fuelType,
                onClick = { openFuelSheet = true }
            )

            Spacer(Modifier.height(12.dp))

            CommonInputField(
                label = "Vehicle Number",
                value = formState.vehicleNumber,
                onValueChange = viewModel::updateVehicleNumber
            )

            Spacer(Modifier.height(12.dp))

            CommonInputField(
                label = "Year of Purchase",
                value = formState.yearOfPurchase,
                onValueChange = viewModel::updateYearOfPurchase
            )

            Spacer(Modifier.height(12.dp))

            CommonInputField(
                label = "Owner Name",
                value = formState.ownerName,
                onValueChange = viewModel::updateOwnerName
            )

            Spacer(Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.addCar() }
            ) {
                Text("Add Vehicle")
            }
        }
    }

    // -------- Bottom Sheets --------

    if (showBrandSheet) {
        CommonSelectionBottomSheet(
            title = "Select Vehicle Brand",
            items = viewModel.brandList,
            selectedItem = formState.brand,
            onItemSelected = {
                viewModel.updateBrand(it)
                showBrandSheet = false
            },
            onClose = { showBrandSheet = false }
        )
    }

    if (openModelSheet) {
        CommonSelectionBottomSheet(
            title = "Select Vehicle Model",
            items = viewModel.modelList,
            selectedItem = formState.model,
            onItemSelected = {
                viewModel.updateModel(it)
                openModelSheet = false
            },
            onClose = { openModelSheet = false }
        )
    }

    if (openFuelSheet) {
        CommonSelectionBottomSheet(
            title = "Select Fuel Type",
            items = viewModel.fuelList,
            selectedItem = formState.fuelType,
            onItemSelected = {
                viewModel.updateFuelType(it)
                openFuelSheet = false
            },
            onClose = { openFuelSheet = false }
        )
    }
}


@Composable
fun CommonInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium)

        },
        textStyle = MaterialTheme.typography.titleMedium,

        readOnly = readOnly,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    )
}


@Composable
fun SelectableInputField(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            OutlinedTextField(
                value = value,

                textStyle = MaterialTheme.typography.titleMedium,
                onValueChange = {},
                enabled = false,
                readOnly = true,
                placeholder = {
                    Text(
                        text = "Select $label",
                        color = if (value.isEmpty()) GrayHeading else Blue_dark,
                        style = if (value.isEmpty()) {
                            MaterialTheme.typography.titleMedium.copy(fontWeight = Normal)
                        } else {
                            MaterialTheme.typography.titleMedium.copy(fontWeight = Bold)
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonSelectionBottomSheet(
    title: String,
    items: List<SelectionItem>,
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    onClose: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onClose() },
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Color.White
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = Bold,
                    fontSize = 16.sp
                ),
                color = HeadingColorTitle
            )

            Icon(
                imageVector = (Icons.Default.Close),
                contentDescription = stringResource(R.string.close),
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onClose() }
            )
        }
        HorizontalDivider(thickness = 1.dp)
        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach { item ->

                val isSelected = item.title == selectedItem

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .border(
                            width = if (isSelected) 1.dp else 1.dp,
                            color = if (isSelected) Color(0xFF0061FF) else Color(0xFFE6E6E6),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onItemSelected(item.title) }
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    item.icon?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )

                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = item.title,
                        style = if (!isSelected) {
                            MaterialTheme.typography.labelMedium.copy(fontSize = 16.sp)
                        } else {
                            MaterialTheme.typography.labelMedium.copy(
                                fontWeight = Bold,
                                fontSize = 16.sp
                            )
                        },
                        modifier = Modifier.weight(1f),
                        color = BlueTopBar
                    )

                    RadioButton(
                        selected = isSelected,
                        onClick = { onItemSelected(item.title) },
                        colors = RadioButtonColors(
                            selectedColor = Blue_10,
                            unselectedColor = UnselectedRadioButtonColor,
                            disabledSelectedColor = Blue_10,
                            disabledUnselectedColor = UnselectedRadioButtonColor
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}


