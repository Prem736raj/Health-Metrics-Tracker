package com.health.calculator.bmi.tracker.ui.components

import com.health.calculator.bmi.tracker.data.models.MilestoneCategory
import com.health.calculator.bmi.tracker.data.models.MilestoneType
import com.health.calculator.bmi.tracker.data.models.PersonalRecordType
import com.health.calculator.bmi.tracker.data.models.ReminderCategory
import org.junit.Assert.assertNotNull
import org.junit.Test

class WellnessIconMappingTest {

    @Test
    fun `every milestone category has a stable vector icon`() {
        MilestoneCategory.values().forEach { category ->
            assertNotNull("Missing icon for $category", milestoneCategoryIcon(category))
        }
    }

    @Test
    fun `every milestone and personal record type has a stable vector icon`() {
        MilestoneType.values().forEach { type ->
            assertNotNull("Missing icon for $type", milestoneIcon(type))
        }
        PersonalRecordType.values().forEach { type ->
            assertNotNull("Missing icon for $type", personalRecordIcon(type))
        }
    }

    @Test
    fun `metabolic labels map to non emoji vector icons`() {
        listOf(
            "Central waist measurement",
            "Elevated blood pressure",
            "Elevated fasting glucose",
            "Reduced HDL",
            "Elevated triglycerides"
        ).forEach { label ->
            assertNotNull("Missing icon for $label", metabolicCriterionIcon(label))
        }
    }

    @Test
    fun `every reminder category has a stable vector icon`() {
        ReminderCategory.entries.forEach { category ->
            assertNotNull("Missing icon for $category", reminderCategoryIcon(category))
        }
    }
}
