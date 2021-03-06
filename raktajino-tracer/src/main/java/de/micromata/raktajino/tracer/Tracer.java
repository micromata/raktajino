package de.micromata.raktajino.tracer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class Tracer implements TestExecutionListener {

  private final Path root;

  public Tracer() throws Exception {
    this.root = Files.createTempDirectory("junit-tracer-");
  }

  @Override
  public void testPlanExecutionStarted(TestPlan testPlan) {
    try {
      long tests = testPlan.countTestIdentifiers(TestIdentifier::isTest);
      Files.write(root.resolve("test.plan.execution.begin.txt"), (tests + " tests").getBytes());
    } catch (IOException e) {
      // TODO Handle IOException.
    }
  }

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    try {
      long tests = testPlan.countTestIdentifiers(TestIdentifier::isTest);
      Files.write(root.resolve("test.plan.execution.end.txt"), (tests + " tests").getBytes());
      System.out.printf("raktajino-tracer (%d): %s%n", tests, root.toUri());
    } catch (IOException e) {
      // TODO Handle IOException.
    }
  }

  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
    try {
      Path path = root.resolve(path(testIdentifier.getUniqueId()));
      Files.createDirectories(path);
      Files.write(path.resolve("test.execution.begin.txt"), testIdentifier.toString().getBytes());
    } catch (IOException e) {
      // TODO Handle IOException.
    }
  }

  @Override
  public void dynamicTestRegistered(TestIdentifier testIdentifier) {
    Path path = root.resolve(path(testIdentifier.getUniqueId()));
    try {
      Files.createDirectories(path);
      Files.write(path.resolve("test.execution.begin.txt"), testIdentifier.toString().getBytes());
    } catch (IOException e) {
      // TODO Handle IOException.
    }
  }

  @Override
  public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult result) {
    Path path = root.resolve(path(testIdentifier.getUniqueId()));
    if (Files.notExists(path)) {
      throw new IllegalStateException("Expected path to exist: " + path);
    }
    TestExecutionResult.Status status = result.getStatus();
    try {
      Files.write(path.resolve("test.execution.end.txt"), result.toString().getBytes());
      Files.write(path.resolve("test.status." + status + ".txt"), status.toString().getBytes());
    } catch (IOException e) {
      // TODO Handle IOException.
    }
  }

  @Override
  public void executionSkipped(TestIdentifier testIdentifier, String reason) {
    Path path = root.resolve(path(testIdentifier.getUniqueId()));
    try {
      Files.createDirectories(path);
      Files.write(path.resolve("test.execution.skipped.txt"), reason.getBytes());
    } catch (IOException e) {
      // TODO Handle IOException.
    }
  }

  // https://stackoverflow.com/a/1184263/1431016
  static Path path(String string) {
    char escape = '%'; // ... or some other legal char.
    int len = string.length();
    StringBuilder builder = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      char ch = string.charAt(i);
      if (ch == ':') { // `:` is illegal in java.nio.file.Path
        builder.append('~');
        continue;
      }
      if (ch < ' '
          || ch >= 0x7F // || ch == fileSep || ... // add other illegal chars
          || (ch == '.' && i == 0) // we don't want to collide with "." or ".."!
          || ch == escape) {
        builder.append(escape);
        if (ch < 0x10) {
          builder.append('0');
        }
        builder.append(Integer.toHexString(ch));
        continue;
      }
      builder.append(ch);
    }
    return Paths.get(builder.toString());
  }
}
