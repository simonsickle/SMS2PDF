package com.simonsickle.sms2pdf.fragment

import android.content.Intent
import android.os.Bundle
import android.print.PdfConverter
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
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
            val converter = PdfConverter()
            val path = requireContext().getExternalFilesDir(null)

            converter.print(
                binding.webView.createPrintDocumentAdapter("backup.pdf"),
                path,
                "backup+${args.threadId}.pdf",
                object : PdfConverter.CallbackPrint {
                    override fun onFailure() {
                        Toast.makeText(
                            context,
                            "well this failed, time to fix it",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun success(file: File) {
                        val uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.simonsickle.sms2pdf.pdfProvider", file
                        )
                        val share = Intent()
                        share.action = Intent.ACTION_SEND
                        share.type = "application/pdf"
                        share.putExtra(Intent.EXTRA_STREAM, uri)
                        requireContext().startActivity(Intent.createChooser(share, "Share"))
                    }
                })
        }

    }
}