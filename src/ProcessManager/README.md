# 进程管理模块设计


进程调度算法也称 CPU 调度算法，毕竟进程是由 CPU 调度的。  
当 CPU 空闲时，操作系统就选择内存中的某个「就绪状态」的进程，并给其分配 CPU。
常见的调度算法：

> 先来先服务调度算法  
最短作业优先调度算法  
高响应比优先调度算法  
时间片轮转调度算法  
最高优先级调度算法  
多级反馈队列调度算法


## 建立模型


- 首先，设计一个 PCB 类表示进程，包括以下属性

```java
public static int period = 500;  
private final int id;  // 进程ID  
private final String name;  // 进程名称  
private int priority;  // 进程优先级  
private final int arrivalTime;  // 进程到达时间  
private int burstTime;  // 进程需要的时间片  
private int remainingTime;  // 进程剩余的时间片  
private int waitingTime;  // 进程等待时间  
private ProcessStatus status;  // 进程状态  
private double responseRatio; ////进程响应比
```

- 设计一个调度器抽象类 Schedule, 实现 Runnable 接口实现并发, 各种调度算法实现 schedule 方法即可

```java
 public abstract class Scheduler implements Runnable{
    // 要调度的进程队列 
    public final Vector<PCB> readyQueue;  
  
    public Scheduler(Vector<PCB> pcbList) {  
        //构造函数
        ...
    }  
  
    // @description TODO 运行一个进程，运行结束将进程从就绪队列删除  

     public void PCB_start(PCB pcb){  
        // 启动进程  
        ...
    }  
     // @description TODO 调度器  
     abstract void schedule();  
  
     // @description TODO 插入进程  
     abstract void insertProcess(PCB pcb);  
  
}
```
- `Vector<PCB>readyQueue` ：因为要使用定时器实现时间片, 高优先级的进程抢夺 cpu 资源等，需要采用并发编程，采用线程安全的 Vector
- `public void PCB_start(PCB pcb)` ：进程启动，等待进程执行结束或被其它进程抢夺

- 进程调度类之间的继承关系

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/Scheduler.png?token=ATVNUUALI63MFMA2S5WAAZTDVWKNK)


## 先来先服务调度算法


![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229230053.png?token=ATVNUUCIODSOY4Y3S7WXFP3DVWV6E)



最简单的一个调度算法，就是非抢占式的**先来先服务（_First Come First Severd, FCFS_）算法**了。

**每次从就绪队列选择最先进入队列的进程，然后一直运行，直到进程退出或被阻塞，才会继续从队列中选择第一个进程接着运行。**

这似乎很公平，但是当一个长作业先运行了，那么后面的短作业等待的时间就会很长，不利于短作业。

FCFS 对长作业有利，适用于 CPU 繁忙型作业的系统，而不适用于 I/O 繁忙型作业的系统。

```java
@Override 
public void schedule() {  
    int currentTime = 0;  
  
    // 遍历就绪队列中的所有进程  
    while(!readyQueue.isEmpty()){  
        PCB pcb = readyQueue.get(0);  
        PCB_start(pcb);  
        currentTime += pcb.getBurstTime() - pcb.getRemainingTime();  
        System.out.println("当前时间：" + currentTime);  
    }  
  
    System.out.println("先来先服务调度算法演示结束");  
    System.out.println("-------------------------------------");  
}

@Override  
public void insertProcess(PCB pcb) {  
    //直接插入到末尾  
    readyQueue.add(pcb);  
}
```

**实现**：遍历进程队列，依次执行即可，新进程直接进入队列末尾


## 最短作业优先调度算法

**最短作业优先（_Shortest Job First, SJF_）调度算法**，它会**优先选择运行时间最短的进程来运行**。

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229230145.png?token=ATVNUUGHOOUQ6QWM5Z2DEYDDVWWBO)



```java
@Override  
public void schedule() {  
    // 定义当前时间  
    int currentTime = 0;  
  
    // 循环调度就绪队列中的进程  
    while (!readyQueue.isEmpty()) {  
        // 找到周期最短的进程  
        PCB pcb = readyQueue.get(0);  
  
        // 启动进程  
        PCB_start(pcb);  
  
        // 更新当前时间  
        currentTime += pcb.getBurstTime() - pcb.getRemainingTime();  
        System.out.println("当前时间：" + currentTime);  
    }  
}
```

