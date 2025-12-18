package com.example.tummoccarapptask.presentation.Screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tummoccarapptask.R
import com.example.tummoccarapptask.presentation.model.FilterItem
import com.example.tummoccarapptask.presentation.model.FilterState
import com.example.tummoccarapptask.presentation.model.FilterType
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.UserData
import com.example.tummoccarapptask.presentation.model.VehicleItem
import com.example.tummoccarapptask.presentation.navigation.Screens
import com.example.tummoccarapptask.presentation.theme.BgColorSubtitle
import com.example.tummoccarapptask.presentation.theme.BlueTopBar
import com.example.tummoccarapptask.presentation.theme.Blue_10
import com.example.tummoccarapptask.presentation.theme.Blue_dark
import com.example.tummoccarapptask.presentation.theme.Blue_light
import com.example.tummoccarapptask.presentation.theme.Card1
import com.example.tummoccarapptask.presentation.theme.Card2
import com.example.tummoccarapptask.presentation.theme.HeadingColorTitle
import com.example.tummoccarapptask.presentation.theme.RowBgColor
import com.example.tummoccarapptask.presentation.theme.UnselectedRadioButtonColor
import com.example.tummoccarapptask.presentation.utils.calculateYearDifference
import com.example.tummoccarapptask.presentation.viewmodel.CarViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun HomeRoute(
    navController: NavHostController = rememberNavController(),
    viewModel: CarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filterState by viewModel.filterState.collectAsState()

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = BlueTopBar,
            darkIcons = false
        )
    }

    HomeScreen(
        uiState = uiState,
        filterState = filterState,
        userData = viewModel.userData,
        onAddVehicle = {
            navController.navigate(Screens.AddCar.route)
        },
        onFilterApply = viewModel::applyFilter,
        onFilterClear = viewModel::clearFilter,
        onFilterTypeChange = viewModel::updateFilterType,
        onFilterItemClick = viewModel::toggleFilterItem
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: Resource<List<VehicleItem>>,
    filterState: FilterState,
    userData: UserData,
    onAddVehicle: () -> Unit,
    onFilterApply: () -> Unit,
    onFilterClear: () -> Unit,
    onFilterTypeChange: (FilterType) -> Unit,
    onFilterItemClick: (FilterType, String) -> Unit
) {
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        floatingActionButton = {
            AddVehicleFab(onClick = onAddVehicle)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            TopHeader(userData)

            when (uiState) {
                is Resource.Loading -> CommonLoader()

                is Resource.Error -> EmptyState(uiState.message)

                is Resource.Success -> {
                    VehicleInventoryList(
                        vehicleList = uiState.data,
                        onClickFilter = {
                            showFilterSheet = true
                        }
                    )
                }
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState,
                dragHandle = null,
                containerColor = White
            ) {
                FilterBottomSheet(
                    state = filterState,
                    onTypeChange = onFilterTypeChange,
                    onItemClick = onFilterItemClick,
                    onClearAll = {
                        onFilterClear()
                        showFilterSheet = false
                    },
                    onApply = {
                        onFilterApply()
                        showFilterSheet = false
                    },
                    onDismiss = {

                        showFilterSheet = false

                    }
                )
            }
        }
    }
}

@Composable
fun AddVehicleFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        containerColor = Blue_10,
        contentColor = White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_vehicle),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun VehicleInventoryList(
    vehicleList: List<VehicleItem>,
    onClickFilter: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)),
        contentPadding = PaddingValues(12.dp)
    ) {

        item {
            Text(
                text = stringResource(R.string.vehicle_inventory_list),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = Bold
            )
        }

        item { Spacer(Modifier.height(16.dp)) }

        item { FilterButton(onClickFilter) }

        item { Spacer(Modifier.height(16.dp)) }

        if (vehicleList.isNotEmpty()) {

            item { VehicleTableHeader() }

            items(
                items = vehicleList,
                key = { it.id }
            ) { vehicle ->
                VehicleTableRow(vehicle)
            }

        } else {
            item {
                EmptyState()
            }
        }
    }
}

@Composable
fun EmptyState(msg: String? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val errorMessage = msg ?: stringResource(R.string.no_cars_found)
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

object VehicleTableConfig {
    const val MODEL = 1.2f
    const val NUMBER = 2f
    const val FUEL = 1.2f
    const val YEAR = 1.8f
}

@Composable
fun VehicleTableHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(RowBgColor)
            .border(0.5.dp, LightGray, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .height(IntrinsicSize.Min)
    ) {
        TableCell(stringResource(R.string.model_brand), VehicleTableConfig.MODEL)
        TableCell(stringResource(R.string.vehicle_number), VehicleTableConfig.NUMBER)
        TableCell(
            stringResource(id = R.string.fuel_type),
            VehicleTableConfig.FUEL,

            )
        TableCell(
            stringResource(R.string.year_of_purchase),
            VehicleTableConfig.YEAR,
            showDivider = false
        )
    }
}

