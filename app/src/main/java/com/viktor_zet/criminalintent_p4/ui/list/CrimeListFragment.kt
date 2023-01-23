package com.viktor_zet.criminalintent_p4.ui.list

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.viktor_zet.criminalintent_p4.R
import com.viktor_zet.criminalintent_p4.databinding.FragmentCrimeListBinding
import com.viktor_zet.criminalintent_p4.entity.Crime
import kotlinx.coroutines.launch
import java.util.*

class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect { crimes ->
                    // Shows a message if the list is empty
                    if (crimes.isEmpty()) binding.msgText.visibility = View.VISIBLE
                    else binding.msgText.visibility = View.GONE
                    binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes, { crimeId ->
                        //On click it will navigate to detail fragment
                        findNavController().navigate(
                            CrimeListFragmentDirections.showCrimeDetail(
                                crimeId
                            )
                        )
                    }, { pos ->
                        //Deleting item by Alert Dialog
                        val alertDialog =
                            AlertDialog.Builder(this@CrimeListFragment.requireContext())
                                .setTitle("Deleteting")
                                .setMessage("Delete?")
                                .setNegativeButton("Cancel") { _, _ -> }
                                .setPositiveButton("Ok") { _, _ ->
                                    lifecycleScope.launch {
                                        crimeListViewModel.deleteCrime(
                                            crimes[pos]
                                        )
                                    }
                                }
                        alertDialog.create().show()
                        true
                    })
                }
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                showNewCrime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNewCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCrime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false
            )
            crimeListViewModel.addCrime(newCrime)
            findNavController().navigate(CrimeListFragmentDirections.showCrimeDetail(newCrime.id))
        }
    }
}