**实现**：对进程队列按运行时间按从小到大排序，依次调用进程中的序列，新进程插入队列需要使用插入排序，保持队列的顺序


## 高响应比优先调度算法

**高响应比优先 （_Highest Response Ratio Next, HRRN_）调度算法**主要是权衡了短作业和长作业。

**每次进行进程调度时，先计算「响应比优先级」，然后把「响应比优先级」最高的进程投入运行**，「响应比优先级」的计算公式：

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229230220.png?token=ATVNUUH32NXGMRX4YASBTW3DVWWDU)


- 如果两个进程的「<font color="#ff0000">等待时间</font>」相同时，「<font color="#ff0000">要求的服务时间</font>」越短，「<font color="#ff0000">响应比</font>」就越高，这样短作业的进程容易被选中运行；
- 如果两个进程「<font color="#ff0000">要求的服务时间</font>」相同时，「<font color="#ff0000">等待时间</font>」越长，「<font color="#ff0000">响应比</font>」就越高，这就兼顾到了长作业进程，因为进程的响应比可以随时间等待的增加而提高，当其等待时间足够长时，其响应比便可以升到很高，从而获得运行的机会；

```java
@Override  
public void schedule() {  
    // 定义当前时间  
    int currentTime = 0;  
  
    // 循环调度就绪队列中的进程  
    while (!readyQueue.isEmpty()) {  
        // 找到响应比最高的进程  
        readyQueue.sort((p1, p2) -> {  
            if(p2.getResponseRatio() - p1.getResponseRatio() > 0){  
                return 1;  
            }  
            return -1;  
        });  
        for (PCB pcb : readyQueue) {  
            System.out.print(pcb.getName() + "响应比：" + pcb.getResponseRatio() +"\t");  
        }  
        PCB maxPcb = readyQueue.get(0);  
  
        PCB_start(maxPcb);  
  
        // 更新当前时间  
        currentTime += maxPcb.getPriority();  
        System.out.println("当前时间：" + currentTime);  
    }  
  
    System.out.println("高响应比优先算法演示结束");  
    System.out.println("-------------------------------------");  
}
```

**实现**：与短作业优先类似，区别只是排序时依据的属性不同而已


## 时间片轮转调度算法

**每个进程被分配一个时间段，称为时间片（_Quantum_），即允许该进程在该时间段中运行。**

-   如果时间片用完，进程还在运行，那么将会把此进程从 CPU 释放出来，并把 CPU 分配另外一个进程；
-   如果该进程在时间片结束前阻塞或结束，则 CPU 立即进行切换；

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229230259.png?token=ATVNUUHDCFHM35GBKFUFR2LDVWWGC)


```java
@Override  
public void schedule() {  
    // 定义当前时间  
    int currentTime = 0;  
    interrupt.schedule(new TimerTask() {  
        @Override  
        public void run() {  
            if(!readyQueue.isEmpty()){  
                //终止正在运行的进程并把它添加到就绪队列的末尾  
                PCB pcb = readyQueue.get(0);  
                pcb.setStatus(PCB.ProcessStatus.READY);  
                readyQueue.remove(0);  
                readyQueue.add(pcb);  
            }  
  
        }  
    },0, timeSlice* 500L);  
    // 循环调度就绪队列中的进程  
    while (!readyQueue.isEmpty()) {  
        // 取出就绪队列中的第一个进程  
        PCB pcb = readyQueue.get(0);  
  
        //开启进程  
        PCB_start(pcb);  
  
        currentTime += pcb.getBurstTime() - pcb.getRemainingTime();  
        System.out.println("当前时间：" + currentTime);  
    }  
    System.out.println("时间片轮换调度算法演示结束");  
    System.out.println("-------------------------------------");  
}
```

**实现**：设置一个定时器，每隔一段时间把正在运行的程序转为就绪状态，并放到队列的末尾


## 最高优先级调度算法

**从就绪队列中选择最高优先级的进程进行运行，这称为最高优先级（_Highest Priority First，HPF_）调度算法**。

进程的优先级可以分为，静态优先级或动态优先级：

- <font color="#ff0000">静态优先级</font>：创建进程时候，就已经确定了优先级了，然后整个运行时间优先级都不会变化；
- <font color="#ff0000">动态优先级</font>：根据进程的动态变化调整优先级，比如如果进程运行时间增加，则降低其优先级，如果进程等待时间（就绪队列的等待时间）增加，则升高其优先级，也就是**随着时间的推移增加等待进程的优先级**。

