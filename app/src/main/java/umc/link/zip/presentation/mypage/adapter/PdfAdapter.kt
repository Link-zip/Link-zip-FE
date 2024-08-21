package umc.link.zip.presentation.mypage.adapter

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class PdfAdapter(private val resources: Resources, private val resourceId: Int) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private val pageCount: Int

    init {
        val file = getFileFromRaw(resources, resourceId)
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(fileDescriptor!!)
        pageCount = pdfRenderer!!.pageCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val imageView = LayoutInflater.from(parent.context).inflate(R.layout.item_pdf, parent, false) as ImageView
        return PdfViewHolder(imageView)
    }

    override fun getItemCount(): Int = pageCount

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val page = pdfRenderer!!.openPage(position)
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        holder.imageView.setImageBitmap(bitmap)
        page.close()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        pdfRenderer?.close()
        fileDescriptor?.close()
    }

    class PdfViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    private fun getFileFromRaw(resources: Resources, resourceId: Int): File {
        val file = File.createTempFile("temp_pdf", ".pdf")
        try {
            val inputStream: InputStream = resources.openRawResource(resourceId)
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}
