package ProcessManager;

import java.util.List;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 20:20
 * @description TODO 高响应比优先算法
 * @modified lmio
 */
public class HRRN_Schedule extends PCBList implements Scheduler{

    public HRRN_Schedule(List<PCB> pcbList) {
        super(pcbList);
    }

    @Override
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;

        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 找到响应比最高的进程
            PCB maxPcb = readyQueue.get(0);
            double maxResponseRatio = Double.MIN_VALUE;
            for (PCB pcb : readyQueue) {
                if (pcb.getBurstTime() > maxResponseRatio) {
                    maxPcb = pcb;
                    maxResponseRatio = pcb.getResponseRatio();
                }
            }

            PCB_start(maxPcb);

            // 更新当前时间
            currentTime += maxPcb.getBurstTime();
            System.out.println("当前时间：" + currentTime);
        }
    }
}
