package de.micromata.raktajino.resourcemanager;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.nio.file.FileSystem;
import java.nio.file.Path;

class JimFS implements ResourceSupplier<Path> {

  private final FileSystem jim = Jimfs.newFileSystem(Configuration.unix());

  @Override
  public Path get() {
    return jim.getPath("/");
  }

  @Override
  public void close() {
    try {
      jim.close();
    } catch (Exception e) {
      throw new RuntimeException("He's dead, Jim.", e);
    }
  }
}
