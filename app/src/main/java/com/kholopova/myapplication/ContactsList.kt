package com.kholopova.myapplication

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun ContactsList() {
    val context = LocalContext.current
    val contacts = rememberSaveable(
        saver = listSaver(
            save = { contacts ->
                contacts.flatMap { listOf(it.name.orEmpty(), it.phoneNumber.orEmpty()) }
            },
            restore = { flat ->
                flat.chunked(2).map { pair ->
                    Contact(
                        name = pair[0].ifEmpty { null },
                        phoneNumber = pair.getOrElse(1) { "" }.ifEmpty { null }
                    )
                }
            }
        )
    ) {
        context.fetchAllContacts()
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
            Column(
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
            ) {
                Text(text = contact.name ?: stringResource(R.string.unknown_contact))
                if (!contact.phoneNumber.isNullOrBlank()) {
                    Text(
                        text = contact.phoneNumber,
                    )
                }
            }
        }
    }
}
