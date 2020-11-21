package com.jinjer.simpleplayer.presentation.controller.main

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.jinjer.simpleplayer.presentation.R
import com.jinjer.simpleplayer.presentation.base.BaseActivity
import com.jinjer.simpleplayer.presentation.base.MainFragment
import com.jinjer.simpleplayer.presentation.databinding.ActivityMainBinding
import com.jinjer.simpleplayer.presentation.main.albums.AlbumDetailsFragment
import com.jinjer.simpleplayer.presentation.main.singers.SingerDetailsFragment
import com.jinjer.simpleplayer.presentation.models.singer.SingerDetails
import com.jinjer.simpleplayer.presentation.utils.MessageUtils
import com.jinjer.simpleplayer.presentation.utils.extensions.activityViewModel
import com.jinjer.simpleplayer.presentation.utils.notifications.INotifyManager
import org.kodein.di.direct
import org.kodein.di.instance

// TODO: very long vector path (lint check)
// TODO: versions gradle file

class MainActivity: BaseActivity(), IMainController {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by activityViewModel()

    private val requestCodeReadPermission = 1
    @Suppress("unused")
    private val simpleName = MainActivity::class.java.simpleName
    private var readPermissionGranted = false

    private val tagMainFragment = "tag_main_fragment"
    private val tagAlbumDetails = "tag_album_details"
    private val tagSingerDetails = "tag_singer_details"

    private val mainFragment: MainFragment?
    get() = supportFragmentManager
        .findFragmentByTag(tagMainFragment) as? MainFragment

    private val albumDetailsFragment: AlbumDetailsFragment?
    get() = supportFragmentManager
        .findFragmentByTag(tagAlbumDetails) as? AlbumDetailsFragment

    private val singerDetailsFragment: SingerDetailsFragment?
    get() = supportFragmentManager
        .findFragmentByTag(tagSingerDetails) as? SingerDetailsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupNotifications()
        checkPermission()

        if (readPermissionGranted) {
            mainViewModel.onPermissionGranted()
        }

        addMainFragment()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.onAppResumes()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.onAppStopped()
    }

    override fun onBackPressed() {
        albumDetailsFragment?.let { fragment ->
            if (fragment.backProcessed()) {
                return
            }

            supportFragmentManager.popBackStack()
            return
        }

        singerDetailsFragment?.let { fragment ->
            if (fragment.backProcessed()) {
                return
            }

            supportFragmentManager.popBackStack()
            return
        }

        if (mainFragment?.backProcessed() == true) {
            return
        }

        super.onBackPressed()
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

    override fun showAlbumDetails(albumId: Int) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_content, AlbumDetailsFragment.newInstance(albumId), tagAlbumDetails)
            .addToBackStack(null)
            .commit()
    }

    override fun showSingerDetails(singerId: Int) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_content, SingerDetailsFragment.newInstance(singerId), tagSingerDetails)
            .addToBackStack(null)
            .commit()
    }

    private fun addMainFragment() {
        mainFragment ?: run {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_content, MainFragment(), tagMainFragment)
                .commit()
        }
    }

    private fun setupNotifications() {
        direct.instance<INotifyManager>().createChannels()
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