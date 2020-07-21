package com.example.kotlinpdfexample

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import java.io.*
import java.lang.Exception

class PdfDocumentAdapter(context: Context,path: String): PrintDocumentAdapter() {

    internal var context: Context ?= null
    internal var  path = "null"

    init{
        this.context = context
        this.path = path
    }
    override fun onLayout(
        p0: PrintAttributes?,
        p1: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        LayoutResultCallback: LayoutResultCallback?,
        p4: Bundle?
    ) {
        if(cancellationSignal!!.isCanceled)
           LayoutResultCallback!!.onLayoutCancelled()

        else{
            val builder = PrintDocumentInfo.Builder("file name")
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()
            LayoutResultCallback!!.onLayoutFinished(builder.build(),p1 != p0)
        }
    }

    override fun onWrite(
        pageRanges: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        writeResultCallback: WriteResultCallback?
    ) {
        var input:InputStream ?= null
        var out: OutputStream ?= null

        try {
            val file = File(path)
            input = FileInputStream(file)
            out = FileOutputStream(parcelFileDescriptor!!.fileDescriptor)

            if(!cancellationSignal!!.isCanceled)
            {
                input.copyTo(out)
                writeResultCallback!!.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
            else
                writeResultCallback!!.onWriteCancelled()
        }catch (e: Exception){
            writeResultCallback!!.onWriteFailed(e.message)
            Log.e("Bo Bo", e.message)
        }finally {
            try {
                input!!.close()
                out!!.close()
            }catch (e: IOException)
            {
                Log.e("Bo Bo", e.message)
            }
        }
    }
}