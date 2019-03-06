package de.micromata.raktajino.systemsink;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SystemSinkTests {
  @Test
  void instantiatable() {
    Assertions.assertNotNull(new SystemSink().toString());
  }
}
