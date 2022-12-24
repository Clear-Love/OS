package ProcessManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lmio
 * @time 2022/12/24 17:03
 * @description TODO 短作业优先调度算法
 * @modified lmio
 * @version 1.0
 */
public class SJF_Schedule extends PCBList implements Scheduler {

    public SJF_Schedule(List<PCB> pcbList) {
        super(pcbList);
    }

    // 实现Scheduler接口的schedule()方法
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;

        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 找到周期最短的进程
            PCB shortest = null;
            int shortestPeriod = Integer.MAX_VALUE;
            for (PCB pcb : readyQueue) {
                if (pcb.getBurstTime() < shortestPeriod) {
                    shortest = pcb;
                    shortestPeriod = pcb.getBurstTime();
                }
            }

            PCB_start(shortest);

            // 更新当前时间
            currentTime += shortestPeriod;
            System.out.println("当前时间：" + currentTime);
        }
    }
}
