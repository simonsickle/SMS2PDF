package com.simonsickle.sms2pdf.html

import com.simonsickle.sms2pdf.models.SmsFolder
import com.simonsickle.sms2pdf.models.SmsModel
import kotlinx.html.*
import kotlinx.html.stream.createHTML

private val css by lazy {
    """
        |p {margin: 0px; padding: 0px;font-family: sans-serif;}
        |ol {padding: 0px;list-style: none;}
        |li {max-width: 75%;padding:10px;}
        |li.sent {float: right;clear: both;}
        |li.received {float: left;clear: both;}
        |div.message {background-color: #f1f1f1;border: 2px solid #dedede;border-radius: 10px;padding: 10px;margin: 10px 0;width: fit-content;}
        |div.message p {padding: 5px;}
        |li.received div.message {border-color: #ccc;background-color: #ddd;}
        |p.sender {font-size: 14px;}
        |p.timestamp {float: right;clear: right;font-size: 12px;}
""".trimMargin()
}

fun getHtmlForConversation(messages: List<SmsModel>): String {
    return createHTML(false).html {
        head {
            style { unsafe { +css } }
        }
        body {
            ol {
                messages.forEach { message ->
                    li(message.folderName.type) {
                        p("sender") {
                            if (message.folderName == SmsFolder.INBOX) {
                                +message.address
                            }
                        }
                        div("message") {
                            p { +message.message }
                        }
                        p("timestamp") { +message.time }
                    }
                }
            }
        }
    }.toString()
}