[![Release](https://jitpack.io/v/dilbertside/BOD.svg)](https://jitpack.io/#dilbertside/BOD)
[![Build Status](https://travis-ci.org/dilbertside/BOD.svg)](https://travis-ci.org/dilbertside/BOD)

# Business Object Document

In order to achieve inter-operability between disparate systems, disparate companies and disparate supply chains, there must be a common horizontal message architecture that provides a common understanding for all.

It is based on the OAGIS specification V9 http://www.oagi.org/oagis/9.0/Documentation/Architecture.html

Initially it is based on the XML schema definition.

This repository is a tentative to specify the BOD in a JSON like representation.

## Maven

To use it in your Maven build add:

### Repository

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

### Dependency:


```xml
  <dependency>
    <groupId>com.github.dilbertside</groupId>
    <artifactId>BOD</artifactId>
    <version>0.2.4</version>
  </dependency>
```

## Gradle

To use it in your Gradle project add:

### Repository

```
repositories {
    mavenLocal()
    mavenCentral()
    maven { url "https://jitpack.io" }
}
```

### Dependency:


```
  implementation group: 'com.github.dilbertside', name: 'BOD', version: '0.2.4'
```

