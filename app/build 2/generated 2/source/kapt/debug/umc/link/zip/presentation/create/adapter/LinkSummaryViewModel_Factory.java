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
public final class LinkSummaryViewModel_Factory implements Factory<LinkSummaryViewModel> {
  private final Provider<LinkRepository> linkRepositoryProvider;

  public LinkSummaryViewModel_Factory(Provider<LinkRepository> linkRepositoryProvider) {
    this.linkRepositoryProvider = linkRepositoryProvider;
  }

  @Override
  public LinkSummaryViewModel get() {
    return newInstance(linkRepositoryProvider.get());
  }

  public static LinkSummaryViewModel_Factory create(
      Provider<LinkRepository> linkRepositoryProvider) {
    return new LinkSummaryViewModel_Factory(linkRepositoryProvider);
  }

  public static LinkSummaryViewModel newInstance(LinkRepository linkRepository) {
    return new LinkSummaryViewModel(linkRepository);
  }
}
