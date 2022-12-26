package ProcessManager;

import java.util.Comparator;
import java.util.List;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 21:34
 * @description TODO 最高优先级调度算法（抢占式）
 * @modified lmio
 */
public class HPF_Schedule extends PCBList implements Scheduler{

    public HPF_Schedule(List<PCB> pcbList) {
        super(pcbList);
        readyQueue.sort((p1, p2) -> p2.getPriority() - p1.getPriority());
    }

    // 实现Scheduler接口的schedule()方法
    @Override
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;

        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 找到优先级最高的进程

            PCB pcb = readyQueue.get(0);
            int shortestTime = pcb.getPriority();

            // 启动进程
            PCB_start(pcb);

            // 更新当前时间
            currentTime += pcb.getBurstTime() - pcb.getRemainingTime();
            System.out.println("当前时间：" + currentTime);
        }
        System.out.println("高优先级调度算法演示结束");
        System.out.println("-------------------------------------");
    }

    @Override
    public void insertProcess(PCB pcb) {
        int newPriority = pcb.getPriority();
        if(newPriority > readyQueue.get(0).getPriority()){
            synchronized (this){
                //将正在运行的进程转到就绪
                readyQueue.get(0).setStatus(PCB.ProcessStatus.READY);
                //插入到队列前面
                readyQueue.add(0, pcb);
            }
        }else {
            //用插入排序使就绪队列重新排序
            for (int i = readyQueue.size()-1; i >= 0; i--) {
                int priority = readyQueue.get(i).getPriority();
                if(newPriority < priority){
                    readyQueue.add(i+1, pcb);
                }
            }
        }
    }

    @Override
    public void run() {
        System.out.println("短作业优先调度器运行");
        schedule();
    }
}
