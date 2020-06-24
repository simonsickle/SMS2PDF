package com.simonsickle.sms2pdf.models

import android.database.Cursor
import android.provider.Telephony
import android.telephony.PhoneNumberUtils
import java.text.SimpleDateFormat
import java.util.*

enum class SmsFolder(val type: String) {
    INBOX("received"),
    SENT("sent")
}

data class SmsModel(
    val id: String,
    val thread: String,
    val address: String,
    val message: String,
    val readState: Boolean,
    val time: String,
    val folderName: SmsFolder
) {
    companion object {
        private fun timestampToDate(time: Long): String {
            return SimpleDateFormat("MMM d, yyyy '-' h:mm:ss a", Locale.getDefault())
                .format(Date(time))
        }

        private fun formatPhoneNumber(phone: String): String {
            return PhoneNumberUtils.formatNumber(phone, Locale.US.country)
        }

        fun fromCursor(c: Cursor): SmsModel {
            return SmsModel(
                id = c.getString(c.getColumnIndexOrThrow("_id")),
                thread = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.THREAD_ID)),
                address = formatPhoneNumber(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))),
                message = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY)),
                readState = c.getString(c.getColumnIndex(Telephony.Sms.READ)) == "1",
                time = timestampToDate(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE)).toLong()),
                folderName = if (c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)) == Telephony.Sms.MESSAGE_TYPE_INBOX.toString()) SmsFolder.INBOX else SmsFolder.SENT
            )
        }
    }
}