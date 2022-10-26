package com.codersergg.lock;

/**
 * Классы, которые реализуют этот интерфейс, могут быть использаваны для получения lock-объекта,
 * используя интерфейс com.codersergg.lock.LockService. Стоит самостоятельно позаботиться о
 * реализации методов boolean equals(Object o), int hashCode() в имплементирующих интерфейс классах,
 * исключая использавание часто изменяющих свое зачение в процессе работы приложения полей, таких
 * как int gold, int progress и т.д.
 */
public interface Lockable {

  boolean equals(Object o);

  int hashCode();
}
