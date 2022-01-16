package com.chibisov.todoapplication.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
//Implement another one SearchView
//import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chibisov.todoapplication.R
import com.chibisov.todoapplication.data.model.ToDoData
import com.chibisov.todoapplication.data.viewmodel.ToDoViewModel
import com.chibisov.todoapplication.fragments.SharedViewModel
import com.chibisov.todoapplication.fragments.list.adapter.ListAdapter
import com.chibisov.todoapplication.fragments.list.adapter.SwipeToDelete
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedToDoViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "Fragment Create")
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list, container, false)
        // Set adapter and manager for RV
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleViewList)
        setupRecycleView(recyclerView)

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            data ->
            mSharedToDoViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })
        mSharedToDoViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer {
            showEmptyDatabaseViews(it)
        })


        val btn = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        //val listLayout = view.findViewById<ConstraintLayout>(R.id.listLayout)

        btn.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

//        listLayout.setOnClickListener {
//            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
//        }


        //Set menu
        setHasOptionsMenu(true)
        return view
    }

    private fun setupRecycleView(recyclerView: RecyclerView?) {
        //adapter RecycleView
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        //Animation in RecycleView
        recyclerView?.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        recyclerView?.let {
            swipeToDelete(it)
        }
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if(emptyDatabase) {
            view?.findViewById<ImageView>(R.id.noDataIView)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.noDataTView)?.visibility = View.VISIBLE

        }   else   {
            view?.findViewById<ImageView>(R.id.noDataIView)?.visibility = View.INVISIBLE
            view?.findViewById<TextView>(R.id.noDataTView)?.visibility = View.INVISIBLE
            }
        }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                //Delete Item
                mToDoViewModel.delete(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                //Toast.makeText(requireContext(), "Successfully delete", Toast.LENGTH_SHORT).show()
                //Restore deleted data
                restoreDeletedData(viewHolder.itemView,itemToDelete,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int){
        val snackBar = Snackbar.make(
            view,
            "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoveAll()
            R.id.menu_priority_high -> mToDoViewModel.sortItems("HIGH").observe(this, Observer {
                data -> adapter.setData(data)
            })
            R.id.menu_priority_low -> mToDoViewModel.sortItems("LOW").observe(this, Observer {
                data -> adapter.setData(data)
            })
        }
        return super.onOptionsItemSelected(item)
    }

    //Show dialog  and delete all data
    private fun confirmRemoveAll(){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Delete all data")
        alertDialog.setMessage("Are u sure to delete all?")
        alertDialog.setPositiveButton("Yes"){
                _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireContext(),"Successfully deleted All", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setNegativeButton("NO"){_, _ -> }
        alertDialog.show()

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }



    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery: String = query
        searchQuery = "%$searchQuery%"

        mToDoViewModel.searchItem(searchQuery).observe(this, Observer {
            data ->
            data?.let {
                adapter.setData(it)
            }
        })
    }

}