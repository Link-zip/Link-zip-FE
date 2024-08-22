package umc.link.zip.presentation.zip.adapter;

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
public final class OpenZipViewModel_Factory implements Factory<OpenZipViewModel> {
  @Override
  public OpenZipViewModel get() {
    return newInstance();
  }

  public static OpenZipViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OpenZipViewModel newInstance() {
    return new OpenZipViewModel();
  }

  private static final class InstanceHolder {
    private static final OpenZipViewModel_Factory INSTANCE = new OpenZipViewModel_Factory();
  }
}
