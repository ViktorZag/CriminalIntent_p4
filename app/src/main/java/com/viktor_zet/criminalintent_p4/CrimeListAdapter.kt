package com.viktor_zet.criminalintent_p4

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.viktor_zet.criminalintent_p4.databinding.ListItemCrimeBinding

class CrimeListAdapter(private val crimes: List<Crime>) :
    RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }
    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }
    override fun getItemCount(): Int = crimes.size

    class CrimeHolder(
        private val binding: ListItemCrimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crime: Crime) {
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = 
               DateFormat.format("dd/MM/yyyy", crime.date) //crime.date.toString()
            binding.crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
            binding.root.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    "${crime.title} clicked!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}