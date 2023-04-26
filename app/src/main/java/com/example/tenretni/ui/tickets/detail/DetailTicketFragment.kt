package com.example.tenretni.ui.tickets.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.Constants
import com.example.tenretni.core.DateHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.FragmentDetailTicketBinding
import com.example.tenretni.domain.models.Ticket
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DetailTicketFragment : Fragment(R.layout.fragment_detail_ticket) {

        private val args: DetailTicketFragmentArgs by navArgs()

    private val binding: FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels{
        DetailTicketViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailTicketUiState.Loading -> Unit
                is DetailTicketUiState.Success -> {
                    fillDetail(it.ticket)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    fun fillDetail(ticket: Ticket){

        if(ticket.status == "Open")
        {
            binding.btnOpen.visibility = View.GONE
            binding.grpBtnSolveGateway.visibility = View.VISIBLE
        }else{
            binding.btnOpen.visibility = View.VISIBLE
            binding.grpBtnSolveGateway.visibility = View.GONE
        }

        binding.txvCustomerName.text = "${ticket.customer.firstName} ${ticket.customer.lastName}"
        binding.txvCustomerAddress.text = ticket.customer.address
        binding.txvCustomerCity.text = ticket.customer.city

        Glide.with(requireContext()).load(Constants.FLAG_API_URL.format(ticket.customer.country.toLowerCase())).into(binding.imvCustomerCountry)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.ticketNumber, ticket.ticketNumber)

        binding.currentTicket.txvTicketNb.text = requireContext().getString(R.string.ticketNumber, ticket.ticketNumber)
        binding.currentTicket.chipTicketState.chipBackgroundColor = ColorHelper.ticketStatusColor(requireContext(), ticket.status)
        binding.currentTicket.chipTicketPriority.chipBackgroundColor = ColorHelper.ticketPriorityColor(requireContext(), ticket.priority)
        binding.currentTicket.chipTicketState.text = TranslationHelper.ticketStatusString(requireContext(), ticket.status)
        binding.currentTicket.chipTicketPriority.text = TranslationHelper.ticketPriorityString(requireContext(), ticket.priority)
        binding.currentTicket.txvTicketDate.text = DateHelper.formatISODate(ticket.createdDate)


    }
}