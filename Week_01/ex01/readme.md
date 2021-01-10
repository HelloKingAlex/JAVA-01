# 字节码分析
## 准备工作

将src下的文件编译，并将编译后的class放在target文件夹下
```bash
javac -g -d ./target ./src/*.java
```

以LocalVariableTest的字节码举例
```bash
javap -c -v ./target/LocalVariableTest
```

针对main方法的分析
![](./resource/images/2021-01-10-12-10-37.png)

针对first以及second赋值的分析
![](./resource/images/2021-01-10-14-41-14.png)

针对movingAverage.sumbit(first)的分析
![](./resource/images/2021-01-10-15-09-02.png)