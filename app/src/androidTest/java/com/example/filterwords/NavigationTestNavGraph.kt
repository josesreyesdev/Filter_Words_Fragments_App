package com.example.filterwords

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTestNavGraph {

    private lateinit var navController: TestNavHostController

    private lateinit var exampleFragmentScenario: FragmentScenario<LetterListFragment>

    @Test
    fun navigate_to_words_nav_component() {

        //Instancia de prueba del controlador de navegacion
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        //Dependencia especifica del fragmento a utilizar y el theme que debe usar
        val letterListScenario =
            launchFragmentInContainer<LetterListFragment>( themeResId = R.style.Theme_FilterWords)

        /** Declaro de forma explicita el grafico de navegacion que queremos q utilice
        el controlador de navegacion para el fragmento lanzado */
        letterListScenario.onFragment{ fragment ->
            navController.setGraph(R.navigation.nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        //Activando evento que solicita la navegacion
        onView(withId(R.id.recycler_view))
            .perform( RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>( 2, click()))

        //Verificando el destino del controlador de navegacion en este caso wordListFragment
        assertEquals(navController.currentDestination?.id, R.id.wordListFragment)
    }


    fun example() {
        /* Ejemplo anterior de lo que se haria si tuvieramos 10 buttons y estos conducirian
         a un fragment diferente cada una y esto no seria correcto, para esto las pruebas de unidades
         y de instrumentacion tienen un funcion que permite establecer la misma configuracion
         para cada prueba de una clase sin código repetitivo.... VER FUNCION setup() y declarar las
         variables al inicio de la clase:

        lateinit var navController: TestNavHostController
        lateinit var exampleFragmentScenario: FragmentScenario<ExampleFragment>*/

        /*
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        val exampleFragmentScenario = launchFragmentInContainer<ExampleFragment>(themeResId =
        R.style.Theme_Example)

        exampleFragmentScenario.onFragment { fragment ->

            navController.setGraph(R.navigation.example_nav_graph)

            Navigation.setViewNavController(fragment.requireView(), navController)
        } */
    }


    @Before
    fun setup() {

        /*Este metodo se ejecuta para cada prueba que escribamos en esta clase y podemos acceder a las variables
   * necesarias desde cualquiera de las pruebas. De manera similar si hay codigo a ejecutar despues de cada
   * prueba usamos la anotacion @after. Ej. Borrar un recurso que usamos para una prueba, y en las pruebas de
   * de instrumentacion podemos usarlo para que el dispositivo cambie a un estado especifico. */

        navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        exampleFragmentScenario = launchFragmentInContainer(themeResId = R.style.Theme_FilterWords)
        exampleFragmentScenario.onFragment{ fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }
}

/* Tambien existe las anotaciones @BeforeClass y @AfterClass => estas solo se ejecutan una vez
    * pero el código ejecutado se aplica a todos sus metodos.
    *
    * Si nuestros metodos de configuracion o desplazaiento contienen operaciones costosas es preferible
    * usar los metodos:
    * @BeforeClass, @AfterClass => estas se colocan con un objeto complementario y anotarse con:
    * @jvmStatic.
    *
    * Ej. para demostrar el orden de ejecucion de las anotaciones previas: */
@RunWith(AndroidJUnit4::class)
class OrderOfTestAnnotations{

    @Before
    fun setupFunction() {
        println("Set up function")
    }

    @Test
    fun test_a() {
        println("Test a")
    }

    @Test
    fun test_b() {
        println("Test b")
    }

    @Test
    fun test_c() {
        println("Test c")
    }

    @After
    fun tearDownFunction() {
        println("Tear Down function")
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            println("Set up class")
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            println("Tear down class")
        }
    }

    /*Resultado de ejecucion:
    * 1.- Set up class
    * 2.- Set up function
    * 3.- Test a
    * 4.- Tear down function
    * 5.- Set up function
    * 6.- Test b
    * 7.- Tear down function
    * 8.- Set up function
    * 9.- Test c
    * 10.- Tear down function
    * 11.- Tear down class
    *  */
}