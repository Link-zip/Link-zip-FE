package umc.link.zip.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import umc.link.zip.domain.model.search.LocalDatabase
import umc.link.zip.domain.model.search.SearchKeywordDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): LocalDatabase {
        return Room.databaseBuilder(
            appContext,
            LocalDatabase::class.java,
            "local_database"
        ).build()
    }

    @Provides
    fun provideSearchKeywordDao(database: LocalDatabase): SearchKeywordDao {
        return database.getSearchKeywordDao()
    }
}