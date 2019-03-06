package de.micromata.raktajino.resourcemanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceManagerTests {
  @Test
  void instantiatable() {
    Assertions.assertNotNull(new ResourceManager().toString());
  }
}
