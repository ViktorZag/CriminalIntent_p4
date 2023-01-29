package com.viktor_zet.criminalintent_p4.ui.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.viktor_zet.criminalintent_p4.R
import com.viktor_zet.criminalintent_p4.databinding.FragmentCrimeDetailBinding
import com.viktor_zet.criminalintent_p4.entity.Crime
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.math.roundToInt
import com.viktor_zet.criminalintent_p4.utils.rotateBitmap

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val TASK = "TASK"

/**
 * A simple [Fragment] subclass.
 * Use the [CrimeDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeDetailFragment : Fragment() {

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "Cannot access binding because it is null. Is the view visible?" }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeid)
    }

    private val selectSuspect =
        registerForActivityResult(ActivityResultContracts.PickContact())
        { uri: Uri? ->
            uri?.let { parseContactSelection(it) }
        }
    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture())
    { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            crimeDetailViewModel.updateCrime { oldCrime ->
                oldCrime.copy(photoFileName = photoName)
            }
        }
    }
    private var photoName: String? = null

    private lateinit var callback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("Fragment", "Back pressed")
                if (binding.crimeTitle.text.isBlank()) {
                    Toast.makeText(context, "Title can't be empty!", Toast.LENGTH_SHORT).show()
                    Log.d("Fragment", "Toast showed")
                } else {
                    isEnabled = false
                    Log.d("Fragment", "BackStack popped")
                    findNavController().popBackStack()
                }
            }
        }
        Log.d("Fragment", "onViewCreated")
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCrimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())

                }
            }
            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }
            crimeSuspect.setOnClickListener {
                selectSuspect.launch(null)
            }


            crimeCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(
                    requireContext().applicationContext.filesDir,
                    photoName!!
                )
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.viktor_zet.criminalintent_p4.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }
            val selectSuspectIntent = selectSuspect.contract.createIntent(requireContext(), null)
            crimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)

            val captureImageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         //   val captureImageIntent = takePhoto.contract.createIntent(requireContext(), null)
            crimeCamera.isEnabled = canResolveIntent(captureImageIntent)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
            crimeDetailViewModel.updateCrime {
                it.copy(
                    date = bundle.getSerializable(
                        DatePickerFragment.BUNDLE_KEY_DATE
                    ) as Date
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = DateFormat.format("EEE, d MMM yyyy HH:mm", crime.date)
            crimeDate.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(crime.date)
                )
            }
            changeTimeButton.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectTime(crime.date)
                )
            }
            crimePhoto.setOnClickListener {
                if (crime.photoFileName!=null)
                findNavController().navigate(CrimeDetailFragmentDirections.showImage(crime.photoFileName))
            }
            crimeSolved.isChecked = crime.isSolved
            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
                }
                val chooserIntent =
                    Intent.createChooser(reportIntent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }
            updatePhoto(crime.photoFileName)
        }
    }

    private fun getCrimeReport(crime: Crime): String {

        val solvedString =
            if (crime.isSolved)
                getString(R.string.crime_report_solved)
            else
                getString(R.string.crime_report_unsolved)
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspectText =
            if (crime.suspect.isBlank())
                getString(R.string.crime_report_no_suspect)
            else
                getString(R.string.crime_report_suspect, crime.suspect)

        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText
        )

    }

    private fun parseContactSelection(contacrUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val queryCursor = requireActivity().contentResolver
            .query(contacrUri, queryFields, null, null, null)

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = suspect)
                }
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolvedActivity != null
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
// Read in the dimensions of the image on disk
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()
// Figure out how much to scale down by
        val sampleSize = if (srcHeight <= destHeight && srcWidth <= destWidth) {
            1
        } else {
            val heightScale = srcHeight / destHeight
            val widthScale = srcWidth / destWidth
            minOf(heightScale, widthScale).roundToInt()
        }
// Read in and create final bitmap
        return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
            inSampleSize = sampleSize
        })
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.crimePhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                binding.crimePhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.crimePhoto.setImageBitmap(rotateBitmap(scaledBitmap, 90.0f))
                    binding.crimePhoto.tag = photoFileName
                }
            } else {
                binding.crimePhoto.setImageBitmap(null)
                binding.crimePhoto.tag = null
            }
        }
    }


}