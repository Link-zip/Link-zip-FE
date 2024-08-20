package umc.link.zip.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import umc.link.zip.data.repositoryImpl.ListRepositoryImpl
import umc.link.zip.data.repositoryImpl.AlertRepositoryImpl
import umc.link.zip.data.repositoryImpl.LinkRepositoryImpl
import umc.link.zip.data.repositoryImpl.LoginRepositoryImpl
import umc.link.zip.data.repositoryImpl.SearchRepositoryImpl
import umc.link.zip.data.repositoryImpl.TestRepositoryImpl
import umc.link.zip.data.repositoryImpl.ZipRepositoryImpl
import umc.link.zip.data.service.ListService
import umc.link.zip.data.service.AlertService
import umc.link.zip.data.service.LinkService
import umc.link.zip.data.service.LoginService
import umc.link.zip.data.service.SearchService
import umc.link.zip.data.service.TestService
import umc.link.zip.data.service.ZipService
import umc.link.zip.domain.repository.AlertRepository
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.domain.repository.LoginRepository
import umc.link.zip.domain.repository.ListRepository
import umc.link.zip.domain.repository.SearchRepository
import umc.link.zip.domain.repository.TestRepository
import umc.link.zip.domain.repository.ZipRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    // 스코프 애노테이션이 있음
    // 해당하는 Hilt 컴포넌트의 수명동안 매 요청에 동일 인스턴스를 반환
    // 다음의 경우 viewModel의 수명동안 동일 인스턴스를 반환
    @ViewModelScoped
    @Provides
    fun providesTestRepository(
        testService: TestService
    ): TestRepository = TestRepositoryImpl(testService)

    @ViewModelScoped
    @Provides
    fun providesLoginRepository(
        loginService: LoginService
    ): LoginRepository = LoginRepositoryImpl(loginService)

    @ViewModelScoped
    @Provides
    fun provideZipRepository(
        zipService: ZipService
    ): ZipRepository = ZipRepositoryImpl(zipService)

    @ViewModelScoped
    @Provides
    fun providesListRepository(
        listService: ListService
    ): ListRepository = ListRepositoryImpl(listService)

    @ViewModelScoped
    @Provides
    fun provideLinkRepository(
        linkService: LinkService
    ): LinkRepository = LinkRepositoryImpl(linkService)

    @ViewModelScoped
    @Provides
    fun providesAlertRepository(
    alertService: AlertService
    ): AlertRepository = AlertRepositoryImpl(alertService)

    @ViewModelScoped
    @Provides
    fun providesListRepository(
        listService: ListService
    ): ListRepository = ListRepositoryImpl(listService)

    @ViewModelScoped
    @Provides
    fun providesSearchRepository(
        searchService: SearchService
    ): SearchRepository = SearchRepositoryImpl(searchService)
}
