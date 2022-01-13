package com.chibisov.todoapplication.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibisov.todoapplication.R
import com.chibisov.todoapplication.data.viewmodel.ToDoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment : Fragment() {

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        // Set adapter and manager for RV
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            data -> adapter.setData(data)
        })


        val btn = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val listLayout = view.findViewById<ConstraintLayout>(R.id.listLayout)

        btn.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }


        //Set menu
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

}