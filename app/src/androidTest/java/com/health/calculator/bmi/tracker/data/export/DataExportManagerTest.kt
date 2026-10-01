package com.health.calculator.bmi.tracker.data.export

import android.content.Context
import android.content.ContextWrapper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataExportManagerTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test fun successfulExportReturnsReadableUri() = runBlocking {
        val manager = DataExportManager(context)
        assertTrue(manager.exportData(emptyList(), ExportConfig(format = ExportFormat.CSV)))
        val progress = manager.exportProgress.value
        assertTrue(progress.isComplete)
        assertFalse(progress.isExporting)
        assertNull(progress.error)
        context.contentResolver.openInputStream(progress.resultUri!!).use { stream ->
            assertNotNull(stream)
            assertTrue(stream!!.readBytes().isNotEmpty())
        }
    }

    @Test fun providerFailureReturnsFalseAndSafeError() = runBlocking {
        val invalidProviderContext = object : ContextWrapper(context) {
            override fun getPackageName(): String = "invalid.test.provider"
        }
        val manager = DataExportManager(invalidProviderContext)
        assertFalse(manager.exportData(emptyList(), ExportConfig(format = ExportFormat.CSV)))
        val progress = manager.exportProgress.value
        assertFalse(progress.isComplete)
        assertFalse(progress.isExporting)
        assertNull(progress.resultUri)
        assertNotNull(progress.error)
        assertFalse(progress.error!!.contains("invalid.test.provider"))
    }
}
