package com.health.calculator.bmi.tracker.ui.components

import com.health.calculator.bmi.tracker.ui.theme.AppColors

import androidx.compose.ui.res.stringResource
import com.health.calculator.bmi.tracker.R

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun ProfilePictureSection(
    profilePictureUri: String?,
    initials: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(100.dp)) {
            Surface(
                onClick = onEditClick,
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (!profilePictureUri.isNullOrBlank()) {
                        AsyncImage(
                            model = profilePictureUri,
                            contentDescription = "Profile picture. Edit photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (initials.isNotBlank()) {
                        Text(
                            text = initials.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    } else {
                        Icon(Icons.Default.Person, "Your profile. Edit photo", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
            FilledIconButton(
                onClick = onEditClick,
                modifier = Modifier.align(Alignment.BottomEnd).size(48.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (AppColors.isDark) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.CameraAlt, "Edit profile photo", modifier = Modifier.size(22.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImagePickerDialog(
    onDismiss: () -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp, start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.txt_profile_picture),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ListItem(
                headlineContent = { Text(stringResource(R.string.txt_take_a_photo)) },
                leadingContent = { Icon(Icons.Default.CameraAlt, null) },
                modifier = Modifier.clickable { onCameraClick() }
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.txt_choose_from_gallery)) },
                leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
                modifier = Modifier.clickable { onGalleryClick() }
            )
            ListItem(
                headlineContent = { Text(stringResource(R.string.txt_remove_current_picture), color = MaterialTheme.colorScheme.error) },
                leadingContent = { Icon(Icons.Default.AddPhotoAlternate, null, tint = MaterialTheme.colorScheme.error) },
                modifier = Modifier.clickable {
                    onImageSelected(null)
                    onDismiss()
                }
            )
        }
    }
}
