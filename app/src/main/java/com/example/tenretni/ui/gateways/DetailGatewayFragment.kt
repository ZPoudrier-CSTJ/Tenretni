package com.example.tenretni.ui.gateways

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.FragmentDetailGatewayBinding
import com.example.tenretni.databinding.FragmentDetailTicketBinding
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.ui.tickets.detail.DetailTicketFragmentArgs
import com.example.tenretni.ui.tickets.detail.DetailTicketViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailGatewayFragment : Fragment(R.layout.fragment_detail_gateway) {

    private val args: DetailGatewayFragmentArgs by navArgs()
    private lateinit var currentGateway: Gateway
    private val binding: FragmentDetailGatewayBinding by viewBinding()
    private val viewModel: DetailGatewayViewModel by viewModels{
        DetailGatewayViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.detailGatewayUiState.onEach {
            when(it){
                is DetailGatewayUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailGatewayUiState.Loading -> Unit
                is DetailGatewayUiState.Success -> {
                    currentGateway = it.gateway
                    bindContent(it.gateway)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        viewModel.loadDetailGateway()
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    fun bindContent(gateway: Gateway){
        binding.chipStatus.chipBackgroundColor = ColorHelper.connectionStatusColor(requireContext(), gateway.connection.status)
        binding.chipStatus.text = TranslationHelper.connectionStatusString(requireContext(), gateway.connection.status)
        binding.txvUuid.text = gateway.serialNumber
        binding.txvMac.text = gateway.config.mac
        binding.txvSsid.text = "SSID: ${gateway.config.SSID}"
        binding.txvPin.text = "PIN: ${gateway.pin}"
        binding.txvIp.text = gateway.connection.ip
        binding.txvDetailPing.text = requireContext().getString(R.string.txtPing, gateway.connection.ping)
        binding.txvDetailDownload.text = requireContext().getString(R.string.txtSpeed, gateway.connection.download)
        binding.txvDetailUpload.text = requireContext().getString(R.string.txtSpeed, gateway.connection.upload)
        binding.txvSignal.text = requireContext().getString(R.string.txtSignal, gateway.connection.signal)
        binding.txvHash.text = gateway.hash
        binding.txvKernel.text = "Kernel revision ${gateway.config.kernelRevision}"
        binding.txvVersion.text = "Version ${gateway.config.version}"
        //TODO: Check image for kernel
        //Glide.with(requireContext()).load(R.drawable."element_${gateway.config.kernel[0]}").into(binding.imvElement1)

    }




}