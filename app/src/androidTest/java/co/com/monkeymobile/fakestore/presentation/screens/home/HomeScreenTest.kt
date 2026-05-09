package co.com.monkeymobile.fakestore.presentation.screens.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import co.com.monkeymobile.fakestore.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeScreen_navigatesFromLoginToHome() {
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()

        composeTestRule.onNodeWithText("Username").performTextInput("derek")
        composeTestRule.onNodeWithText("Password").performTextInput("jklg*_56")

        composeTestRule.onNodeWithTag("login_button").performClick()

        composeTestRule.waitUntil(5000) {
            composeTestRule
                .onAllNodesWithText("Products")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("products_title").assertIsDisplayed()
    }
}
