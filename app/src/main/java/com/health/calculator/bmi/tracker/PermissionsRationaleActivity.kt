package com.health.calculator.bmi.tracker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.health.calculator.bmi.tracker.core.constants.AppLinks
import com.health.calculator.bmi.tracker.ui.theme.HealthCalculatorTheme

/** Health Connect's policy link must work even before app onboarding is complete. */
class PermissionsRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Only the fixed, public policy URL is opened. Incoming intent data is ignored.
        if (openPrivacyPolicy()) return
        setContent {
            HealthCalculatorTheme {
                Scaffold { padding ->
                    Column(Modifier.fillMaxSize().padding(padding)
                        .verticalScroll(rememberScrollState()).padding(24.dp)) {
                        Text(stringResource(R.string.health_permissions_title),
                            style = MaterialTheme.typography.headlineSmall)
                        Text(stringResource(R.string.health_permissions_rationale),
                            modifier = Modifier.padding(vertical = 16.dp))
                        Text(AppLinks.PRIVACY_POLICY)
                        Button(onClick = { openPrivacyPolicy() }) {
                            Text(stringResource(R.string.health_permissions_open_policy))
                        }
                    }
                }
            }
        }
    }

    private fun openPrivacyPolicy(): Boolean = try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppLinks.PRIVACY_POLICY)))
        finish()
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }
}
