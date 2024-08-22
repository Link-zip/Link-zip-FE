package umc.link.zip.presentation.test;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import umc.link.zip.domain.repository.TestRepository;

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
public final class TestViewModel_Factory implements Factory<TestViewModel> {
  private final Provider<TestRepository> repositoryProvider;

  public TestViewModel_Factory(Provider<TestRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public TestViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static TestViewModel_Factory create(Provider<TestRepository> repositoryProvider) {
    return new TestViewModel_Factory(repositoryProvider);
  }

  public static TestViewModel newInstance(TestRepository repository) {
    return new TestViewModel(repository);
  }
}
