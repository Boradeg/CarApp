package com.example.tummoccarapptask.presentation.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
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
import com.example.tummoccarapptask.presentation.theme.GrayHeading
import com.example.tummoccarapptask.presentation.theme.HeadingColorTitle
import com.example.tummoccarapptask.presentation.theme.LablePlaceholder
import com.example.tummoccarapptask.presentation.theme.UnselectedRadioButtonColor
import com.example.tummoccarapptask.presentation.viewmodel.CarViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: CarViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var showBrandSheet by remember { mutableStateOf(false) }
    var openFuelSheet by remember { mutableStateOf(false) }
    var openModelSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Listen for SharedFlow events
    LaunchedEffect(Unit) {
        viewModel.submitState.collect { state ->
            isLoading = state is SubmitState.Loading

            when (state) {
                is SubmitState.Success -> {
                    Toast.makeText(context, "Vehicle Added!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

                is SubmitState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = White,
            darkIcons = true
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                },
                title = {
                    Text(
                        stringResource(R.string.add_vehicle),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
        },
        containerColor = White
    ) { padding ->

        if (isLoading) {
            CommonLoader()
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.vehical_details),
                fontWeight = W700,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.satoshi_variable)),
                color = GrayHeading
            )
            Spacer(modifier = Modifier.padding(top = 10.dp))
            CommonInputField(
                stringResource(R.string.select_a_brand),
                label = stringResource(R.string.brand),
                value = formState.brand,
                onClick = { showBrandSheet = true },
                readOnly = true,
            )



            Spacer(Modifier.height(12.dp))


            CommonInputField(
                stringResource(R.string.select_a_model),
                label = stringResource(R.string.model),
                value = formState.model,
                onClick = { openModelSheet = true },
                readOnly = true,
            )
            Spacer(Modifier.height(12.dp))

            CommonInputField(
                stringResource(R.string.txt_select_fuel_type),
                label = stringResource(R.string.fuel_type),
                value = formState.fuelType,
                onClick = { openFuelSheet = true },
                readOnly = true,
            )

            Spacer(Modifier.height(12.dp))

            CommonInputField(
                stringResource(R.string.enter_vehicle_number_e_g_mh_12_ab_1234),
                label = stringResource(R.string.vehicle_number),
                value = formState.vehicleNumber,
                onValueChange = viewModel::updateVehicleNumber
            )


            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.other_details),

                fontWeight = W700,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.satoshi_variable)),
                color = GrayHeading
            )
            Spacer(Modifier.height(12.dp))

            CommonInputField(
                stringResource(R.string.select_year_of_purchase),

                label = stringResource(R.string.year_of_purchase),
                value = formState.yearOfPurchase,
                onValueChange = viewModel::updateYearOfPurchase
            )

            Spacer(Modifier.height(12.dp))

            CommonInputField(
                stringResource(R.string.enter_owner_s_full_name),

                label = stringResource(R.string.owner_name),
                value = formState.ownerName,
                onValueChange = viewModel::updateOwnerName
            )

            Spacer(Modifier.height(24.dp))
            PrimaryButton(
                stringResource(R.string.add_vehicle),
                enabled = !isLoading
            ) {
                viewModel.addCar()
            }
        }
    }


    if (showBrandSheet) {
        CommonSelectionBottomSheet(
            title = stringResource(R.string.select_vehicle_brand),
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
            title = stringResource(R.string.select_vehicle_model),
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
            title = stringResource(R.string.select_fuel_type),
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
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1381FF),
            contentColor = White,
            disabledContainerColor = Color(0xFF1381FF).copy(alpha = 0.5f),
            disabledContentColor = White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = Bold,
            fontFamily = FontFamily(Font(R.font.satoshi_variable))
        )
    }
}

@Composable
fun CommonLoader() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CommonInputField(
    hint: String,
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .then(
                if (onClick != null)
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onClick() }
                else Modifier
            )
    ) {
        val borderColor = Color(0xFFE6E6E6)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            enabled = onClick == null,

            label = {
                Text(
                    text = label,
                    color = LablePlaceholder,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = W500,
                        fontSize = 14.sp
                    )
                )
            },

            trailingIcon = {
                if (readOnly) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = GrayHeading
                    )
                }
            },
            placeholder = {
                //hint
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        color = GrayHeading,
                        fontWeight = W400,
                        fontSize = 16.sp,
                    )
                }
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = BlueTopBar,
                fontWeight = W700,
                fontSize = 16.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(White),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                disabledBorderColor = borderColor,
                errorBorderColor = borderColor,
                cursorColor = Color.Black
            )

        )
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
        containerColor = White
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


