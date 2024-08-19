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
public final class OpenZipListDialogSharedViewModel_Factory implements Factory<OpenZipListDialogSharedViewModel> {
  @Override
  public OpenZipListDialogSharedViewModel get() {
    return newInstance();
  }

  public static OpenZipListDialogSharedViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static OpenZipListDialogSharedViewModel newInstance() {
    return new OpenZipListDialogSharedViewModel();
  }

  private static final class InstanceHolder {
    private static final OpenZipListDialogSharedViewModel_Factory INSTANCE = new OpenZipListDialogSharedViewModel_Factory();
  }
}
