package com.brainup.readbyapp.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.browser.customtabs.CustomTabsIntent.Builder
import androidx.core.content.ContextCompat
import com.brainup.readbyapp.R
import com.brainup.readbyapp.utils.ViewDocumentHelper.MediaType.UNKNOWN


object ViewDocumentHelper {
    private const val URL = "http://docs.google.com/gview?embedded=true&url="

    enum class MediaType {
        AUDIO, VIDEO, IMAGE, PDF, UNKNOWN, DOC, PPT, XLS
    }

    private const val VIDEO_MOV_FORMAT = "MOV"
    private const val AUDIO_CAF_FORMAT = "caf"
    private const val IMAGE_MIME_TYPE = "image"
    private const val AUDIO_MIME_TYPE = "audio"
    private const val VIDEO_MIME_TYPE = "video"
    private const val PDF_MIME_TYPE = "pdf"
    private const val DOC_MIME_TYPE = "doc"
    private const val DOCX_MIME_TYPE = "docx"
    private const val PPT_MIME_TYPE = "ppt"
    private const val PPTX_MIME_TYPE = "pptx"
    private const val XLS_MIME_TYPE = "xls"
    private const val XLSX_MIME_TYPE = "xlsx"

    fun navigateToCustomTab(context: Context, url: String) {
        val sb = StringBuilder()
        if (getExtension(url) == MediaType.XLS || getExtension(url) == MediaType.PPT) {
            sb.append(URL)
        }
        sb.append(url)
        val builder = Builder()
        // builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        builder.setCloseButtonIcon(
             getBitmapFromDrawable(
                 context,
                 R.drawable.ic_arrow_back_black_24dp
             )
        )
        builder.setShowTitle(true)
        builder.enableUrlBarHiding()
        val customTabsIntent = builder.build()

        customTabsIntent.launchUrl(context, Uri.parse(sb.toString()))
    }

    private fun getBitmapFromDrawable(context: Context, drawableId: Int): Bitmap {
        return when (val drawable = ContextCompat.getDrawable(context, drawableId)) {
            is BitmapDrawable -> drawable.bitmap
            is VectorDrawable -> getBitmapFromVectorDrawable((drawable as VectorDrawable?)!!)
            else -> throw IllegalArgumentException("Unable to convert to bitmap")
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBitmapFromVectorDrawable(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun getExtension(url: String): MediaType {
        val type: String?
        var mediaType = UNKNOWN
        try {
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension!!.equals(VIDEO_MOV_FORMAT, ignoreCase = true)) {
                mediaType = MediaType.VIDEO
                return mediaType
            }
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (type == null && extension.equals(AUDIO_CAF_FORMAT, ignoreCase = true)) {
                mediaType = MediaType.AUDIO
                return mediaType
            } else if (extension.equals(DOC_MIME_TYPE, ignoreCase = true) || extension.equals(
                    DOCX_MIME_TYPE,
                    ignoreCase = true
                )
            ) {
                mediaType = MediaType.DOC
                return mediaType
            } else if (extension.equals(PPT_MIME_TYPE, ignoreCase = true) || extension.equals(
                    PPTX_MIME_TYPE,
                    ignoreCase = true
                )
            ) {
                mediaType = MediaType.PPT
                return mediaType
            } else if (extension.equals(XLS_MIME_TYPE, ignoreCase = true) || extension.equals(
                    XLSX_MIME_TYPE,
                    ignoreCase = true
                )
            ) {
                mediaType = MediaType.XLS
                return mediaType
            }
            try {
                val str = type!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                mediaType = if (str[0].equals(IMAGE_MIME_TYPE, ignoreCase = true)) {
                    MediaType.IMAGE
                } else if (str[0].equals(AUDIO_MIME_TYPE, ignoreCase = true)) {
                    MediaType.AUDIO
                } else if (str[0].equals(VIDEO_MIME_TYPE, ignoreCase = true)) {
                    MediaType.VIDEO
                } else if (str[0].equals(PDF_MIME_TYPE, ignoreCase = true) || str[1].equals(
                        PDF_MIME_TYPE,
                        ignoreCase = true
                    )
                ) {
                    MediaType.PDF
                } else {
                    UNKNOWN
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mediaType
    }
}
