package com.adyen.android.assignment.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.adyen.android.assignment.api.model.search.Result
import com.adyen.android.assignment.ui.ui.theme.AndroidassignmentTheme

@ExperimentalMaterialApi
@Composable
fun Place(item: Result, onClick : (Boolean) -> Unit,) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = 6.dp,
        shape = MaterialTheme.shapes.medium,
        onClick = {
            onClick(true) }) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "Address: " +item.location.address
            )
            Text(
                text = "City: "+ item.location.locality
            )
            Text(
                text = "Distance: ${item.distance}m"
            )
            Text(
                text = "Category:"
            )
            LazyRow(modifier = Modifier.fillMaxWidth()
                .padding(4.dp)) {
                items(item.categories) { category ->
                    Column {
                        Text(
                            text = category.name
                        )
                        Image(
                            painter = rememberImagePainter(category.icon.getIconUrl()),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.Gray),
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }

        }

    }

}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun PlacePreview() {
    AndroidassignmentTheme(darkTheme = false) {
        Place(item = Result()){}
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DarkPlacePreview() {
    AndroidassignmentTheme(darkTheme = true) {
        Place(item = Result()){}
    }
}