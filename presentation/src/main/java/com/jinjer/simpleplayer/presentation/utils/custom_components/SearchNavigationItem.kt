package com.jinjer.simpleplayer.presentation.utils.custom_components

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.utils.extensions.capitalizeWord

class SearchNavigationItem(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    @StyleableRes
    var index0 = 0
    @StyleableRes
    var index1 = 1

    private lateinit var icon: AppCompatImageView
    private lateinit var txtCount: AppCompatTextView
    private lateinit var title: AppCompatTextView
    private lateinit var constraintSquare: ConstraintLayout

    init {
        setupRoot()
        findChildren()

        val intArray = arrayOf(R.attr.searchNavIconResId, R.attr.searchNavTitle)
        val typedArray = context.obtainStyledAttributes(attrs, intArray.toIntArray())

        val iconResId = typedArray.getResourceId(index0, R.drawable.icon_albums)
        icon.setImageResource(iconResId)

        val title = typedArray.getResourceId(index1, R.string.albums)
        val titleStr = resources.getString(title)
        this.title.text = titleStr.capitalizeWord()

        txtCount.text = "0"

        typedArray.recycle()
    }

    override fun setActivated(activated: Boolean) {
        super.setActivated(activated)

        val colorRes = if (activated) R.color.colorAccent else R.color.colorOnPrimaryVer2
        val color = ContextCompat.getColor(context, colorRes)

        ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(color))
        txtCount.setTextColor(color)
        constraintSquare.isActivated = activated
    }

    fun setCount(count: Int) {
        txtCount.text = count.toString()
    }

    private fun setupRoot() {
        inflate(context, R.layout.custom_search_navigation_item, this)
        orientation = VERTICAL
    }

    private fun findChildren() {
        icon = findViewById(R.id.icon)
        txtCount = findViewById(R.id.count)
        title = findViewById(R.id.title)
        constraintSquare = findViewById(R.id.square)
    }
}