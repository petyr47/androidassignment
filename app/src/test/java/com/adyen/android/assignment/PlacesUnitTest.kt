package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlaceDetailsQueryBuilder
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.PlacesSearchQueryBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class PlacesUnitTest {

    private val testFSQId = "4de6588a18389f0558772bbf"

    @Test
    fun testResponseNotNull() {
        runBlocking {
            val query = PlacesSearchQueryBuilder()
                .setLatitudeLongitude(52.376510, 4.905890)
                .setProperties("food")
                .build()
            val response = PlacesService.instance.getPlaces(query)
            assertNotNull(response)
        }
    }

    @Test
    fun testResponseHasResults() {
        runBlocking {
            val query = PlacesSearchQueryBuilder()
                .setLatitudeLongitude(52.376510, 4.905890)
                .setProperties("food")
                .build()
            val response = PlacesService.instance
                .getPlaces(query)
            assertTrue(response.results.isNotEmpty())
        }
    }

    @Test
    fun testFetchPlaceDetailsNotNull() {
        runBlocking {
            val detailsQuery = PlaceDetailsQueryBuilder().apply {
                fourSquareId = testFSQId
                setFields()
            }.build()
            val response = PlacesService.instance.getPlaceDetails(testFSQId, detailsQuery)
            assertNotNull(response)
        }
    }

    @Test
    fun testFetchPlaceDetailsIsForCorrectPlace() {
        runBlocking {
            val detailsQuery = PlaceDetailsQueryBuilder().apply {
                fourSquareId = testFSQId
                setFields()
            }.build()
            val response = PlacesService.instance.getPlaceDetails(testFSQId, detailsQuery)
            assertEquals(response.fsq_id, testFSQId)
        }
    }

}
