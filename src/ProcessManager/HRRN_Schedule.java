package ProcessManager;

import java.util.Vector;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 20:20
 * @description TODO 高响应比优先算法
 * @modified lmio
 */
public class HRRN_Schedule extends PCBList implements Scheduler{

    public HRRN_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
        readyQueue.sort((p1, p2) -> (int) (p2.getResponseRatio() - p1.getResponseRatio()));
    }

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

    @Override
    public void insertProcess(PCB pcb) {
        readyQueue.add(pcb);
        System.out.println("进程" + pcb.getId() + "插入就绪队列");
    }

    @Override
    public void run() {
        System.out.println("高响应比优先调度器运行");
        schedule();
    }
}
