package com.study.mystudyapp

import android.app.Application
import com.study.mystudyapp.database.room.AppDatabase
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class MVVMApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApp))


        bind() from singleton { AppDatabase() }
    }

}