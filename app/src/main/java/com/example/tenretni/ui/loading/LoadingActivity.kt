package com.example.tenretni.ui.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tenretni.MainActivity
import com.example.tenretni.R
import com.example.tenretni.databinding.ActivityLoadingBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoadingActivity : AppCompatActivity() {

    private val viewModel: LoadingViewModel by viewModels()
    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)






        viewModel.loadingUiState.onEach {
            when(it){
                LoadingUiState.Finished -> {
                    startActivity(MainActivity.newIntent(this))
                    finish()
                }
                is LoadingUiState.Loading -> {
                    binding.txvLoading.text = getString(R.string.loadingFormat,it.progression, 10)
                    binding.pgbLoading.setProgress(it.progression, true)
                }
            }
        }.launchIn(lifecycleScope)
    }
}