package com.health.calculator.bmi.tracker.data.export

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import java.io.File

internal class HealthReportPdf(private val context: Context) {
    private val navy = Color.rgb(21, 50, 47)
    private val teal = Color.rgb(0, 111, 101)
    private val muted = Color.rgb(67, 90, 85)
    private val wash = Color.rgb(231, 244, 239)
    private val title = paint(29f, Color.WHITE, true)
    private val heroLabel = paint(11f, Color.rgb(218, 246, 235))
    private val heading = paint(18f, navy, true)
    private val body = paint(11f, navy)
    private val emphasis = paint(12f, navy, true)
    private val small = paint(10f, muted)
    private val block = Paint(Paint.ANTI_ALIAS_FLAG)

    fun generate(report: HealthReportSnapshot): Uri {
        require(report.sections.isNotEmpty())
        val directory = File(context.cacheDir, "exports").apply { mkdirs() }
        val file = File.createTempFile("Health_Report_", ".pdf", directory)
        try {
            val document = PdfDocument()
            try {
                file.outputStream().use { output ->
                    draw(document, report)
                    document.writeTo(output)
                }
            } finally {
                document.close()
            }
            return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (error: Exception) {
            file.delete()
            throw error
        }
    }

    private fun draw(document: PdfDocument, report: HealthReportSnapshot) {
        var pageNumber = 0
        fun newPage() = document.startPage(PdfDocument.PageInfo.Builder(595, 842, ++pageNumber).create())
        fun onPage(drawPage: (PdfDocument.Page) -> Unit) {
            val page = newPage()
            try {
                drawPage(page)
                page.canvas.drawText("${ExportDisclosurePolicy.APP_NAME}  /  Page $pageNumber", 40f, 812f, small)
            } finally {
                document.finishPage(page)
            }
        }
        val maxCount = report.sections.maxOf { it.records.size }
        onPage { page ->
            val canvas = page.canvas
            block.color = navy
            canvas.drawRect(0f, 0f, 595f, 180f, block)
            canvas.drawText("PERSONAL HEALTH RECORDS", 40f, 47f, heroLabel)
            canvas.drawText("Health report", 40f, 88f, title)
            canvas.drawText(report.window.title, 40f, 117f, heroLabel)
            canvas.drawText("${report.recordCountLabel} across ${report.sectionCountLabel}", 40f, 144f, heroLabel)
            canvas.drawText("Records by tool", 40f, 221f, heading)
            val rowSpacing = minOf(26f, 366f / maxOf(1, report.sections.size - 1))
            report.sections.forEachIndexed { index, section ->
                val y = 251f + rowSpacing * index
                canvas.drawText(section.tool.title, 40f, y, body)
                block.color = wash
                canvas.drawRoundRect(265f, y - 9f, 503f, y + 1f, 5f, 5f, block)
                block.color = teal
                val width = (238f * section.records.size / maxCount).coerceAtLeast(4f)
                canvas.drawRoundRect(265f, y - 9f, 265f + width, y + 1f, 5f, 5f, block)
                canvas.drawText(section.records.size.toString(), 516f, y, emphasis)
            }
            val footer = layout(
                "Dates and times use ${report.window.zone.id}. Empty days are omitted. " +
                    "Cached step totals of zero are omitted because older entries can represent unavailable data.\n\n" +
                    ExportDisclosurePolicy.shareFooter(),
                small, 515
            )
            canvas.save()
            canvas.translate(40f, 641f)
            footer.draw(canvas)
            canvas.restore()
        }

        report.tablePages().forEach { blocks ->
            onPage { page ->
                val canvas = page.canvas
                var top = 42f
                blocks.forEach { table ->
                    val section = table.section
                    block.color = wash
                    canvas.drawRoundRect(40f, top, 555f, top + 47f, 9f, 9f, block)
                    canvas.drawText(section.tool.title, 54f, top + 21f, heading)
                    val count = recordCount(section.records.size)
                    val status = if (table.continued) "$count  ·  Continued" else count
                    canvas.drawText(status, 54f, top + 38f, small)
                    canvas.drawText("Date", 48f, top + 64f, small)
                    canvas.drawText("Record", 221f, top + 64f, small)
                    canvas.drawText("Context", 394f, top + 64f, small)
                    table.rows.forEachIndexed { row, record ->
                        val y = top + 85f + 24f * row
                        if (row % 2 == 0) {
                            block.color = wash
                            canvas.drawRect(40f, y - 14f, 555f, y + 7f, block)
                        }
                        val date = report.window.date(record.timestamp)
                        val dateText = when (section.tool) {
                            ReportTool.FOOD_LOG, ReportTool.WATER_LOG, ReportTool.STEPS -> date
                            else -> "$date  ${report.window.time(record.timestamp)}"
                        }
                        canvas.drawText(fit(dateText, small, 165f), 48f, y, small)
                        canvas.drawText(fit(record.result, emphasis, 165f), 221f, y, emphasis)
                        canvas.drawText(fit(record.context, small, 153f), 394f, y, small)
                    }
                    top += 96f + 24f * table.rows.size
                }
            }
        }
    }

    private fun recordCount(count: Int) = if (count == 1) "1 record" else "$count records"

    private fun fit(text: String, style: TextPaint, width: Float): String =
        android.text.TextUtils.ellipsize(text, style, width, android.text.TextUtils.TruncateAt.END).toString()

    private fun layout(text: String, style: TextPaint, width: Int) =
        StaticLayout.Builder.obtain(text, 0, text.length, style, width).build()

    private fun paint(size: Float, ink: Int, bold: Boolean = false) =
        TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size
            color = ink
            typeface = if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
}
