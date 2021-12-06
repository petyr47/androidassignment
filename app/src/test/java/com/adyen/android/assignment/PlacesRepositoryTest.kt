package com.adyen.android.assignment

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class PlacesRepositoryTest  {

    private val testFSQId = "4de6588a18389f0558772bbf"

    @Test
    fun testFetchPlaces() {
        val repository = PlacesRepository()
        runBlocking {
            val responseWrapper = repository.fetchPlaces()
            Assert.assertNotNull(responseWrapper)
        }
    }

    @Test
    fun testGetDetailsForId() {
        val repository = PlacesRepository()
        runBlocking {
            val responseWrapper = repository.getDetailsForId(testFSQId)
            Assert.assertNotNull(responseWrapper)
        }
    }

    @Test
    fun testGetDetailsForIdWhenIdEmpty() {
        val repository = PlacesRepository()
        runBlocking {
            val responseWrapper = repository.getDetailsForId("")
            Assert.assertNotNull(responseWrapper)
            Assert.assertFalse(responseWrapper.success)
            Assert.assertNull(responseWrapper.response)
            Assert.assertTrue(responseWrapper.message.isNotBlank())
        }
    }

}