package ProcessManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * @author lmio
 * @time 2022/12/24 13:38
 * @description TODO 一个调度器的抽象类，用于实现各种进程调度
 * @modified lmio
 * @version 1.0
 */
public abstract class Scheduler implements Runnable{
    public final Vector<PCB> readyQueue;

    public int currentTime;

    public Scheduler(Vector<PCB> pcbList) {
        readyQueue = pcbList;
        currentTime = 0;
        Timer update = new Timer(true);
        update.schedule(new TimerTask() {
            @Override
            public void run() {
                // 更新所有进程的等待时间和响应比
                synchronized (readyQueue){
                    for (PCB pcb : readyQueue) {
                        pcb.setWaitingTime(pcb.getWaitingTime() + 1);
                        pcb.setResponseRatio(((double) pcb.getWaitingTime() + pcb.getBurstTime()) / pcb.getBurstTime());
                    }
                    currentTime++;
                }
            }
        }, 0, PCB.period);
    }



    /**
     * @author lmio
     * @description TODO 运行一个进程，运行结束将进程从就绪队列删除
     * @time 19:55 2022/12/24
     * @name PCB_start
     * @returntype void
     **/
    public void PCB_start(PCB pcb){
        // 启动进程
        Thread thread = new Thread(pcb);
        thread.setDaemon(true);
        thread.start();

        // 等待进程执行结束或转到就绪
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 从就绪队列中移除进程
        if(pcb.getStatus() == PCB.ProcessStatus.READY){
            System.out.println("进程" + pcb.getId() + "被中断");
        }
        if(pcb.getStatus() == PCB.ProcessStatus.TERMINATED){
            System.out.println("进程" + pcb.getId() + "运行完毕");
            synchronized (readyQueue){
                readyQueue.remove(pcb);
            }
        }

    }
    /**
     * @author lmio
     * @description TODO 调度器
     * @time 20:14 2022/12/24
     * @name schedule
     * @returntype void
     **/
    abstract void schedule();

    /**
     * @author lmio
     * @description TODO 插入进程
     * @time 17:26 2022/12/25
     * @name insertProcess
     * @returntype void
     **/
    public void insertProcess(PCB pcb){
        System.out.println("进程" + pcb.getId() + "插入");
        //直接插入到末尾
        addPCB(pcb);
    }

    /**
     * @author lmio
     * @description TODO 等待就绪队列不为空
     * @time 1:41 2023/1/5
     * @name waitReadyQueue
     * @returntype void
     **/
    public void waitReadyQueue(){
        while (readyQueue.isEmpty()){
            // 循环等待10s，若队列仍然为空，退出调度器
            synchronized (readyQueue){
                try {
                    readyQueue.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @author lmio
     * @description TODO 添加元素到就绪队列的末尾
     * @time 1:41 2023/1/5
     * @name addPCB
     * @returntype void
     **/
    public void addPCB(PCB pcb){
        synchronized (readyQueue){
            readyQueue.add(pcb);
            readyQueue.notify();
        }
    }
}
