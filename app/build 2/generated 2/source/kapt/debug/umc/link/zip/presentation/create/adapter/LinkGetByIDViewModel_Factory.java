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
public final class LinkGetByIDViewModel_Factory implements Factory<LinkGetByIDViewModel> {
  private final Provider<LinkRepository> linkRepositoryProvider;

  public LinkGetByIDViewModel_Factory(Provider<LinkRepository> linkRepositoryProvider) {
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public LinkGetByIDViewModel get() {
    return newInstance(linkRepositoryProvider.get());
  }

  public static LinkGetByIDViewModel_Factory create(
      Provider<LinkRepository> linkRepositoryProvider) {
    return new LinkGetByIDViewModel_Factory(linkRepositoryProvider);
  }

  public static LinkGetByIDViewModel newInstance(LinkRepository linkRepository) {
    return new LinkGetByIDViewModel(linkRepository);
  }
}
