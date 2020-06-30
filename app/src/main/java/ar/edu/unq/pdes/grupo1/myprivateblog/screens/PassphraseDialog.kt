package ar.edu.unq.pdes.grupo1.myprivateblog.screens

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.R

class PassphraseDialog(val passphrase: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)

            builder.setMessage(getString(R.string.passphrase,passphrase))
                .setPositiveButton("OK", DialogInterface.OnClickListener{ dialog, id ->
                    dialog.cancel()
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}