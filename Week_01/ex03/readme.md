# Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的关系

1. -Xmx 最大堆内存  
2. -Xms 初始堆内存。通常可以设定为与-Xmx相同，以避免每次垃圾回收完成之后JVM重新分配内存。  
3. -Xmn 设置年轻代内存。堆大小=年轻代大小 + 老年代大小 + 持久代。 jdk8以后持久代变为metaspace，属于非堆内存。
4. -Xss 设置每个线程的栈空间大小，JDK5以后默认为1M。
5. -XX:MetaspaceSize 元空间大小，元空间并不存放在VM中，而是使用本机内存，所以这块区域的大小受本地内存限制。这个参数的意义在于首次使用不够时而触发FGC的阈值，不设置的话，默认为20M左右
6. -XX:MaxMetaspaceSize 元空间最大内存
7. -XX:MaxDirectMemorySize 堆外内存，默认64M，设置了-Xmx时，堆外内存的值和堆内存的值一样，堆外内存到达最大值时，也会触发FGC