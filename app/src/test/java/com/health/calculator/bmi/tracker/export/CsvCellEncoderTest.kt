package com.health.calculator.bmi.tracker.export

import com.health.calculator.bmi.tracker.data.export.CsvCellEncoder
import org.junit.Assert.assertEquals
import org.junit.Test

class CsvCellEncoderTest {
    @Test fun formulaPrefixesAreNeutralizedEvenAfterWhitespace() {
        listOf("=1+1", "+1+1", "-1+1", "@SUM(A1)", "  =1+1", "\t=1+1").forEach {
            assertEquals("'$it", CsvCellEncoder.encode(it))
        }
    }

    @Test fun quotedFormulaIsNeutralizedBeforeCsvEscaping() {
        assertEquals("\"'=HYPERLINK(\"\"url\"\",\"\"label\"\")\"",
            CsvCellEncoder.encode("=HYPERLINK(\"url\",\"label\")"))
    }

    @Test fun carriageReturnsQuotesAndLineFeedsStayInOneCell() {
        assertEquals("\"first\rsecond\"", CsvCellEncoder.encode("first\rsecond"))
        assertEquals("\"first\n\"\"second\"\"\"", CsvCellEncoder.encode("first\n\"second\""))
    }

    @Test fun ordinaryValuesArePreserved() {
        listOf("", "70.5", "120/80", "Water", "After lunch").forEach {
            assertEquals(it, CsvCellEncoder.encode(it))
        }
    }
}
