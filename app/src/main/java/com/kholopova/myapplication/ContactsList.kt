package com.kholopova.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

private var cachedContacts: List<Contact>? = null

fun clearContactsCache() {
    cachedContacts = null
}

@Composable
fun ContactsList() {
    val context = LocalContext.current
    val contacts = remember {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            clearContactsCache()
            emptyList()
        } else {
            cachedContacts ?: context.fetchAllContacts().also { cachedContacts = it }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = if (contacts.isEmpty()) {
                    stringResource(R.string.no_contacts_found)
                } else {
                    stringResource(R.string.found_contacts, contacts.size)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
        items(contacts) { contact ->
            Text(
                text = contact.name ?: stringResource(R.string.unknown_contact),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val number = contact.phoneNumber ?: return@clickable
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:$number".toUri()
                        }
                        context.startActivity(intent)
                    }
                    .padding(vertical = 8.dp)
            )
        }
    }
}
