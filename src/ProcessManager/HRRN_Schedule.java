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
        readyQueue.sort((p1, p2) -> (int) (p2.getResponseRatio() - p1.getResponseRatio()));
    }

    @Override
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;

        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 找到响应比最高的进程
            PCB maxPcb = readyQueue.get(0);

            PCB_start(maxPcb);

            // 更新当前时间
            currentTime += maxPcb.getPriority();
            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        double responseRatio = pcb.getResponseRatio();
        synchronized (this){
            double newRatio = pcb.getResponseRatio();
            if(pcb.getResponseRatio() > readyQueue.get(0).getResponseRatio()){
                //将正在运行的进程转到就绪
                readyQueue.get(0).setStatus(PCB.ProcessStatus.READY);
                //插入到队列前面
                readyQueue.add(0, pcb);
            }else {
                //用插入排序使就绪队列重新排序
                for (int i = readyQueue.size(); i > 0; i--) {
                    double ratio = readyQueue.get(i).getResponseRatio();
                    if(newRatio < ratio){
                        readyQueue.add(i+1, pcb);
                    }
                }
            }
        }
    }
}
