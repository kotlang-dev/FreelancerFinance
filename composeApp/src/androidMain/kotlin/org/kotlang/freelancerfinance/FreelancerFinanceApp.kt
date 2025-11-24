package org.kotlang.freelancerfinance

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.kotlang.freelancerfinance.di.initKoin

class FreelancerFinanceApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@FreelancerFinanceApp)
        }
    }

}