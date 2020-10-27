package com.jinjer.simpleplayer.presentation.base

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.controller.service.MusicService
import com.jinjer.simpleplayer.presentation.databinding.ActivityMainBinding
import com.jinjer.simpleplayer.presentation.main.player.PlayerActivity
import com.jinjer.simpleplayer.presentation.navigation.ViewPagerAdapterMain
import com.jinjer.simpleplayer.presentation.utils.MessageUtils
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import com.jinjer.simpleplayer.presentation.utils.extensions.activityViewModel
import com.jinjer.simpleplayer.presentation.utils.notifications.INotifyManager
import org.kodein.di.direct
import org.kodein.di.instance

class MainActivity: PlayerActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override var idFragmentPlayer: Int = R.id.fragment_player

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainActivityViewModel by activityViewModel()

    private val requestCodeReadPermission = 1
    private val simpleName = MainActivity::class.java.simpleName
    private var bottomMargin: Int = 0
    private var readPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initBottomMargin()

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)

        setupNotifications()
        setupViewPager()

        checkPermission()

        if (readPermissionGranted) {
            mainViewModel.onPermissionGranted()
        }

        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (readPermissionGranted) {
            mainViewModel.onAppResumes()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }

        when(requestCode) {
            requestCodeReadPermission -> {
                if (grantResults.isNotEmpty() &&
                    grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    mainViewModel.onPermissionGranted()
                } else {
                    showMessageNoRequiredPermission()
                }
            }
            else -> {  }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val selectedIndex =  when(item.itemId) {
            R.id.tracks -> 0
            R.id.folders -> 1
            R.id.search -> 2

            else -> 3
        }

        binding.viewPager.setCurrentItem(selectedIndex, false)

        return true
    }

    override fun onBackPressed() {
        if (super.backProcessed()) {
            return
        }

        super.onBackPressed()
    }

    override fun getBehaviorView(): View = binding.fragmentPlayer

    override fun getBottomMargin(): Int = bottomMargin

    private fun initBottomMargin() {
        val bottomNavHeight = resources.getDimensionPixelSize(R.dimen.bottom_navigation_height)
        val collapsedPlayerHeight = resources.getDimensionPixelSize(R.dimen.collapsed_player_height)

        bottomMargin = bottomNavHeight + collapsedPlayerHeight
    }

    private fun setupNotifications() {
        direct.instance<INotifyManager>().createChannels()
    }

    private fun setupViewPager() {
        val viewpagerAdapter = ViewPagerAdapterMain(supportFragmentManager)

        with(binding.viewPager) {
            adapter = viewpagerAdapter
            (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = bottomMargin
            offscreenPageLimit = 4
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            readPermissionGranted = true
            return
        }

        val permissionGranted = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            readPermissionGranted = true
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCodeReadPermission)
        }
    }

    private fun showMessageNoRequiredPermission() {
        val msg = getString(R.string.no_required_permission)

        val onClickListener = DialogInterface.OnClickListener { d, _ ->
            d.dismiss()
            finishAndRemoveTask()
        }

        MessageUtils.showAlert(
            this,
            msg = msg,
            positiveBtnId = R.string.leave,
            commonListener = onClickListener,
            isCancelable = false)
    }
}