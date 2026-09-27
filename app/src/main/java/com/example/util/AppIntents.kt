package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object AppIntents {
    fun openGoogleMaps(context: Context, latitude: Double, longitude: Double, label: String, fallbackCity: String = "") {
        try {
            val uri = if (latitude != 0.0 && longitude != 0.0) {
                // Open directions or pin location
                Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
            } else {
                val query = if (fallbackCity.isNotEmpty()) "$label, $fallbackCity" else label
                Uri.parse("geo:0,0?q=" + Uri.encode(query))
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to launch map app", Toast.LENGTH_SHORT).show()
        }
    }

    fun dialPhoneNumber(context: Context, rawPhone: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${rawPhone.trim()}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to start phone dialer", Toast.LENGTH_SHORT).show()
        }
    }
}
