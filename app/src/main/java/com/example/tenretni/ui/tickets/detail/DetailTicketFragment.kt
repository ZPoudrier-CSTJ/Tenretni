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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.Constants
import com.example.tenretni.core.DateHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.FragmentDetailTicketBinding
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Ticket
import com.example.tenretni.ui.customer.CustomerUiState
import com.example.tenretni.ui.tickets.list.TicketsFragmentDirections
import com.google.android.gms.maps.model.LatLng
import io.github.g00fy2.quickie.ScanQRCode
import io.github.g00fy2.quickie.QRResult
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DetailTicketFragment : Fragment(R.layout.fragment_detail_ticket) {

        private val args: DetailTicketFragmentArgs by navArgs()

    private lateinit var detailTicketRecyclerViewAdapter: DetailTicketRecyclerViewAdapter

    private val scanQRCode = registerForActivityResult(ScanQRCode(), ::handleQuickieResult)

    private lateinit var currentTicket: Ticket

    private val binding: FragmentDetailTicketBinding by viewBinding()
    private val viewModel: DetailTicketViewModel by viewModels{
        DetailTicketViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailTicketRecyclerViewAdapter = DetailTicketRecyclerViewAdapter(listOf(), ::onRecyclerViewDetailTicketClick)

        binding.btnInstall.setOnClickListener{
            scanQRCode.launch(null)
        }

        binding.fabLocation.setOnClickListener{


                val fullName = "${currentTicket.customer.firstName} ${currentTicket.customer.lastName}"
                val position = LatLng(currentTicket.customer.coord.latitude.toDouble(), currentTicket.customer.coord.longitude.toDouble())
                val action = DetailTicketFragmentDirections.actionDetailTicketFragmentToMapsActivity(position, fullName)
                findNavController().navigate(action)

        }

        binding.btnSolve.setOnClickListener{
            viewModel.solveTicket("solve")
        }
        binding.btnOpen.setOnClickListener{
            viewModel.solveTicket("open")
        }

        binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = detailTicketRecyclerViewAdapter
        }
        viewModel.detailTicketUiState.onEach {
            when(it){
                is DetailTicketUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()

                }
                DetailTicketUiState.Loading -> Unit
                is DetailTicketUiState.Success -> {
                    currentTicket = it.ticket
                    fillDetail(it.ticket)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.customerUiState.onEach {
            when(it){
                is CustomerUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    navigateOut()
                }
                CustomerUiState.Loading -> Unit
                is CustomerUiState.Success -> {
                    binding.rcvGateways.visibility = View.VISIBLE
                    detailTicketRecyclerViewAdapter.gateways = it.gateways
                    detailTicketRecyclerViewAdapter.notifyDataSetChanged()


                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        viewModel.loadDetailTicket()
        super.onResume()
    }

    private fun handleQuickieResult(qrResult: QRResult){
        when(qrResult){
            is QRResult.QRSuccess -> {
                viewModel.installGateway(qrResult.content.rawValue)
            }
            QRResult.QRUserCanceled -> Toast.makeText(requireContext(), getString(R.string.canceled_installation), Toast.LENGTH_LONG).show()
            QRResult.QRMissingPermission -> Toast.makeText(requireContext(), getString(R.string.cam_permission_missing), Toast.LENGTH_LONG).show()
            is QRResult.QRError -> qrResult.exception.localizedMessage
        }
    }

    @SuppressLint("SetTextI18n")
    fun fillDetail(ticket: Ticket){

        if(ticket.status == "Open")
        {
            binding.btnOpen.visibility = View.GONE
            binding.grpBtnSolveGateway.visibility = View.VISIBLE
        }else{
            binding.btnOpen.visibility = View.VISIBLE
            binding.grpBtnSolveGateway.visibility = View.INVISIBLE
        }

        binding.txvCustomerName.text = "${ticket.customer.firstName} ${ticket.customer.lastName}"
        binding.txvCustomerAddress.text = ticket.customer.address
        binding.txvCustomerCity.text = ticket.customer.city

        Glide.with(requireContext()).load(Constants.FLAG_API_URL.format(ticket.customer.country.lowercase())).into(binding.imvCustomerCountry)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.ticketNumber, ticket.ticketNumber)

        binding.currentTicket.txvTicketNb.text = requireContext().getString(R.string.ticketNumber, ticket.ticketNumber)
        binding.currentTicket.chipTicketState.chipBackgroundColor = ColorHelper.ticketStatusColor(requireContext(), ticket.status)
        binding.currentTicket.chipTicketPriority.chipBackgroundColor = ColorHelper.ticketPriorityColor(requireContext(), ticket.priority)
        binding.currentTicket.chipTicketState.text = TranslationHelper.ticketStatusString(requireContext(), ticket.status)
        binding.currentTicket.chipTicketPriority.text = TranslationHelper.ticketPriorityString(requireContext(), ticket.priority)
        binding.currentTicket.txvTicketDate.text = DateHelper.formatISODate(ticket.createdDate)


    }

    private fun navigateOut(){
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigateUp()
    }

    private fun onRecyclerViewDetailTicketClick(gateway: Gateway){
        val action = DetailTicketFragmentDirections.actionDetailTicketFragmentToDetailGatewayFragment(gateway.href)
        findNavController().navigate(action)
    }


}