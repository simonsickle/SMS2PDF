package com.simonsickle.sms2pdf.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.simonsickle.sms2pdf.R
import com.simonsickle.sms2pdf.databinding.ConversationFragmentBinding
import com.simonsickle.sms2pdf.recyclers.ConversationAdapter
import com.simonsickle.sms2pdf.viewmodel.ConversationSelectionViewModel

class ConversationSelectionFragment : Fragment(R.layout.conversation_fragment) {
    private val viewModel by viewModels<ConversationSelectionViewModel> {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ConversationSelectionViewModel(activity!!.application) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ConversationFragmentBinding.bind(view)

        val conversationAdapter = ConversationAdapter {
            findNavController().navigate(ConversationSelectionFragmentDirections.goToHtmlView(it))
        }

        binding.conversationRecycler.apply {
            adapter = conversationAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.conversationsLiveData.observe(viewLifecycleOwner, Observer { conversations ->
            conversations?.let {
                conversationAdapter.conversations = it
                conversationAdapter.notifyDataSetChanged()
            }
        })
    }

}