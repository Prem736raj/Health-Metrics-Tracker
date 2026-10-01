package com.health.calculator.bmi.tracker

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ManifestPolicyTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test fun healthConnectRationaleDoesNotResolveToDashboard() {
        val intent = Intent("androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE")
            .setPackage(context.packageName)
        val activities = context.packageManager.queryIntentActivities(intent, 0)
        assertEquals(1, activities.size)
        assertEquals(PermissionsRationaleActivity::class.java.name, activities.single().activityInfo.name)
    }

    @Test fun platformHealthPermissionAliasIsPermissionProtected() {
        val component = ComponentName(context.packageName,
            "com.health.calculator.bmi.tracker.ViewHealthPermissionUsageActivity")
        val info = context.packageManager.getActivityInfo(component, 0)
        assertEquals("android.permission.START_VIEW_PERMISSION_USAGE", info.permission)
        assertEquals(PermissionsRationaleActivity::class.java.name, info.targetActivity)
    }

    @Test fun advertisingPermissionsAreNotShipped() {
        val permissions = context.packageManager.getPackageInfo(context.packageName,
            PackageManager.GET_PERMISSIONS).requestedPermissions.orEmpty().toSet()
        assertFalse("com.google.android.gms.permission.AD_ID" in permissions)
        assertFalse("android.permission.ACCESS_ADSERVICES_AD_ID" in permissions)
        assertFalse("android.permission.ACCESS_ADSERVICES_ATTRIBUTION" in permissions)
        assertTrue("android.permission.ACCESS_NETWORK_STATE" in permissions)
    }
}
