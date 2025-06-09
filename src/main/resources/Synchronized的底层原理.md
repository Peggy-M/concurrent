## Synchronized的底层原理

对 Synchronized 而言，其本身的锁与 Java 语言层面的 AQS，CAS 锁存在一定的差异，Synchronized 的锁机制是由 JVM 实现的，JVM 通过 **Monitor** 对象来实现对于方法以及代码块的同步，所以要研究 Synchronized 的底层实现原理，就需要去研究一下虚拟机内部是如何实现这套锁机制的，这里我们就是用 JDK 官方默认的一套 JVM 实现 HotSpot 作为其研究的对象，当然 JVM 的实现不只是只有这一种（比如有 **OpenJ9**、**GraalVM**），这里使用这一种只是因为 HotSpot 是官方默认并且使用率最高的一套实现。

在研究 HotSpot 关于 syncharonized 的具体实现之前，这里需要抛出一个疑问，有没有好奇，既然对于任何的锁来说，都是需要获取到锁资源，比如对于 AQS 与 CAS 我们有lock锁对象，通过 .lock 与 .unlock 就可完成对锁资源的获取与释放。那对于 synchronized 这种特殊的未声明任何的锁对象语法格式，其锁对象是在哪里呢？对于 synchronized  而言其锁对象是本身是一个监视器对象，也就是上面提到过的 **Monitor** 对象当然也有其将它称为 **Monitor（管程）** 。有意思的我们好像并能显式地看到这个对象，其实这就是接下来要引入的概念- **对象头** 。

### 对象头是从何而言，到何而去？

我们知道，对于一个对象创建之后无论你是使用何种方法，比如 new 、序列化、反射。这些创建的对象都是最终在绝大都数情况下都是被存储在了 JVM 内存空间当中的堆空间中，当然这个不包含已经发生内存逃逸的对象。

虽然堆已经是 JVM 内部的一块内存空间，但是为了更好的管理同意，堆的空间内存存储空间又被划分成了 对象头(Header)、实例数据(InstanceData)和对其填充(Padding),这三部分。

而这第一部分就是本篇关于 syncharonized 的重点，也就是存储 Montior 监视器对象的位置-对象头。这部分存储了对象在运行过程当中的自身运行是数据、比如哈希码、CG分代年龄、锁状态标志、线程持有的锁、偏向锁ID、偏向时间戳等。其头的大小是和虚拟机的架构位(这个架构位数大多数是源于 CPU 的寻址能力，就是一次可以处理数据的最大宽度)数有关，比如 32位与64位分别对应的就是 32 比特和 64比特，当然官方对于对象头有用另一种常用的叫法，就做 **Mark Word** 。

关于 **Mark Word ** 头信息当中存储的数据标志位可以参考下面这张表![image-20250606224536287](.\assets\image-20250606224536287.png) 																							

![image-20250606225051394](.\assets\image-20250606225051394.png) 

![image-20250606225122250](.\assets\image-20250606225122250.png) 

### 锁的升级以及对比

在 1.6 版本之前是没有锁升级这种概念的，在次之前一直都是以重量级锁作为 syncharoniozed 的锁机制的，后面是为了性能的优化和提升才引入了“偏向锁”以及“轻量锁” 这种概念和机制。随后锁便有了四种状态 **无锁状态、偏向锁状态、轻量级锁、重量级锁**，在这个过程当中锁除了偏向锁可以降级到无锁状态，其他的只能只升不降，其本身是为了提高获取锁和释放锁的效率。而触发降级的条件是当偏向锁的持有线程不再活跃（如线程终止），或发生竞争时，JVM 会撤销偏向锁，退回到 **无锁状态**。

| 二进制值（低3位） | 锁状态           | 升级方向                                       | 是否支持降级                   |
| ----------------- | :--------------- | :--------------------------------------------- | :----------------------------- |
| `001`             | **无锁**         | → 偏向锁 / 轻量级锁                            | -                              |
| `101`             | 锁状态**偏向锁** | 描述→ 轻量级锁                                 | **可以降级到无锁**（撤销偏向） |
| `000`             | **轻量级锁**     | 对象未被锁定，存储哈希码或分代年龄。→ 重量级锁 | **极少降级**（依赖 JVM 优化）  |
| `010`             | **重量级锁**     | 无进一步升级                                   | **不支持降级**                 |

![image-20250606231014706](.\assets\image-20250606231014706.png) 

![image-20250606231108252](./assets/image-20250606231108252.png) 

从上面的 HotSpot 下 `biasedLocking.cpp` 文件当中 revoke_bias 方法下就可以看到具体的锁降级流程。

> 下面的运行案例可参考 UpgradeTest 测试类下的 upgrade 测试方法

~~~ bash
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

main:java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 20 2f 92 (00000101 00100000 00101111 10010010) (-1842405371)
      4     4        (object header)                           c1 01 00 00 (11000001 00000001 00000000 00000000) (449)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

t1:java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           7a 98 0d b6 (01111010 10011000 00001101 10110110) (-1240622982)
      4     4        (object header)                           c1 01 00 00 (11000001 00000001 00000000 00000000) (449)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
~~~



#### 偏向锁

在 HotSpot 的作者研究发现，在绝大都数的情况下，锁不仅仅存在多线程的竞争关系，而且还存在总是有一个线程多次重复的获取，为了使线程获取锁的代价更底，从而引入了偏向锁，实现原理就是在 Mark Word 对象头信息，以及栈帧也就是同步方法的方法栈当中存储了偏向锁的线程  ID ，以后该线程在进入和退出同步方法的失败就不需要进行 CAS 操作来完成加锁与解锁了。当一个线程进来，先检查一下 Mark Word 当中是否存储着指向当前线程的偏向锁，如果获取锁成功，如果没有就检查一下 Mark Word 偏向锁的标识是否设置为了1 ，如果没有就直接通过 CAS 竞争锁资源，如果设置了则就需要通过 CAS 尝试将当前的偏向锁指向当前的线程。