这里实现的<font color="#ff0000">静态优先级</font>

```java
@Override  
public void schedule() {  
    // 定义当前时间  
    int currentTime = 0;  
  
    // 循环调度就绪队列中的进程  
    while (!readyQueue.isEmpty()) {  
        // 找到优先级最高的进程  
  
        PCB pcb = readyQueue.get(0);  
  
        // 启动进程  
        PCB_start(pcb);  
  
        // 更新当前时间  
        currentTime += pcb.getBurstTime() - pcb.getRemainingTime();  
        System.out.println("当前时间：" + currentTime);  
    }  
    System.out.println("高优先级调度算法演示结束");  
    System.out.println("-------------------------------------");  
}
```

**实现**：与短作业优先类似，区别只是排序时根据的属性不同


## 多级反馈队列调度算法

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229225815.png?token=ATVNUUE6FHHDJ4PZ362GIRTDVWVUK)

![](https://raw.githubusercontent.com/Clear-Love/image/main/image/20221229225943.png?token=ATVNUUE62N7QUGOYETE6IHTDVWVZ4)



**多级反馈队列（_Multilevel Feedback Queue_）调度算法**是「时间片轮转算法」和「最高优先级算法」的综合和发展。

- 「<font color="#ff0000">多级</font>」表示有多个队列，每个队列优先级从高到低，同时优先级越高时间片越短。
- 「<font color=" #ff0000 ">反馈</font>」表示如果有新的进程加入优先级高的队列时，立刻停止当前正在运行的进程，转而去运行优先级高的队列；

```java
@Override  
public void schedule() {  
    int currentTime = 0;  
    while (!readyQueue.isEmpty()) {  
        //找到运行队列  
        PCBlevelQueue levelq = queues.get(0);  
        for (PCBlevelQueue queue : queues) {  
            levelq = queue;  
            PCB prb = levelq.pcbqueue.peek();  
            //若该队列为空，查找下一级队列  
            if (prb != null) {  
                break;  
            }  
        }  
  
        Queue<PCB> queue = levelq.pcbqueue;  
        System.out.println("当前队列时间片：" + levelq.timeSlice);  
        interrupt = new Timer(true);  
        interrupt.schedule(new TimerTask() {  
            @Override  
            public void run() {  
                if(!readyQueue.isEmpty()){  
                    //终止正在运行的进程并把它添加到就绪队列的末尾  
                    now.setStatus(PCB.ProcessStatus.READY);  
                    readyQueue.remove(0);  
                    readyQueue.add(now);  
                }  
  
            }  
        },0, (long) levelq.timeSlice * PCB.period);  
  
        // 循环调度就绪队列中的进程  
        while (!queue.isEmpty()) {  
            // 取出就绪队列中的第一个进程  
            now = queue.peek();  
  
            //开启进程  
            PCB_start(now);  
  
            queue.poll();  
            if(now.getStatus() == PCB.ProcessStatus.READY){  
                //若在固定时间片内未完成，添加到下一级的末尾  
                System.out.println("进程" + now.getId() + "未在固定的时间片内完成，加入下级队列");  
                queues.get(levelq.level+1 == levelNum ? levelNum:levelq.level+1).pcbqueue.add(now);  
            }  
  
            currentTime += now.getBurstTime() - now.getRemainingTime();  
            System.out.println("当前时间：" + currentTime);  
        }  
        interrupt.cancel();  
    }  
    System.out.println("多级反馈队列调度算法演示结束");  
    System.out.println("-------------------------------------");  
}
```

**实现**：使用 `private final List<PCBlevelQueue> queues;` 作为多级队列

- 设置了多个队列，赋予每个队列不同的优先级，每个**队列优先级从高到低**，同时**优先级越高时间片越短**；
- 新的进程会被放入到第一级队列的末尾，按先来先服务的原则排队等待被调度，如果在第一级队列规定的时间片没运行完成，则将其转入到第二级队列的末尾，以此类推，直至完成；
- 当较高优先级的队列为空，才调度较低优先级的队列中的进程运行。如果进程运行时，有新进程进入较高优先级的队列，则停止当前运行的进程并将其移入到原队列末尾，接着让较高优先级的进程运行；
