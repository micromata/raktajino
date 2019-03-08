package de.micromata.raktajino.all;

import de.micromata.raktajino.resourcemanager.ResourceManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IntegrationTests {
  @Test
  void newResourceManager() {
    Assertions.assertNotNull(new ResourceManager().toString());
  }
}
