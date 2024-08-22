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
public final class LinkUpdateLikeViewModel_Factory implements Factory<LinkUpdateLikeViewModel> {
  private final Provider<LinkRepository> linkRepositoryProvider;

  public LinkUpdateLikeViewModel_Factory(Provider<LinkRepository> linkRepositoryProvider) {
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public LinkUpdateLikeViewModel get() {
    return newInstance(linkRepositoryProvider.get());
  }

  public static LinkUpdateLikeViewModel_Factory create(
      Provider<LinkRepository> linkRepositoryProvider) {
    return new LinkUpdateLikeViewModel_Factory(linkRepositoryProvider);
  }

  public static LinkUpdateLikeViewModel newInstance(LinkRepository linkRepository) {
    return new LinkUpdateLikeViewModel(linkRepository);
  }
}
