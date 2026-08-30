package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.repository.NearTwoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TwoKmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.initialize(this)

        // Prepopulate seed data asynchronously if empty
        CoroutineScope(Dispatchers.IO).launch {
            val repo = NearTwoRepository(AppDatabase.getDatabase())
            repo.initializeDatabaseIfEmpty()
        }
    }
}
