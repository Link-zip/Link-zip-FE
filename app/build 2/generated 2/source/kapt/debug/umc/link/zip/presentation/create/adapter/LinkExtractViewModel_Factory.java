package umc.link.zip.presentation.create.adapter;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import umc.link.zip.domain.repository.LinkRepository;

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
public final class LinkExtractViewModel_Factory implements Factory<LinkExtractViewModel> {
  private final Provider<LinkRepository> linkRepositoryProvider;

  public LinkExtractViewModel_Factory(Provider<LinkRepository> linkRepositoryProvider) {
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public LinkExtractViewModel get() {
    return newInstance(linkRepositoryProvider.get());
  }

  public static LinkExtractViewModel_Factory create(
      Provider<LinkRepository> linkRepositoryProvider) {
    return new LinkExtractViewModel_Factory(linkRepositoryProvider);
  }

  public static LinkExtractViewModel newInstance(LinkRepository linkRepository) {
    return new LinkExtractViewModel(linkRepository);
  }
}
