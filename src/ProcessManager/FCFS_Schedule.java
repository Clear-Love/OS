package ProcessManager;

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

    @Override
    public void run() {
        System.out.println("先来先服务调度器运行");
        schedule();
    }
}
