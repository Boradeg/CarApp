package com.example.tummoccarapptask.presentation.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.font.FontWeight.Companion.W900
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.util.copy
import com.example.tummoccarapptask.R
import com.example.tummoccarapptask.data.repository.FilterItem
import com.example.tummoccarapptask.data.repository.FilterType
import com.example.tummoccarapptask.presentation.model.FilterState
import com.example.tummoccarapptask.presentation.model.Resource
import com.example.tummoccarapptask.presentation.model.UserData
import com.example.tummoccarapptask.presentation.model.VehicleItem
import com.example.tummoccarapptask.presentation.navigation.Screen
import com.example.tummoccarapptask.presentation.theme.BlueTopBar
import com.example.tummoccarapptask.presentation.theme.Blue_10
import com.example.tummoccarapptask.presentation.theme.Blue_dark
import com.example.tummoccarapptask.presentation.theme.Blue_light
import com.example.tummoccarapptask.presentation.theme.Card1
import com.example.tummoccarapptask.presentation.theme.Card2
import com.example.tummoccarapptask.presentation.theme.RowBgColor
import com.example.tummoccarapptask.presentation.viewmodel.CarViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: CarViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val filterState by viewModel.filterState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFilterSheet by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    val userdata = viewModel.userData

    SideEffect {
        systemUiController.setStatusBarColor(
            color = BlueTopBar,
            darkIcons = false
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddCar.route)
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                containerColor = Blue_10,
                contentColor = White,
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_vehicle),
                        tint = White
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Increased spacer
                    Text(
                        text = stringResource(R.string.add_vehicle),
                        style = MaterialTheme.typography.labelLarge // labelLarge is better for buttons
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            TopHeader(innerPadding, userdata)
            when (uiState) {
                is Resource.Loading -> {
                    CommonLoader()
                }
                is Resource.Success -> {
                    val cars = (uiState as Resource.Success).data
                    VehicleInventoryList(cars, onClickFilter = {
                        showFilterSheet = true
                    })
                }

                is Resource.Error -> Text("Error: ${(uiState as Resource.Error).message}")
            }
        }
        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState
            ) {
                FilterBottomSheet(
                    state = filterState,

                    onTypeChange = { type ->
                        viewModel.updateFilterType(type)
                    },

                    onItemClick = { type, id ->
                        viewModel.toggleFilterItem(type, id)
                    },

                    onApply = {
                        viewModel.applyFilter()
                        showFilterSheet = false
                    },

                    onClearAll = {
                        viewModel.clearFilter()
                        showFilterSheet = false
                    }
                )
            }
        }
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


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    showDivider: Boolean = true,
    color: Color = BlueTopBar,
    textStyle: TextStyle? = null
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = textStyle ?: MaterialTheme.typography.labelMedium.copy(
                fontSize = 12.sp,
                fontWeight = W500,
                color = color
            )
        )
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
fun VehicleTableRow(item: VehicleItem) {
    val column1Weight = 1.2f
    val column2Weight = 2.0f
    val column3Weight = 1.2f
    val column4Weight = 1.8f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .border(0.5.dp, LightGray)
            .height(IntrinsicSize.Min)
    ) {
        TableCell(item.model, column1Weight)
        TableCell(item.number, column2Weight,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = W700,
                color  = Color(0xFF1570EF)
            ))
        TableCell(item.fuelType, column3Weight)
        TableCell(item.year, column4Weight, showDivider = false)
    }
}


@Composable
fun VehicleInventoryList(vehicleList: List<VehicleItem>, onClickFilter: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB)),
        contentPadding = PaddingValues(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.vehicle_inventory_list),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            FilterButton { onClickFilter() }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (vehicleList.size > 0) {
            item {
                VehicleTableHeader()
            }

            items(items = vehicleList) { vehicle ->
                Log.d("TAG", "VehicleInventoryListScreen: $vehicle")
                VehicleTableRow(item = vehicle)
            }

        } else {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.no_cars_found),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleTableHeader() {
    val column1Weight = 1.2f
    val column2Weight = 2.0f
    val column3Weight = 1.2f
    val column4Weight = 1.8f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(RowBgColor)
            .border(0.5.dp, LightGray, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .height(IntrinsicSize.Min)
    ) {
        TableCell(text = stringResource(id = R.string.model_brand), weight = column1Weight,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = W500,
                color = BlueTopBar
            ))
        TableCell(text = stringResource(R.string.vehicle_number), weight = column2Weight)
        TableCell(text = stringResource(R.string.fuel_type), weight = column3Weight)
        TableCell(
            text = stringResource(R.string.year_of_purchase),
            weight = column4Weight,
            showDivider = false
        )

    }
}

@Composable
fun TopHeader(innerPadding: PaddingValues, userData: UserData) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(BlueTopBar)
            .padding(top = innerPadding.calculateTopPadding())

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
    onApply: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Filter",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(Modifier.height(16.dp))

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onClearAll,
                modifier = Modifier.weight(1f)
            ) {
                Text("Clear all")
            }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = onApply,
                modifier = Modifier.weight(1f)
            ) {
                Text("Apply")
            }
        }
    }
}

@Composable
fun FilterItemsColumn(
    items: List<FilterItem>,
    onItemClick: (String) -> Unit
) {
    Column {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.id) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f)
                )
                Checkbox(
                    checked = item.isSelected,
                    onCheckedChange = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Composable
fun FilterTypeColumn(
    selectedType: FilterType,
    onTypeChange: (FilterType) -> Unit
) {
    Column(
        modifier = Modifier.width(100.dp)
    ) {
        FilterType.values().forEach {
            Text(
                text = it.name.replaceFirstChar { c -> c.uppercase() },
                color = if (it == selectedType) Color(0xFF1381FF) else Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .clickable { onTypeChange(it) }
            )
        }
    }
}
