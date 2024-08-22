package umc.link.zip.presentation.home.alert;

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
public final class AlertViewModel_Factory implements Factory<AlertViewModel> {
  @Override
  public AlertViewModel get() {
    return newInstance();
  }

  public static AlertViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AlertViewModel newInstance() {
    return new AlertViewModel();
  }

  private static final class InstanceHolder {
    private static final AlertViewModel_Factory INSTANCE = new AlertViewModel_Factory();
  }
}
