package com.example.filterwords

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) //Ejecutor de pruebas
class NavigationTestsActivity {

    @get:Rule() // Iniciando la actividad de pruebas
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test //Elegiremos un elemento segun su posicion en el adaptador
    fun navigate_to_word() {
        /* onView(withText("C"))
            .perform(click()) */

        onView(withId(R.id.recycler_view))
            .perform( RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(9, click()))

        onView(withText("Words That Start With J"))
            .check(matches(isDisplayed()))
    }
}