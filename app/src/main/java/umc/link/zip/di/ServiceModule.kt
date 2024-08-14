package umc.link.zip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import umc.link.zip.data.service.LinkService
import umc.link.zip.data.service.LoginService
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
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.buildService()
    }

    fun provideZipService(retrofit: Retrofit): ZipService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideLinkService(retrofit: Retrofit): LinkService {
        return retrofit.buildService()
    }
}