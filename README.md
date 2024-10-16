To implement Latta to your Java desktop / server application:

1. Add Latta dependency to your pom.xml file

```
<dependency>
    <groupId>ai.latta</groupId>
    <artifactId>latta-java-recorder</artifactId>
    <version>1.0</version>
</dependency>
```

2. Wrap whole application into Latta recorder

```java
LattaRecorder.recordApplication("YOUR API KEY", () -> {
});
```