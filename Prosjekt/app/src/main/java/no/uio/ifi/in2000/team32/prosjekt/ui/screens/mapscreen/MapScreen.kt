package no.uio.ifi.in2000.team32.prosjekt.ui.screens.mapscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.team32.prosjekt.model.Resource
import no.uio.ifi.in2000.team32.prosjekt.ui.components.buttons.ShowSwimmingSpotsButton
import no.uio.ifi.in2000.team32.prosjekt.ui.components.buttons.UserLocationButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // No need for innerPadding in this scenario
@Composable
fun MapScreen(
    navController: NavHostController, mapViewModel: MapViewModel
) {
    val suggestions = mapViewModel.searchSuggestions.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Remember mutableStateOfs
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val selectedSuggestionIndex by remember { mutableIntStateOf(-1) }
    val isDropdownExpanded = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val networkIsAvailable by mapViewModel.isNetworkAvailable.collectAsState()
    val errorMessage = mapViewModel.errorMessage.collectAsState().value

    val userLocation = mapViewModel.userLocation.collectAsState()

    //Viser Snackbar hvis internett blir borte.
    LaunchedEffect(networkIsAvailable) {
        if (!networkIsAvailable) {
            snackbarHostState.showSnackbar(
                message = errorMessage, duration = SnackbarDuration.Long
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                val hasLocation = (userLocation.value is Resource.Success<*>)
                UserLocationButton(mapViewModel, snackbarHostState, hasLocation)

                Spacer(modifier = Modifier.height(12.dp))

                ShowSwimmingSpotsButton(mapViewModel)
            }
        },
        topBar = {
            TopBar(navController)
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    SearchBar(
                        searchQuery,
                        selectedSuggestionIndex,
                        suggestions,
                        isDropdownExpanded,
                        mapViewModel,
                        keyboardController
                    )
                }
            }
        },
    ) {
        MapDisplay(
            mapViewModel, navController, snackbarHostState, networkIsAvailable, userLocation
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("") }, // Vil ikke egentlig ha tekst
        actions = {
            IconButton(onClick = { navController.navigate("help") }) {
                Icon(Icons.Filled.Info, contentDescription = "Help") // HELP OUTLINE?
            }
        },
    )
}

@Composable
fun SearchBar(
    searchQuery: MutableState<String>,
    selectedSuggestionIndex: Int,
    suggestions: State<List<String>>,
    isDropdownExpanded: MutableState<Boolean>,
    mapViewModel: MapViewModel,
    keyboardController: SoftwareKeyboardController?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedTextField(value = searchQuery.value,
                onValueChange = { newValue ->
                    searchQuery.value = newValue
                    if (newValue.isNotEmpty() && newValue.length >= 3) {
                        mapViewModel.fetchSuggestions(newValue)
                        isDropdownExpanded.value = true
                    } else {
                        isDropdownExpanded.value = false
                    }
                },
                placeholder = { Text("Søk etter by") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {

                    if (searchQuery.value.isNotEmpty()) {                       //Gjør slik at app ikke kræsjer hvis bruker trykker "tom enter" i søkefelt
                        if (selectedSuggestionIndex >= 3) {
                            mapViewModel.searchCity(suggestions.value[selectedSuggestionIndex])
                        } else {
                            mapViewModel.searchCity(searchQuery.value)
                        }
                    }
                    keyboardController?.hide()
                    searchQuery.value = ""
                    isDropdownExpanded.value = false
                }),
                modifier = Modifier.weight(1f)
            )
        }


        // Dropdown menu for autocomplete suggestions
        DropdownMenu(
            expanded = isDropdownExpanded.value && suggestions.value.isNotEmpty(),
            onDismissRequest = { isDropdownExpanded.value = false },
            properties = PopupProperties(focusable = false), // Gjør at dropdown ikke tar fokus
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            suggestions.value.forEach { suggestion ->
                DropdownMenuItem(text = { Text(suggestion) }, onClick = {
                    searchQuery.value = suggestion
                    isDropdownExpanded.value = false
                    mapViewModel.searchCity(suggestion)
                })
            }
        }
    }
}
