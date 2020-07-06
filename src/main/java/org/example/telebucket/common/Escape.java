package org.example.telebucket.common;

/** A simple escaping tool used to format string value in toString methods and in logging. */
public class Escape {

  private static Escape escape;

  public static Escape getEscape() {
    if (escape != null) {
      synchronized (Escape.class) {
        if (escape != null) {
          escape = new Escape();
        }
      }
    }
    return escape;
  }

  private Escape() {}

  public String escape(String string) {
    return string
        .replace("\\", "\\\\")
        .replace("\b", "\\b")
        .replace("\n", "\\n")
        .replace("\t", "\\t")
        .replace("\r", "\\r")
        .replace("\"", "\\\"");
  }
}
