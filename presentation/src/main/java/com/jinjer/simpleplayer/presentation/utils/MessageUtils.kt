package com.jinjer.simpleplayer.presentation.utils

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jinjer.simpleplayer.presentation.utils.extensions.getAppName

object MessageUtils {
    fun showAlert(context: Context,
                  title: String = context.getAppName(),
                  msg: String,
                  positiveBtnId: Int = android.R.string.ok,
                  negativeBtnId: Int? = null,
                  neutralBtnId: Int? = null,
                  commonListener: DialogInterface.OnClickListener? = null,
                  isCancelable: Boolean = true) {

        val alertDialog =  MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(isCancelable)

        val clickListener = commonListener ?: DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.setPositiveButton(positiveBtnId, clickListener)
        negativeBtnId?.let { negativeId ->
            alertDialog.setNegativeButton(negativeId, clickListener)
        }
        neutralBtnId?.let { neutralId ->
            alertDialog.setNeutralButton(neutralId, clickListener)
        }

        alertDialog
            .create()
            .show()
    }
}