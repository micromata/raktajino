package de.micromata.raktajino.resourcemanager;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TemporaryTests {
  @Test
  void constructorCreatesEmptyDirectory() throws Exception {
    try (Temporary temporary = new Temporary()) {
      Path path = temporary.get();
      assertTrue(Files.isDirectory(path));
      Files.delete(path);
    }
  }
}
