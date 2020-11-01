package com.jinjer.simpleplayer.presentation.main.singers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseFragment
import com.jinjer.simpleplayer.presentation.databinding.FragmentSingerDetailsBinding

class SingerDetailsContentFragment: BaseFragment() {
    private lateinit var binding: FragmentSingerDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_singer_details, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    companion object {
        fun newInstance(singerId: Int): SingerDetailsContentFragment = SingerDetailsContentFragment().apply {
            arguments = Bundle().apply {
                putInt(SingerDetailsFragment.keySingerId, singerId)
            }
        }
    }
}