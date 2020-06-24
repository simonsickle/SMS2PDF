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
        getConversations()
    }

    private fun getConversations() {
        val conversations = mutableListOf<Conversations>()
        val cursor = contentResolver.query(Telephony.Threads.CONTENT_URI,
        arrayOf(Telephony.Sms.Conversations.THREAD_ID, Telephony.TextBasedSmsColumns.ADDRESS),
        null,
        null,
        null)

        cursor?.use {
            cursor.moveToPosition(-1)
            while (cursor.moveToNext()) {
                conversations.add(Conversations.fromCursor(cursor))
            }
        }

        conversationsData.postValue(conversations)

        Log.i("SIMON", "Got some convos, ${conversations.size}")
    }
}