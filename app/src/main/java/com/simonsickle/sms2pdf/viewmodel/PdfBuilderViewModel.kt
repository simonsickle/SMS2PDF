package com.simonsickle.sms2pdf.viewmodel

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simonsickle.sms2pdf.html.getHtmlForConversation
import com.simonsickle.sms2pdf.models.SmsModel

class PdfBuilderViewModel(application: Application) : ViewModel() {
    private val contentResolver = application.contentResolver
    private val messageData = MutableLiveData<String>()
    val messageHtmlLiveData: LiveData<String> = messageData

    fun getCursorForConversation(threadId: Int) {
        val texts = mutableListOf<SmsModel>()
        val mms =
            contentResolver.query(
                Telephony.Sms.CONTENT_URI, null,
                Telephony.Sms.THREAD_ID + "=$threadId", null, null
            )
        mms?.use {
            mms.moveToPosition(-1)
            while (mms.moveToNext()) {
                texts.add(SmsModel.fromCursor(mms))
            }
        }

        messageData.postValue(getHtmlForConversation(texts))
    }
}