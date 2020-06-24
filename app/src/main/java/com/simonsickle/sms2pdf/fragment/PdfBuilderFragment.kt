package com.simonsickle.sms2pdf.fragment

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.print.PdfConverter
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.simonsickle.sms2pdf.R
import com.simonsickle.sms2pdf.databinding.PdfBuilderFragmentBinding
import com.simonsickle.sms2pdf.viewmodel.PdfBuilderViewModel
import java.io.File
import java.io.FileInputStream


class PdfBuilderFragment : Fragment(R.layout.pdf_builder_fragment) {
    private val viewModel by viewModels<PdfBuilderViewModel> {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PdfBuilderViewModel(activity!!.application) as T
            }
        }
    }

    private val args: PdfBuilderFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = PdfBuilderFragmentBinding.bind(view)
        viewModel.messageHtmlLiveData.observe(viewLifecycleOwner, Observer {
            binding.webView.loadDataWithBaseURL(null, it, "text/html", "UTF-8", null)
            binding.btnCreatePdf.isEnabled = true
        })
        viewModel.getCursorForConversation(args.threadId)
        binding.btnCreatePdf.setOnClickListener {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "text-backup.pdf")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS
            )
            val imageUri: Uri? =
                requireActivity().contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                )
            val outputStream = requireActivity().contentResolver.openFileDescriptor(imageUri!!, "rw")
            // TODO not quite right
            PdfConverter.getInstance()
                .convert(context, viewModel.messageHtmlLiveData.value!!, outputStream)
        }

    }
}