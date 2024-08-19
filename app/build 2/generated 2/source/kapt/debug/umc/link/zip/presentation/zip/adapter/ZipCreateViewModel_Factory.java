package umc.link.zip.presentation.zip.adapter;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import umc.link.zip.domain.repository.ZipRepository;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ZipCreateViewModel_Factory implements Factory<ZipCreateViewModel> {
  private final Provider<ZipRepository> zipRepositoryProvider;

  public ZipCreateViewModel_Factory(Provider<ZipRepository> zipRepositoryProvider) {
    this.zipRepositoryProvider = zipRepositoryProvider;
  }

  @Override
  public ZipCreateViewModel get() {
    return newInstance(zipRepositoryProvider.get());
  }

  public static ZipCreateViewModel_Factory create(Provider<ZipRepository> zipRepositoryProvider) {
    return new ZipCreateViewModel_Factory(zipRepositoryProvider);
  }

  public static ZipCreateViewModel newInstance(ZipRepository zipRepository) {
    return new ZipCreateViewModel(zipRepository);
  }
}
