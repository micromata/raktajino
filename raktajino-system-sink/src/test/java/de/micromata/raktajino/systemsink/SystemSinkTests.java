package de.micromata.raktajino.systemsink;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

class SystemSinkTests {
  @Test
  @ResourceLock(Resources.SYSTEM_OUT)
  @ResourceLock(Resources.SYSTEM_ERR)
  void streams() {
    Object out = System.out;
    Object err = System.err;
    assertSame(out, System.out);
    assertSame(err, System.err);
    try (SystemSink.Streams streams = new SystemSink.Streams()) {
      assertNotSame(out, System.out);
      assertNotSame(err, System.err);
      System.out.println("foo");
      System.out.println("bar");
      assertLinesMatch(Arrays.asList("foo", "bar"), streams.outLines());
    }
    assertSame(out, System.out);
    assertSame(err, System.err);
  }
}
