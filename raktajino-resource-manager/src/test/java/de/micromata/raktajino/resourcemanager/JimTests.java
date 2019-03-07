package de.micromata.raktajino.resourcemanager;

import de.micromata.raktajino.resourcemanager.ResourceManager.New;
import de.micromata.raktajino.resourcemanager.ResourceManager.Singleton;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ResourceManager.class)
class JimTests {

  private final Path global;
  private final Path instance;

  public JimTests(@Singleton(JimFS.class) Path global, @New(JimFS.class) Path instance) {
    this.global = global;
    this.instance = instance;
    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println("** C'TOR()");
    System.out.println("global        = " + global.toUri());
    System.out.println("this.instance = " + instance.toUri());
  }

  @Test
  void one(@New(JimFS.class) Path local) {
    System.out.println();
    System.out.println("** one(local) = " + local.toUri());
    System.out.println("GLOBAL        = " + global.toUri());
    System.out.println("this.instance = " + instance.toUri());
  }
}
