package umc.link.zip.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import umc.link.zip.domain.model.search.KeywordLocalSource
import umc.link.zip.domain.model.search.KeywordLocalSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    abstract fun bindKeywordLocalSource(
        impl: KeywordLocalSourceImpl
    ): KeywordLocalSource
}
