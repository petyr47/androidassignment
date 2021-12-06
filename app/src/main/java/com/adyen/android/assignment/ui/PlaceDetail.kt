package com.adyen.android.assignment.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.adyen.android.assignment.api.model.Resource
import com.adyen.android.assignment.api.model.Status
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer


@ExperimentalPagerApi
@Composable
fun DetailLayout(viewModel: PlacesViewModel) {

    val (showDialogDetail, setShowDialogDetails) = remember { mutableStateOf(false) }
    val showDetailLoader = remember { mutableStateOf(false) }
    val placeDetails by viewModel.placeDetails.observeAsState(initial = Resource.loading())
    val messageDetails = remember { mutableStateOf("") }

    val context = LocalUriHandler.current

    Box(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .placeholder(
                visible = showDetailLoader.value,
                highlight = PlaceholderHighlight.shimmer(Color.Red),
                color = Color.Red
            )
    ) {
        if (showDetailLoader.value){
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        when (placeDetails.status) {
            Status.SUCCESS -> {
                showDetailLoader.value = false
                val details = placeDetails.data!!
                Column() {
                    Text(
                        text = details.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textAlign = TextAlign.Center)

                    Text(text = "Images",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp))
                    HorizontalPager(count = details.photos.size,
                        contentPadding = PaddingValues(end = 64.dp)) { page ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.3f)
                                .padding(4.dp),
                            elevation = 2.dp,
                            shape = MaterialTheme.shapes.medium,
                        ){
                            Image(
                                painter = rememberImagePainter(details.photos[page].getPhotoUrl()),
                                contentDescription = null,
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    if (details.description.isNotBlank()){
                        Text(
                            text = "Description:",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = details.description,
                            fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Text(
                        text = "Cost:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    )
                    Text(text = "\uD83D\uDCB0".repeat(details.price))
                    if (details.website.isNotBlank()){
                        Button(onClick = {
                            context.openUri(details.website)
                        })
                        {
                            Text(text = "Visit Website ➦")
                        }
                    }

                    if (details.tel.isNotBlank()){
                        Text(text = "Telephone: ${details.tel}")
                    }
                    Text(text = "Rating: ${"⭐".repeat(details.rating.toInt())}")
                }
            }
            Status.LOADING -> {
                showDetailLoader.value = true
            }
            Status.ERROR -> {
                showDetailLoader.value = false
                messageDetails.value = placeDetails.resolveMessage()
                setShowDialogDetails(true)
            }

        }



        //ShowLoader(showLoader = showDetailLoader.value)
        Dialog(message = messageDetails.value, showDialog = showDialogDetail, setShowDialog = setShowDialogDetails)
    }


}