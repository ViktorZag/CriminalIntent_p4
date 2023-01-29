package com.viktor_zet.criminalintent_p4.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.viktor_zet.criminalintent_p4.utils.rotateBitmap
import java.io.File


class ImageDialogFragment : DialogFragment() {

    private val args: ImageDialogFragmentArgs by navArgs()

    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val imageView: ImageView = ImageView(requireContext())
        imageView.layoutParams = LinearLayout.LayoutParams(160, 20)
        val photofileName = File(requireContext().applicationContext.filesDir, args.photoPath)
        val bitmap = rotateBitmap(BitmapFactory.decodeFile(photofileName.path), 90.0f)

        imageView.setImageBitmap(bitmap)

        return AlertDialog.Builder(
            requireContext()
        ).setView(imageView)
            .create()

    }

}