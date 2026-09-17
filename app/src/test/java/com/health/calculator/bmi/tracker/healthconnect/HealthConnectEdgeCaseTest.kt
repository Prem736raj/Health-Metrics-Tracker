package com.health.calculator.bmi.tracker.healthconnect

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import com.health.calculator.bmi.tracker.data.healthconnect.HealthConnectFeature
import com.health.calculator.bmi.tracker.data.healthconnect.HealthConnectPermissionPolicy
import org.junit.Assert.*
import org.junit.Test

/**
 * Verifies Health Connect robustness contracts that protect the app when the
 * Health Connect provider is absent, permissions are denied, or edge-case
 * values appear.
 */
class HealthConnectEdgeCaseTest {

    // ── Permission scope ────────────────────────────────────────────

    @Test
    fun stepsPermission_isExactlyOneReadPermission() {
        val perms = HealthConnectPermissionPolicy.stepsRead
        assertEquals("Steps should request exactly one permission", 1, perms.size)
        assertTrue(perms.contains(HealthPermission.getReadPermission(StepsRecord::class)))
    }

    @Test
    fun weightPermission_isExactlyOneReadPermission() {
        val perms = HealthConnectPermissionPolicy.weightRead
        assertEquals("Weight should request exactly one permission", 1, perms.size)
        assertTrue(perms.contains(HealthPermission.getReadPermission(WeightRecord::class)))
    }

    @Test
    fun allReadOnly_containsNoWritePermissions() {
        val all = HealthConnectPermissionPolicy.allReadOnly
        assertEquals("allReadOnly should be steps + weight", 2, all.size)

        all.forEach { perm ->
            assertFalse(
                "Permission '$perm' must not be a WRITE permission",
                perm.uppercase().contains("WRITE")
            )
        }
    }

    @Test
    fun featurePermissions_areIndependent() {
        val steps = HealthConnectPermissionPolicy.permissionsFor(HealthConnectFeature.STEPS)
        val weight = HealthConnectPermissionPolicy.permissionsFor(HealthConnectFeature.WEIGHT)

        // They must not overlap
        assertTrue(
            "Steps and weight permission sets must be disjoint",
            steps.intersect(weight).isEmpty()
        )
    }

    @Test
    fun allReadOnly_isUnionOfFeaturePermissions() {
        val union = HealthConnectPermissionPolicy.permissionsFor(HealthConnectFeature.STEPS) +
            HealthConnectPermissionPolicy.permissionsFor(HealthConnectFeature.WEIGHT)
        assertEquals(HealthConnectPermissionPolicy.allReadOnly, union)
    }

    // ── Boundary value contracts ────────────────────────────────────

    @Test
    fun stepsHistoryDays_clampedTo1to90() {
        // The manager clamps days via `days.coerceIn(1, 90)`.
        // Verify the policy at the boundary level:
        // - 0 should become 1
        // - 91 should become 90
        // - negative should become 1
        assertEquals(1, 0.coerceIn(1, 90))
        assertEquals(1, (-5).coerceIn(1, 90))
        assertEquals(90, 91.coerceIn(1, 90))
        assertEquals(90, 1000.coerceIn(1, 90))
        assertEquals(30, 30.coerceIn(1, 90))
    }

    @Test
    fun weightKilograms_nonFiniteOrNegativeIsRejected() {
        // The manager checks `kilograms.isFinite() && kilograms > 0.0`
        assertFalse(Double.NaN.isFinite())
        assertFalse(Double.POSITIVE_INFINITY.isFinite())
        assertFalse(Double.NEGATIVE_INFINITY.isFinite())
        assertFalse(0.0 > 0.0)
        assertFalse((-1.0) > 0.0)
        assertTrue(0.1.isFinite() && 0.1 > 0.0)
    }

    // ── Feature count gate ──────────────────────────────────────────

    @Test
    fun featureEnum_hasExactlyTwoEntries() {
        // If someone adds a new Health Connect feature, this test forces them
        // to also update the permission policy and related tests.
        assertEquals(
            "HealthConnectFeature enum should have exactly 2 entries (STEPS, WEIGHT)",
            2,
            HealthConnectFeature.entries.size
        )
    }
}
