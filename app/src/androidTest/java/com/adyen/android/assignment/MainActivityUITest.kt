package com.adyen.android.assignment

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import com.adyen.android.assignment.ui.BottomSheetAppScaffold
import com.adyen.android.assignment.ui.PlacesViewModel
import com.adyen.android.assignment.ui.ui.theme.AndroidassignmentTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import org.junit.Rule
import org.junit.Test
/**
 * Was getting this error while trying to run this test, didn't have enough time to debug it
 * Error:
Apps targeting Android 12 and higher are required to specify an explicit value for `android:exported` when the corresponding component has an intent filter defined.
See https://developer.android.com/guide/topics/manifest/activity-element#exported for details.
 * **/
class MainActivityUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val viewModel = PlacesViewModel()

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @Test
    fun testListIsPopulated() {
        composeTestRule.setContent {
            AndroidassignmentTheme() {
               BottomSheetAppScaffold(viewModel = viewModel)
            }
        }

        val tag = composeTestRule.onNode(hasText("Description"), true)
        tag.assertIsDisplayed()
    }

}