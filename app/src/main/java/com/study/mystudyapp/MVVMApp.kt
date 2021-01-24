package com.study.mystudyapp

import android.app.Application
import com.study.mystudyapp.database.repositories.HskRepository
import com.study.mystudyapp.database.repositories.UserRepository
import com.study.mystudyapp.database.room.AppDatabase
import com.study.mystudyapp.ui.hsk.hsk1.Hsk1ViewModelFactory
import com.study.mystudyapp.ui.login.LogInViewModelFactory
import com.study.mystudyapp.ui.main.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MVVMApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApp))

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { HskRepository(instance()) }


        bind() from provider { LogInViewModelFactory(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
        bind() from provider { Hsk1ViewModelFactory(instance()) }

    }

}