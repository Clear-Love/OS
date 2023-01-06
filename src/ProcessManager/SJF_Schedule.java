package ProcessManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

/**
 * @author lmio
 * @time 2022/12/24 17:03
 * @description TODO 短作业优先调度算法
 * @modified lmio
 * @version 2.0
 */
public class SJF_Schedule extends Scheduler{

    public SJF_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
        // 按作业长度从小到大排序
        readyQueue.sort(Comparator.comparingInt(PCB::getBurstTime));
    }

    // 实现Scheduler接口的schedule()方法
    @Override
    public void schedule() {
        // 循环调度就绪队列中的进程
        while (true) {
            waitReadyQueue();

            // 找到周期最短的进程
            PCB pcb = readyQueue.get(0);

            // 启动进程
            PCB_start(pcb);

            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        System.out.println("进程" + pcb.getId() + "插入");
        int newTime = pcb.getBurstTime();
        // 若就绪队列为空 直接插入到队列末尾
        if(readyQueue.isEmpty()){
            nowProcess = pcb;
            addPCB(pcb);
            return;
        }
        if(newTime < nowProcess.getBurstTime()){
            //将正在运行的进程转到就绪
            nowProcess.setStatus(PCB.ProcessStatus.READY);
            synchronized (readyQueue){
                readyQueue.add(0, pcb);
                readyQueue.notify();
            }
        }else {
            //用插入排序使就绪队列重新排序
            for (int i = readyQueue.size()-1; i >= 0; i--) {
                int time = readyQueue.get(i).getBurstTime();
                if(newTime > time){
                    synchronized (readyQueue){
                        readyQueue.add(i, pcb);
                        readyQueue.notify();
                        return;
                    }
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
