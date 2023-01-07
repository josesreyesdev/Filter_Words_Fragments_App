package com.example.filterwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filterwords.adapter.WordAdapter
import com.example.filterwords.databinding.FragmentWordListBinding

class WordListFragment : Fragment() {

    //Patron singleton
    companion object {
        const val LETTER = "letter"
        const val SEARCH_PREFIX = "https://www.google.com/search?q="
    }

    private var _binding: FragmentWordListBinding? = null
    private val binding get() = _binding!!

    private lateinit var letterId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get parameters or values of preview view
        arguments?.let { argumentsNotNull ->
            letterId = argumentsNotNull.getString(LETTER).toString()
        }
        /*arguments?.let { letterId = it.getString(LETTER).toString() }*/
    }

    //Aumentando el diseño
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val letterId = activity?.intent?.extras?.getString(LETTER).toString()

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = WordAdapter( requireContext(), letterId)

        recyclerView.addItemDecoration(
            DividerItemDecoration( context, DividerItemDecoration.VERTICAL)
        )
        //title = getString(R.string.detail_prefix) + " " + letterId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}