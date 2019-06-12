package de.micromata.raktajino.all;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import de.micromata.raktajino.resourcemanager.ResourceManager;
import de.micromata.raktajino.resourcemanager.ResourceSupplier;
import de.micromata.raktajino.resourcemanager.Temp;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.testkit.engine.EngineTestKit;

class ResourceManagerIntegrationTests {

  @Test
  void verifyDiceTests() {
    EngineTestKit.engine("junit-jupiter")
        .configurationParameter(
            "junit.jupiter.conditions.deactivate", "org.junit.*DisabledCondition")
        .selectors(selectClass(DiceTests.class))
        .execute()
        .tests()
        .assertStatistics(stats -> stats.started(5).succeeded(5).aborted(0).failed(0));
  }

  @Test
  void verifyTempTests() {
    EngineTestKit.engine("junit-jupiter")
        .configurationParameter(
            "junit.jupiter.conditions.deactivate", "org.junit.*DisabledCondition")
        .selectors(selectClass(TempTests.class))
        .execute()
        .tests()
        .assertStatistics(stats -> stats.started(1).succeeded(1).aborted(0).failed(0));
  }

  static class Dice implements ResourceSupplier<Integer> {

    private static final int value = ThreadLocalRandom.current().nextInt(1, 20);

    @Override
    public Integer get() {
      return value;
    }
  }

  @ExtendWith(ResourceManager.class)
  @Disabled
  static class DiceTests {

    @RepeatedTest(5)
    void success(@ResourceManager.Shared(type = Dice.class, name = "D20") int value) {
      assertTrue(0 < value && value <= 20);
    }
  }

  @ExtendWith(ResourceManager.class)
  @Disabled
  static class TempTests {
    @Test
    void one(@Temp Path temp, TestReporter reporter) {
      reporter.publishEntry("temp", temp.toUri().toString());
    }
  }
}
