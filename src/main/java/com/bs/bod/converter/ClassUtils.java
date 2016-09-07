/**
 * ClassUtils
 */
package com.bs.bod.converter;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * subclass of org.springframework.util.ClassUtils
 * 
 * @author dbs on Dec 30, 2015 5:33:22 PM
 *
 * @version 1.0
 * @since 0.0.3
 * 
 */
public class ClassUtils {

  /** Suffix for array class names: "[]" */
  public static final String ARRAY_SUFFIX = "[]";

  /** Prefix for internal array class names: "[" */
  private static final String INTERNAL_ARRAY_PREFIX = "[";

  /** Prefix for internal non-primitive array class names: "[L" */
  private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

  /** The package separator character '.' */
  private static final char PACKAGE_SEPARATOR = '.';

  /** The path separator character '/' */
  private static final char PATH_SEPARATOR = '/';

  /** The inner class separator character '$' */
  private static final char INNER_CLASS_SEPARATOR = '$';

  /** The CGLIB class separator character "$$" */
  public static final String CGLIB_CLASS_SEPARATOR = "$$";

  /** The ".class" file suffix */
  public static final String CLASS_FILE_SUFFIX = ".class";
  /**
   * Map with common "java.lang" class name as key and corresponding Class as value. Primarily for efficient deserialization of remote invocations.
   */
  private static final Map<String, Class<?>> commonClassCache = new HashMap<String, Class<?>>(32);

  /**
   * Map with primitive type name as key and corresponding primitive type as value, for example: "int" -> "int.class".
   */
  private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(32);

  /**
   * Replacement for {@code Class.forName()} that also returns Class instances for primitives (e.g. "int") and array class names (e.g. "String[]"). Furthermore,
   * it is also capable of resolving inner class names in Java source style (e.g. "java.lang.Thread.State" instead of "java.lang.Thread$State").
   * 
   * @param name
   *          the name of the Class
   * @param classLoader
   *          the class loader to use (may be {@code null}, which indicates the default class loader)
   * @return Class instance for the supplied name
   * @throws ClassNotFoundException
   *           if the class was not found
   * @throws LinkageError
   *           if the class file could not be loaded
   * @see Class#forName(String, boolean, ClassLoader)
   */
  public static Class<?> forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
    // Assert.notNull(name, "Name must not be null");

    Class<?> clazz = resolvePrimitiveClassName(name);
    if (clazz == null) {
      clazz = commonClassCache.get(name);
    }
    if (clazz != null) {
      return clazz;
    }

    // "java.lang.String[]" style arrays
    if (name.endsWith(ARRAY_SUFFIX)) {
      String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
      Class<?> elementClass = forName(elementClassName, classLoader);
      return Array.newInstance(elementClass, 0).getClass();
    }

    // "[Ljava.lang.String;" style arrays
    if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
      String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
      Class<?> elementClass = forName(elementName, classLoader);
      return Array.newInstance(elementClass, 0).getClass();
    }

    // "[[I" or "[[Ljava.lang.String;" style arrays
    if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
      String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
      Class<?> elementClass = forName(elementName, classLoader);
      return Array.newInstance(elementClass, 0).getClass();
    }

    ClassLoader clToUse = classLoader;
    if (clToUse == null) {
      clToUse = getDefaultClassLoader();
    }
    try {
      return (clToUse != null ? clToUse.loadClass(name) : Class.forName(name));
    } catch (ClassNotFoundException ex) {
      int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
      if (lastDotIndex != -1) {
        String innerClassName = name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
        try {
          return (clToUse != null ? clToUse.loadClass(innerClassName) : Class.forName(innerClassName));
        } catch (ClassNotFoundException ex2) {
          // Swallow - let original exception get through
        }
      }
      throw ex;
    }
  }

  /**
   * Resolve the given class name as primitive class, if appropriate, according to the JVM's naming rules for primitive classes.
   * <p>
   * Also supports the JVM's internal class names for primitive arrays. Does <i>not</i> support the "[]" suffix notation for primitive arrays; this is only
   * supported by {@link #forName(String, ClassLoader)}.
   * 
   * @param name
   *          the name of the potentially primitive class
   * @return the primitive class, or {@code null} if the name does not denote a primitive class or primitive array class
   */
  public static Class<?> resolvePrimitiveClassName(String name) {
    Class<?> result = null;
    // Most class names will be quite long, considering that they
    // SHOULD sit in a package, so a length check is worthwhile.
    if (name != null && name.length() <= 8) {
      // Could be a primitive - likely.
      result = primitiveTypeNameMap.get(name);
    }
    return result;
  }

  /**
   * Return the default ClassLoader to use: typically the thread context ClassLoader, if available; the ClassLoader that loaded the ClassUtils class will be
   * used as fallback.
   * <p>
   * Call this method if you intend to use the thread context ClassLoader in a scenario where you clearly prefer a non-null ClassLoader reference: for example,
   * for class path resource loading (but not necessarily for {@code Class.forName}, which accepts a {@code null} ClassLoader reference as well).
   * 
   * @return the default ClassLoader (only {@code null} if even the system ClassLoader isn't accessible)
   * @see Thread#getContextClassLoader()
   * @see ClassLoader#getSystemClassLoader()
   */
  public static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ex) {
      // Cannot access thread context ClassLoader - falling back...
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = ClassUtils.class.getClassLoader();
      if (cl == null) {
        // getClassLoader() returning null indicates the bootstrap ClassLoader
        try {
          cl = ClassLoader.getSystemClassLoader();
        } catch (Throwable ex) {
          // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
        }
      }
    }
    return cl;
  }

  /**
   * Determine whether the {@link Class} identified by the supplied name is present and can be loaded. Will return {@code false} if either the class or one of
   * its dependencies is not present or cannot be loaded.
   * 
   * @param className
   *          the name of the class to check
   * @param classLoader
   *          the class loader to use (may be {@code null}, which indicates the default class loader)
   * @return whether the specified class is present
   */
  public static boolean isPresent(String className, ClassLoader classLoader) {
    try {
      forName(className, classLoader);
      return true;
    } catch (Throwable ex) {
      // Class or one of its dependencies is not present...
      return false;
    }
  }

  /**
   * Return the user-defined class for the given instance: usually simply the class of the given instance, but the original class in case of a CGLIB-generated
   * subclass.
   * 
   * @param instance
   *          the instance to check
   * @return the user-defined class
   */
  public static Class<?> getUserClass(Object instance) {
    // Assert.notNull(instance, "Instance must not be null");
    return getUserClass(instance.getClass());
  }

  /**
   * Return the user-defined class for the given class: usually simply the given class, but the original class in case of a CGLIB-generated subclass.
   * 
   * @param clazz
   *          the class to check
   * @return the user-defined class
   */
  public static Class<?> getUserClass(Class<?> clazz) {
    if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
      Class<?> superclass = clazz.getSuperclass();
      if (superclass != null && Object.class != superclass) {
        return superclass;
      }
    }
    return clazz;
  }
}
