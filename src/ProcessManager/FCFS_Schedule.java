package ProcessManager;

import java.util.Comparator;
import java.util.Vector;

/**
 * @author lmio
 * @time 2022/12/24 15:14
 * @description TODO 先来先服务调度算法
 * @modified lmio
 * @version 1.0
 */
public class FCFS_Schedule extends Scheduler{

    public FCFS_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
    }

    @Override
    public void schedule() {
        readyQueue.sort(Comparator.comparingInt(PCB::getArrivalTime));
        // 遍历就绪队列中的所有进程
        while(true){
            waitReadyQueue();
            PCB pcb = readyQueue.get(0);
            PCB_start(pcb);
            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void run() {
        System.out.println("先来先服务调度器运行");
        schedule();
    }
}