@Composable
fun VehicleTableRow(item: VehicleItem) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(White)
            .border(0.5.dp, LightGray)
            .height(IntrinsicSize.Min)
    ) {
        TableCell(item.model, VehicleTableConfig.MODEL, showSubtite = item.brand)
        TableCell(
            item.number,
            VehicleTableConfig.NUMBER,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = Bold,
                color = Color(0xFF1570EF)
            )
        )
        TableCell(item.fuelType, VehicleTableConfig.FUEL)
        TableCell(item.year, VehicleTableConfig.YEAR, showDivider = false, year = item.year.toInt())
    }
}

@Composable
fun FilterItemsColumn(
    items: List<FilterItem>,
    onItemClick: (String) -> Unit
) {
    LazyColumn {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            FilterItemRow(item, onItemClick)
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Composable
fun FilterItemRow(
    item: FilterItem,
    onItemClick: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(BgColorSubtitle)
            .clickable { onItemClick(item.id) }
            .padding(vertical = 3.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            item.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = W500,
                fontSize = 12.sp
            ),
            color = BlueTopBar
        )
        Checkbox(
            checked = item.isSelected,
            onCheckedChange = { onItemClick(item.id) },
            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
            colors = CheckboxDefaults.colors(
                checkedColor = Blue_10,
                uncheckedColor = Color.Gray,
                checkmarkColor = White,
                disabledCheckedColor = LightGray
            )
        )
    }
}


@Composable
fun FilterButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = onClick
            )
            .border(1.dp, LightGray, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.outline_filter_alt_24),
            contentDescription = stringResource(R.string.filter)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = stringResource(R.string.filter),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = W500)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    showDivider: Boolean = true,
    color: Color = BlueTopBar,
    textStyle: TextStyle? = null,
    showSubtite: String? = null,
    year: Int? = null
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = text,
                style = textStyle ?: MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = W500,
                    color = color
                )
            )
            if (year != null) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(
                    text = calculateYearDifference(year),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 10.sp,
                        fontWeight = W400,
                        color = Blue_light
                    )
                )
            }
            if (showSubtite != null) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(
                    text = showSubtite,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 10.sp,
                        fontWeight = W400,
                        color = Blue_light
                    )
                )
            }
        }
    }


    if (showDivider) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(LightGray)
        )
    }
}


@Composable
fun TopHeader(userData: UserData) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BlueTopBar)
        //
        // .padding(top = innerPadding.calculateTopPadding())

    ) {

        Column {

            Row(modifier = Modifier.padding(16.dp)) {

                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.height(50.dp)) {

                    Text(
                        text = "Hi, ${userData.name} 👋",
                        color = White,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp)
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = stringResource(R.string.welcome_back),
                        color = White.copy(0.6f),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
                    )

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth()) {

                BoxContainer(
                    text = stringResource(R.string.total_vehicle),
                    emoji = R.drawable.car,
                    count = userData.totalVehicle,
                    bgColor = Card1,
                    modifier = Modifier.weight(1f)
                )

                BoxContainer(
                    text = stringResource(R.string.total_ev),
                    emoji = R.drawable.img_electric,
                    count = userData.totalEv,
                    bgColor = Card2,
                    modifier = Modifier.weight(1f)
                )

            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}


@Composable
fun BoxContainer(
    text: String,
    emoji: Int,
    count: String,
    bgColor: Color,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(vertical = 14.dp, horizontal = 16.dp)
    ) {

        Column {

            Image(painter = painterResource(emoji), "", modifier = Modifier.size(20.dp))


            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = count,
                color = Blue_dark,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = W900)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                color = Blue_light,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = W500
                )
            )
        }
    }
}

@Composable
fun FilterBottomSheet(
    state: FilterState,
    onTypeChange: (FilterType) -> Unit,
    onItemClick: (FilterType, String) -> Unit,
    onClearAll: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.filter),
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
                    .clickable { onDismiss() }
            )
        }

        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        Row {
            FilterTypeColumn(
                selectedType = state.selectedType,
                onTypeChange = onTypeChange
            )

            Spacer(Modifier.width(16.dp))

            FilterItemsColumn(
                items = if (state.selectedType == FilterType.BRAND)
                    state.brands else state.fuels,
                onItemClick = {
                    onItemClick(state.selectedType, it)
                }
            )
        }

        Spacer(Modifier.height(24.dp))
        ClearApplyButtons(
            onClearAllClick = { onClearAll() },
            onApplyClick = { onApply() }
        )

    }
}

@Composable
fun ClearApplyButtons(
    onClearAllClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onClearAllClick,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, UnselectedRadioButtonColor)
        ) {
            Text(
                text = stringResource(R.string.clear_all),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = W500,
                    fontSize = 16.sp
                ),
                color = Blue_light
            )
        }

        Button(
            onClick = onApplyClick,
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A7CF5)) // Blue background
        ) {
            Text(
                text = stringResource(R.string.apply),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = W500,
                    fontSize = 16.sp
                ),
                color = White
            )
        }
    }
}


@Composable
fun FilterTypeColumn(
    selectedType: FilterType,
    onTypeChange: (FilterType) -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(top = 8.dp)
    ) {
        FilterType.entries.forEach { it ->
            Text(
                text = it.name.lowercase().replaceFirstChar { it.uppercaseChar() },
                color = if (it == selectedType) Blue_10 else Blue_dark,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = W500,
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onTypeChange(it)
                    }

            )
            HorizontalDivider(thickness = 1.dp)
        }
    }
}
