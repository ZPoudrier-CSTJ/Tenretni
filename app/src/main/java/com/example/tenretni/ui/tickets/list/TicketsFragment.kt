package com.example.tenretni.ui.tickets.list

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tenretni.R
import com.example.tenretni.databinding.FragmentTicketsBinding
import com.example.tenretni.domain.models.Ticket
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TicketsFragment : Fragment(R.layout.fragment_tickets) {

    private val binding: FragmentTicketsBinding by viewBinding()
    private val viewModel: TicketsViewModel by viewModels()

    private lateinit var ticketRecyclerViewAdapter: TicketRecyclerViewAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketRecyclerViewAdapter = TicketRecyclerViewAdapter(listOf(), ::onRecyclerViewTicketsClick)

        binding.rcvTickets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ticketRecyclerViewAdapter
        }


        viewModel.ticketUiState.onEach {
            when(it) {
                is TicketsUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage ?: getString(R.string.apiErrorMessage), Toast.LENGTH_LONG).show()
                }
                is TicketsUiState.Success -> {
                    binding.rcvTickets.visibility = View.VISIBLE

                    ticketRecyclerViewAdapter.tickets = it.tickets
                    ticketRecyclerViewAdapter.notifyDataSetChanged()
                    binding.pgbLoading.hide()

                }
                TicketsUiState.Loading ->  {
                    binding.rcvTickets.visibility = View.GONE
                    binding.pgbLoading.show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

    override fun onResume() {
        viewModel.refreshTickets()
        super.onResume()
    }

    private fun onRecyclerViewTicketsClick(ticket: Ticket) {
        val action = TicketsFragmentDirections.actionNavigationTicketsToDetailTicketFragment(ticket.href)
        findNavController().navigate(action)
    }

}