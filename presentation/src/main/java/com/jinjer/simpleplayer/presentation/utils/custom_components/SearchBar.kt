package com.jinjer.simpleplayer.presentation.utils.custom_components

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.jinjer.simpleplayer.presentation.R

interface IOnSearchChangeListener {
    fun onSearchQueryChanged(query: String)
}

class SearchBar(
    context: Context,
    attrs: AttributeSet): ConstraintLayout(context, attrs), View.OnFocusChangeListener {

    private lateinit var imgClear: AppCompatImageView
    private lateinit var editSearch: AppCompatEditText
    private lateinit var imgSearch: AppCompatImageView

    private var searchChangedListener: IOnSearchChangeListener? = null
    private var imgClearVisibility = View.INVISIBLE

    private val textWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun afterTextChanged(s: Editable?) { }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            val newText = text ?: ""

            val clearImgVisibility = if (newText.isEmpty()) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }

            imgClear.visibility = clearImgVisibility
            imgClearVisibility = clearImgVisibility

            searchChangedListener?.onSearchQueryChanged(newText.toString())
        }
    }

    init {
        setupRoot()
        findChildren()

        imgClear.setOnClickListener(::onClearButtonClicked)
        imgClear.visibility = View.INVISIBLE
        editSearch.addTextChangedListener(textWatcher)
        editSearch.onFocusChangeListener = this
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        val (colorRes, textColorRes) = if (hasFocus) {
            showKeyboard()
            imgClear.visibility = imgClearVisibility
            Pair(R.color.colorAccent, android.R.color.white)
        } else {
            hideKeyboard()
            imgClear.visibility = View.INVISIBLE
            Pair(R.color.colorInActive, R.color.colorInActive)
        }

        val colorSearch = ContextCompat.getColor(context, colorRes)
        ImageViewCompat.setImageTintList(imgSearch, ColorStateList.valueOf(colorSearch))

        val textColor = ContextCompat.getColor(context, textColorRes)
        editSearch.setTextColor(textColor)
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)

        if (activated) {
            editSearch.requestFocus()
        } else {
            editSearch.clearFocus()
        }
    }

    private fun showKeyboard() {
        val imm = ContextCompat.getSystemService(editSearch.context, InputMethodManager::class.java)
        imm?.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = ContextCompat.getSystemService(editSearch.context, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(editSearch.windowToken, 0)
    }

    private fun setupRoot() {
        inflate(context, R.layout.custom_search_bar, this)
        background = ContextCompat.getDrawable(context, R.drawable.bg_search_bar)
        isFocusableInTouchMode = true
    }

    private fun findChildren() {
        imgClear = findViewById(R.id.img_clear)
        editSearch = findViewById(R.id.edit_search)
        imgSearch = findViewById(R.id.img_search)
    }

    private fun onClearButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        editSearch.setText("")
    }

    fun addSearchChangeListener(listener: IOnSearchChangeListener) {
        searchChangedListener = listener
    }
}