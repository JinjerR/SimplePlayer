package com.jinjer.simpleplayer.presentation.controller.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.support.v4.media.session.MediaSessionCompat
import androidx.appcompat.app.AppCompatActivity
import com.jinjer.simpleplayer.presentation.controller.service.MusicService
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.tagMusicControl
import com.jinjer.simpleplayer.presentation.controller.service.MusicService.Companion.actionSessionToken
import com.jinjer.simpleplayer.presentation.utils.ShowLog
import java.lang.ref.WeakReference

/** communicates with the service
// (connecting, disconnecting, sending messages to the service) **/

class MediaClientManager(
    private val appContext: Context,
    private val clientCallback: IClientCallback
): IClientManager {

    private val simpleName = MediaClientManager::class.java.simpleName
    private val mMessenger = Messenger(ClientHandler(this))
    private var serviceMessenger: Messenger? = null
    private val serviceIntent = Intent(appContext, MusicService::class.java)
    private var tokenReceived = false
    private var boundToService = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            ShowLog.i("$simpleName.onServiceConnected()", tagMusicControl)

            serviceMessenger ?: run {
                serviceMessenger = Messenger(service)
            }
            if (tokenReceived.not()) {
                sendMessage(actionSessionToken)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            ShowLog.i("$simpleName.onServiceDisconnected()", tagMusicControl)
        }
    }

    override fun bind() {
        if (boundToService.not()) {
            appContext.bindService(serviceIntent, serviceConnection, AppCompatActivity.BIND_AUTO_CREATE)
            boundToService = true
        }
    }

    override fun unbind() {
        if (boundToService) {
            appContext.unbindService(serviceConnection)
            boundToService = false
        }
    }

    override fun startService() {
        appContext.startService(serviceIntent)
    }

    override fun stopService() {
        appContext.stopService(serviceIntent)
    }

    override fun sendMessageToService(action: Int) {
        sendMessage(action)
    }

    private fun sendMessage(action: Int) {
        val msg = Message.obtain(null, action)
        msg.replyTo = mMessenger
        serviceMessenger?.send(msg)
    }

    class ClientHandler(musicClientManager: MediaClientManager): Handler() {
        private val musicClientManagerWeak = WeakReference(musicClientManager)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            musicClientManagerWeak.get()?.apply {
                ShowLog.i("$simpleName.handleMessage(), what = ${ msg.what }", tagMusicControl)

                when(msg.what) {
                    actionSessionToken -> {
                        val sessionToken = msg.obj as MediaSessionCompat.Token
                        tokenReceived = true
                        clientCallback.onTokenReceived(sessionToken)
                    }
                }
            }
        }
    }
}