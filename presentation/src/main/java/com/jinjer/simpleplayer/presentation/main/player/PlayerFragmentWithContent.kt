package com.jinjer.simpleplayer.presentation.main.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.databinding.FragmentPlayerContentBinding

abstract class PlayerFragmentWithContent: PlayerFragmentBase() {
    private lateinit var binding: FragmentPlayerContentBinding
    private val tagContent = "tag_content"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_player_content, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addContent()
    }

    override fun getBehaviorView(): View = binding.fragmentPlayer

    override fun getBottomMargin(): Int =
        resources.getDimensionPixelSize(R.dimen.collapsed_player_height)

    override var idFragmentPlayer: Int = R.id.fragment_player

    fun showNewContent(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        addFragment(fragment, tag, addToBackStack)
    }

    private fun addContent() {
        childFragmentManager.findFragmentByTag(tagContent) ?: run {
            val fragment = getContentFragment()
            addFragment(fragment, tagContent, false)
        }
    }

    private fun addFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        val transaction = childFragmentManager
            .beginTransaction()
            .add(R.id.fragment_content, fragment, tag)

        if (addToBackStack) {
            transaction
                .addToBackStack(null)
                .commit()
        } else {
            transaction
                .commit()
        }
    }

    abstract fun getContentFragment(): Fragment
}