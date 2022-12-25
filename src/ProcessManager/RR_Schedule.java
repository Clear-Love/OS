package ProcessManager;

import java.util.List;

/**
 * @author lmio
 * @time 2022/12/24 17:32
 * @description TODO 时间片轮转调度算法
 * @modified lmio
 * @version 1.0
 */
public class RR_Schedule extends PCBList implements Scheduler{

    private int timeSlice;

    public RR_Schedule(List<PCB> pcbList) {
        super(pcbList);
    }


    // 实现Scheduler接口的schedule()方法
    @Override
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;

        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 取出就绪队列中的第一个进程
            int remainingSlice = timeSlice;


            while (!readyQueue.isEmpty()){
                PCB pcb = readyQueue.get(0);

                //如果该进程在时间片结束前阻塞或结束，则 CPU 立即进行切换；
                if(remainingSlice - pcb.getRemainingTime() >= 0){
                    PCB_start(pcb);
                    remainingSlice -= pcb.getPriority();
                    currentTime += pcb.getPriority();
                    System.out.println("当前时间：" + currentTime);
                }else {
                    break;
                }
            }

            //时间片用完，进程还在运行
            PCB pcb = readyQueue.get(0);
            PCB_start(pcb, remainingSlice);
            currentTime += remainingSlice;
            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        readyQueue.add(pcb);
    }

    @Override
    public void run() {
        System.out.println("时间片轮换调度器运行");
        schedule();
    }
}
