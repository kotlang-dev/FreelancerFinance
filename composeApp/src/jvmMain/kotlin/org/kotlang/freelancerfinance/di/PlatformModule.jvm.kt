package org.kotlang.freelancerfinance.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import org.kotlang.freelancerfinance.data.local.getDesktopFile
import org.kotlang.freelancerfinance.data.util.Constants

actual val platformModule : Module = module {

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            getDesktopFile(Constants.DATASTORE_NAME).absolutePath.toPath()
        }
    }

}