package umc.link.zip.presentation.create.adapter;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class CreateViewModel_Factory implements Factory<CreateViewModel> {
  @Override
  public CreateViewModel get() {
    return newInstance();
  }

  public static CreateViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CreateViewModel newInstance() {
    return new CreateViewModel();
  }

  private static final class InstanceHolder {
    private static final CreateViewModel_Factory INSTANCE = new CreateViewModel_Factory();
  }
}
