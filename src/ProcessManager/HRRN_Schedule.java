package ProcessManager;

import java.util.Vector;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 20:20
 * @description TODO 高响应比优先算法
 * @modified lmio
 */
public class HRRN_Schedule extends Scheduler{

    public HRRN_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
        readyQueue.sort((p1, p2) -> (int) (p2.getResponseRatio() - p1.getResponseRatio()));
    }

    @Override
    public void schedule() {
        // 循环调度就绪队列中的进程
        while (true) {
            waitReadyQueue();
            // 找到响应比最高的进程
            readyQueue.sort((p1, p2) -> {
                if(p2.getResponseRatio() - p1.getResponseRatio() > 0){
                    return 1;
                }
                return -1;
            });
            System.out.println('\n');
            PCB maxPcb = readyQueue.get(0);

            PCB_start(maxPcb);
        }

    }

    @Override
    public void run() {
        System.out.println("高响应比优先调度器运行");
        schedule();
    }
}
