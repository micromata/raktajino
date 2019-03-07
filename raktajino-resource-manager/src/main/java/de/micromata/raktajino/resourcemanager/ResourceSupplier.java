package de.micromata.raktajino.resourcemanager;

import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

/** Implement this interface to create your resource. */
public interface ResourceSupplier<R> extends AutoCloseable, CloseableResource, Supplier<R> {

  /** Return resource as the required parameter type. */
  default Object as(Class<?> parameterType) {
    // TODO find unique converter, like String Object#toString() or File Path#toFile()?
    throw new UnsupportedOperationException("Can't convert to " + parameterType);
  }

  /** Close the managed resource. */
  @Override
  default void close() {
    R instance = get();
    if (instance instanceof AutoCloseable) {
      try {
        ((AutoCloseable) instance).close();
      } catch (Exception e) {
        // TODO better exception handling by reporting or re-throwing?
        throw new RuntimeException("closing failed: " + instance, e);
      }
    }
  }
}
