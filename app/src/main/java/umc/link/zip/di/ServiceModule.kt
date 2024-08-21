package umc.link.zip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import umc.link.zip.data.service.HomeService
import umc.link.zip.data.service.ListService
import umc.link.zip.data.service.AlertService
import umc.link.zip.data.service.LinkService
import umc.link.zip.data.service.LoginService

import umc.link.zip.data.service.MypageService
import umc.link.zip.data.service.SearchService
import umc.link.zip.data.service.TestService
import umc.link.zip.data.service.ZipService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }

    @Provides
    @Singleton
    fun provideTestService(retrofit: Retrofit): TestService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideListService(retrofit: Retrofit): ListService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideZipService(retrofit: Retrofit): ZipService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideLinkService(retrofit: Retrofit): LinkService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideMypageService(retrofit: Retrofit): MypageService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideAlertService(retrofit: Retrofit): AlertService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit): SearchService {
        return retrofit.buildService()
    }

}
