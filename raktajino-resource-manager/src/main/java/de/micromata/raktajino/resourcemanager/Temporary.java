package de.micromata.raktajino.resourcemanager;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ParameterResolutionException;

/** Supplies an empty temporary directory that is deleted on close. */
public class Temporary implements ResourceSupplier<Path> {

  private final Path path;

  /**
   * Create an empty temporary directory.
   *
   * @throws ParameterResolutionException if anything goes wrong
   */
  public Temporary() {
    try {
      this.path = createTempDirectory();
      createdTempDirectory(path);
    } catch (Exception e) {
      throw new ParameterResolutionException("Creating temporary directory failed", e);
    }
  }

  /** Create an empty temporary directory. */
  protected Path createTempDirectory() throws Exception {
    return Files.createTempDirectory("Temporary-");
  }

  /** Override this method if you want to perform tasks on the newly created directory. */
  protected void createdTempDirectory(Path path) {
    assert path != null;
  }

  @Override
  public final Path get() {
    return path;
  }

  @Override
  public void close() {
    // trivial case: already "closed" - be idempotent!
    if (Files.notExists(path)) {
      return;
    }
    try {
      // simple case: delete empty directory right away
      try {
        Files.delete(path);
        return;
      } catch (DirectoryNotEmptyException ignored) {
        // fall-through
      }
      // default case: walk the tree...
      try (Stream<Path> stream = Files.walk(path)) {
        Stream<Path> selected = stream.sorted((p, q) -> -p.compareTo(q));
        for (Path path : selected.collect(Collectors.toList())) {
          Files.deleteIfExists(path);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("deleting temporary path failed: " + path, e);
    }
  }
}
