package com.miled.marvel

import android.app.Application
import com.miled.marvel.core.network.AppApiServices
import com.miled.marvel.core.network.RetrofitClient
import com.miled.marvel.core.network.interceptor.ApiKeyInterceptorImpl
import com.miled.marvel.core.network.interceptor.ConnectivityInterceptorImpl
import com.miled.marvel.features.comics.data.dataSources.localDataSource.ComicsLocalDataSourceImpl
import com.miled.marvel.features.comics.data.dataSources.remoteDataSource.ComicsRemoteDataSourceImpl
import com.miled.marvel.features.comics.repository.ComicsRepositoryImpl
import com.miled.marvel.features.comics.viewModel.ComicsViewModel
import com.miled.marvel.features.favoriteComics.viewModel.FavoriteComicsViewModel
import com.orhanobut.hawk.Hawk
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import timber.log.Timber

class MarvelApplication : Application(), DIAware {
    override val di = DI {
        bindSingleton { ComicsViewModel(instance()) }
        bindSingleton { FavoriteComicsViewModel(instance()) }
        bindSingleton { ComicsRepositoryImpl(instance(), instance()) }
        bindSingleton { ComicsRemoteDataSourceImpl(instance()) }
        bindSingleton { ComicsLocalDataSourceImpl() }
        bindSingleton { ConnectivityInterceptorImpl() }
        bindSingleton { ApiKeyInterceptorImpl() }
        bindSingleton {
            RetrofitClient.getInstance(
                instance(),
                instance()
            ).create(AppApiServices::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}