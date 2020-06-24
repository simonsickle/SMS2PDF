package com.simonsickle.sms2pdf

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.simonsickle.sms2pdf.html.getHtmlForConversation
import com.simonsickle.sms2pdf.models.SmsModel
import com.simonsickle.sms2pdf.fragment.ConversationSelectionFragment
import com.simonsickle.sms2pdf.models.Conversations

class MainActivity : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_SMS
    )

    private fun getPermissionsIfNeeded() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 42)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        getPermissionsIfNeeded()
    }
}