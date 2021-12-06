package com.adyen.android.assignment.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import com.adyen.android.assignment.api.model.Resource
import com.adyen.android.assignment.api.model.Status
import com.adyen.android.assignment.api.model.search.Result
import com.adyen.android.assignment.ui.ui.theme.AndroidassignmentTheme
import com.adyen.android.assignment.ui.ui.theme.BottomSheetShape
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: PlacesViewModel by viewModels()

    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocation()
        setContent {
            AndroidassignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BottomSheetAppScaffold(viewModel = viewModel)
                }
            }
        }

    }

    private fun initLocation() {
        try {
            if (isLocationPermissionGranted()) {
                viewModel.fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this)
                viewModel.fusedLocationProviderClient.requestLocationUpdates(
                    viewModel.locationRequest,
                    viewModel.locationCallback, Looper.myLooper())

                viewModel.fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    it?.let { location ->
                        viewModel.fetchPlaces(location.latitude, location.longitude)
                    }
                }
            } else {
                viewModel.fetchPlaces()
                Toast.makeText(this, "Permission denied, Default location parameters will be used", Toast.LENGTH_LONG).show()
            }
        } catch (e: SecurityException) {
            viewModel.fetchPlaces()
            e.printStackTrace()
        }
    }


    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                viewModel.requestCode
            )
            false
        } else {
            true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == viewModel.requestCode && isLocationPermissionGranted()){
            initLocation()
        }
    }

}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun BottomSheetAppScaffold(viewModel: PlacesViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val openSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.expand() }
    }

    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = { Text("Places Near you!") })
        },
        sheetPeekHeight = 16.dp,
        scaffoldState = scaffoldState,
        sheetShape = BottomSheetShape,
        sheetContent = {
            DetailLayout(viewModel)
        }
    ) {
        MainScreen(viewModel, openSheet)
    }

}


@ExperimentalMaterialApi
@Composable
fun MainScreen(viewModel: PlacesViewModel, openSheet : () -> Unit) {

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val showLoader = remember { mutableStateOf(false) }
    val places by viewModel.places.observeAsState(initial = Resource.loading())
    val message = remember { mutableStateOf("") }

    when (places.status) {
        Status.ERROR -> {
            showLoader.value = false
            message.value = places.resolveMessage()
            setShowDialog(true)
        }
        Status.LOADING -> {
            showLoader.value = true
        }
        Status.SUCCESS -> {
            showLoader.value = false
            val placeList = places.data?.results ?: emptyList()
            if (placeList.isEmpty()) {
                Text("No Places Found")
            } else {
                LazyColumn {
                    items(placeList) { item ->
                        Place(item = item){ show ->
                            if (show){
                                viewModel.selectedItem = item
                                viewModel.fetchPlaceDetails()
                                openSheet()
                            }
                        }
                    }
                }
            }
        }
    }

    ShowLoader(showLoader = showLoader.value)
    Dialog(message = message.value, showDialog = showDialog, setShowDialog = setShowDialog)
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidassignmentTheme {
        //  MainScreen("Android")
    }
}