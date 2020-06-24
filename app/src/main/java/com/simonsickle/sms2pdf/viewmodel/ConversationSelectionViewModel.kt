package com.simonsickle.sms2pdf.viewmodel

import android.app.Application
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simonsickle.sms2pdf.models.Conversations

class ConversationSelectionViewModel(application: Application) : ViewModel() {
    private val contentResolver = application.contentResolver
    private val conversationsData = MutableLiveData<List<Conversations>>()
    val conversationsLiveData: LiveData<List<Conversations>> = conversationsData

    init {
        //TODO: Go get conversations!
        getConversations()
    }

    private fun getConversations() {
        val conversations = mutableListOf<Conversations>()
        val mms =
            contentResolver.query(Telephony.Threads.CONTENT_URI, null, null, null, null)
        mms?.use {
            mms.moveToPosition(-1)
            while (mms.moveToNext()) {
                conversations.add(Conversations.fromCursor(mms))
            }
        }

        conversationsData.postValue(conversations)

        Log.i("SIMON", "Got some convos, ${conversations.size}")
    }
}