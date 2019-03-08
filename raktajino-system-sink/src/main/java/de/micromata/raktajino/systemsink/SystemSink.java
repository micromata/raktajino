package de.micromata.raktajino.systemsink;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

@Target(METHOD)
@Retention(RUNTIME)
@ResourceLock(Resources.SYSTEM_OUT)
@ResourceLock(Resources.SYSTEM_ERR)
@ExtendWith(SystemSink.Extension.class)
public @interface SystemSink {

  class Extension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext context) {
      return parameterContext.getParameter().getType() == Streams.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
      Store store = context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
      return store.getOrComputeIfAbsent(Streams.class, key -> new Streams());
    }
  }

  class Streams implements AutoCloseable, Store.CloseableResource {

    private final PrintStream standardOut, standardErr;
    private final ByteArrayOutputStream out, err;

    Streams() {
      this.standardOut = System.out;
      this.standardErr = System.err;
      this.out = new ByteArrayOutputStream();
      this.err = new ByteArrayOutputStream();
      System.setOut(new PrintStream(out));
      System.setErr(new PrintStream(err));
    }

    @Override
    public void close() {
      System.setOut(standardOut);
      System.setErr(standardErr);
    }

    List<String> outLines() {
      // 11 return out.toString().lines().collect(Collectors.toList());
      return Arrays.asList(out.toString().split("\\R"));
    }

    List<String> errLines() {
      // 11 return err.toString().lines().collect(Collectors.toList());
      return Arrays.asList(err.toString().split("\\R"));
    }

    @Override
    public String toString() {
      return "out=```" + out.toString() + "```, err=```" + err.toString() + "```";
    }
  }
}
