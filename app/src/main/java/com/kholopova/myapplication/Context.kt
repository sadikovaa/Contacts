package com.kholopova.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.core.database.getStringOrNull

@SuppressLint("Range")
fun Context.fetchAllContacts(): List<Contact> {
    Log.i("FETCH", "FETCH")
    return try {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
            .use { cursor: Cursor? ->
                if (cursor == null) return emptyList()
                return buildList {
                    while (cursor.moveToNext()) {
                        val name =
                            cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                        val phoneNumber =
                            cursor.getStringOrNull(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                        add(Contact(name, phoneNumber))
                    }
                }
            }
    } catch (e: SecurityException) {
        emptyList()
    }
}