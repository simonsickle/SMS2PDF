package com.simonsickle.sms2pdf.models

import android.database.Cursor
import android.provider.Telephony

data class Conversations(
    val threadID: Int,
    val address: String
) {
    companion object {
        fun fromCursor(cursor: Cursor): Conversations{
            return Conversations(
                threadID = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.Conversations.THREAD_ID)),
                address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.ADDRESS))
            )
        }
    }
}