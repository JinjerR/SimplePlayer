package com.jinjer.simpleplayer.presentation.main.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.databinding.FragmentCollapsedPlayerBinding
import com.jinjer.simpleplayer.presentation.utils.extensions.fragmentViewModel

class CollapsedPlayerFragment: BaseFragment() {
    private lateinit var binding: FragmentCollapsedPlayerBinding
    private val viewModel: CollapsedPlayerViewModel by fragmentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_collapsed_player, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}