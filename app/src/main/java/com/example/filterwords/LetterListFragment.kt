package com.example.filterwords

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filterwords.adapter.LetterAdapter
import com.example.filterwords.data.SettingsDataStore
import com.example.filterwords.databinding.FragmentLetterListBinding
import kotlinx.coroutines.launch

class LetterListFragment : Fragment() {
    private var _binding: FragmentLetterListBinding? = null /*Establecemos a null ya que no podemos
     aumentar el diseño hasta que se llame a onCreateView()  */
    private val binding get() = _binding!! /*Add dos signos de admiracion cuando sabemos que al acceder
     a _binding no sera nulo. get() => se utiliza para solo obtener, quiere decir que puedo ontener un valor
     pero, una vez asignado no puedo asignarlo a otro elemento */

    private lateinit var recyclerView: RecyclerView
    private var isLinearLayoutManager = true

    //Setting DataStore
    private lateinit var SettingsDataStore: SettingsDataStore

    //Configurando las opciones del menu u obtener los argumentos pasados en fragments anteriores
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //Aumenta el diseño
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //vinculando vistas especificas ej: el recyclerView, o llamando a findViewById() de todas las vistas
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        chooseLayout()

        //Initialize SettingDataStore
        SettingsDataStore = SettingsDataStore( requireContext())
        SettingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            isLinearLayoutManager = value
            chooseLayout()

            //Redraw the menu
            activity?.invalidateOptionsMenu()
        }
    }

    //Reestableciendo la propiedad _binding a null
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //Aumentando el menu de opciones
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)
    }


    private fun chooseLayout() { //escogerLayout
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        }
        recyclerView.adapter = LetterAdapter()

        // Lo mismo pero mas varato
        /*when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = LetterAdapter()
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = LetterAdapter()
            }
        }*/
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return
        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable( this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable( this.requireContext(), R.drawable.ic_linear_layout)
    }

    //Llama a este metodo cuando se seleccione el botón de los iconos
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)

                //Launch a coroutine and write the layout setting in the preference DataStore
                lifecycleScope.launch {
                    SettingsDataStore.saveLayoutToPrefencesStore(isLinearLayoutManager, requireContext())
                }

                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}