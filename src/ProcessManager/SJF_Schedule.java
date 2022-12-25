package ProcessManager;

import java.util.Comparator;
import java.util.List;

/**
 * @author lmio
 * @time 2022/12/24 17:03
 * @description TODO 短作业优先调度算法
 * @modified lmio
 * @version 2.0
 */
public class SJF_Schedule extends PCBList implements Scheduler{

    public SJF_Schedule(List<PCB> pcbList) {
        super(pcbList);
        readyQueue.sort(Comparator.comparingInt(PCB::getBurstTime));
    }

    // 实现Scheduler接口的schedule()方法
    @Override
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;

        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 找到周期最短的进程
            PCB pcb = readyQueue.get(0);

            // 启动进程
            PCB_start(pcb);

            // 更新当前时间
            currentTime += pcb.getBurstTime() - pcb.getRemainingTime();
            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        System.out.println("进程" + pcb.getId() + "插入");
        int newTime = pcb.getBurstTime();
        if(newTime < readyQueue.get(0).getBurstTime()){
            synchronized (this){
                //将正在运行的进程转到就绪
                readyQueue.get(0).setStatus(PCB.ProcessStatus.READY);
                //插入到队列前面
                readyQueue.add(0, pcb);
            }
        }else {
            //用插入排序使就绪队列重新排序
            for (int i = readyQueue.size()-1; i >= 0; i--) {
                int time = readyQueue.get(i).getBurstTime();
                if(newTime > time){
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
