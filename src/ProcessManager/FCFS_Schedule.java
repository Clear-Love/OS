package ProcessManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmio
 * @time 2022/12/24 15:14
 * @description TODO 先来先服务调度算法
 * @modified lmio
 * @version 1.0
 */
public class FCFS_Schedule extends PCBList implements Scheduler{

    public FCFS_Schedule(List<PCB> pcbList) {
        super(pcbList);
    }

    @Override
    public void schedule() {
        int currentTime = 0;
        // 遍历就绪队列中的所有进程
        for (PCB pcb : readyQueue) {
            // 启动进程
            PCB_start(pcb);
            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        //直接插入到末尾
        readyQueue.add(pcb);
    }

    @Override
    public void run() {
        System.out.println("先来先服务调度器运行");
        schedule();
    }
}
