package com.example.isgoman_kotlin.activity.pdf

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.*

class PDFPrintDocumentAdapter
/**
 * @param context
 * @param fileName - for Print Document Info
 * @param filePath - PDF file to be printed, stored in external storage
 */(private val context: Context, private val fileName: String, private val filePath: String) :
    PrintDocumentAdapter() {
    override fun onLayout(
        oldAttributes: PrintAttributes,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle
    ) {
        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }
        val pdi = PrintDocumentInfo.Builder(fileName)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build()
        callback.onLayoutFinished(pdi, true)
    }

    override fun onWrite(
        pages: Array<PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            input = FileInputStream(filePath)
            output = FileOutputStream(destination.fileDescriptor)
            val buf = ByteArray(1024)
            var bytesRead: Int
            while (input.read(buf).also { bytesRead = it } > 0) {
                output.write(buf, 0, bytesRead)
            }
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (ee: FileNotFoundException) {
            //Catch exception
        } catch (e: Exception) {
            //Catch exception
        } finally {
            try {
                input?.close()
                output?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onFinish() {
        super.onFinish()
        //  Toast.makeText(context, "Printing is completed", Toast.LENGTH_SHORT).show();
    }
}