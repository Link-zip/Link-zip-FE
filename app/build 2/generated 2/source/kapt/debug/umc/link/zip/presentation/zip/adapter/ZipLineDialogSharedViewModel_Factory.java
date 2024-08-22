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
public final class ZipLineDialogSharedViewModel_Factory implements Factory<ZipLineDialogSharedViewModel> {
  @Override
  public ZipLineDialogSharedViewModel get() {
    return newInstance();
  }

  public static ZipLineDialogSharedViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ZipLineDialogSharedViewModel newInstance() {
    return new ZipLineDialogSharedViewModel();
  }

  private static final class InstanceHolder {
    private static final ZipLineDialogSharedViewModel_Factory INSTANCE = new ZipLineDialogSharedViewModel_Factory();
  }
}
