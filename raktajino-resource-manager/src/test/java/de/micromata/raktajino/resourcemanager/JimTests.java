package de.micromata.raktajino.resourcemanager;

import de.micromata.raktajino.resourcemanager.ResourceManager.New;
import de.micromata.raktajino.resourcemanager.ResourceManager.Singleton;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResourceManager.class)
class JimTests {

  private final Path global;
  private final Path local;

  public JimTests(@Singleton(JimFS.class) Path global, @New(JimFS.class) Path local) {
    this.global = global;
    this.local = local;
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("*** c'tor()");
    System.out.println("GLOBAL     = " + global.toUri());
    System.out.println("this.local = " + local.toUri());
  }

  @Test
  void one(@New(JimFS.class) Path one) {
    System.out.println();
    System.out.println("**** one() = " + one.toUri());
    System.out.println("GLOBAL     = " + global.toUri());
    System.out.println("this.local = " + local.toUri());
  }
}
