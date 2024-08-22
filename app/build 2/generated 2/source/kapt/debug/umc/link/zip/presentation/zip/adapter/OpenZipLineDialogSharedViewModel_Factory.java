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
public final class OpenZipLineDialogSharedViewModel_Factory implements Factory<OpenZipLineDialogSharedViewModel> {
  @Override
  public OpenZipLineDialogSharedViewModel get() {
    return newInstance();
  }

  public static OpenZipLineDialogSharedViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OpenZipLineDialogSharedViewModel newInstance() {
    return new OpenZipLineDialogSharedViewModel();
  }

  private static final class InstanceHolder {
    private static final OpenZipLineDialogSharedViewModel_Factory INSTANCE = new OpenZipLineDialogSharedViewModel_Factory();
  }
}
