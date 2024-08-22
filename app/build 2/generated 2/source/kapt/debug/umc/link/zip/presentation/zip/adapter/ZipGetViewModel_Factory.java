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
public final class ZipGetViewModel_Factory implements Factory<ZipGetViewModel> {
  private final Provider<ZipRepository> zipRepositoryProvider;

  public ZipGetViewModel_Factory(Provider<ZipRepository> zipRepositoryProvider) {
    this.zipRepositoryProvider = zipRepositoryProvider;
  }

  @Override
  public ZipGetViewModel get() {
    return newInstance(zipRepositoryProvider.get());
  }

  public static ZipGetViewModel_Factory create(Provider<ZipRepository> zipRepositoryProvider) {
    return new ZipGetViewModel_Factory(zipRepositoryProvider);
  }

  public static ZipGetViewModel newInstance(ZipRepository zipRepository) {
    return new ZipGetViewModel(zipRepository);
  }
}
