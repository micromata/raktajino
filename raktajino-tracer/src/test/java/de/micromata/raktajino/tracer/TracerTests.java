package de.micromata.raktajino.tracer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class TracerTests {
  @Test
  void defaultConstructorWithDefaultProperties() {
    Tracer tracer = new Tracer();
    assertNotNull(tracer.toString());
  }
}
