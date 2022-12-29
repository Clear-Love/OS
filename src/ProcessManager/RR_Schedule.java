package ProcessManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * @author lmio
 * @time 2022/12/24 17:32
 * @description TODO 时间片轮转调度算法
 * @modified lmio
 * @version 1.0
 */
public class RR_Schedule extends Scheduler{

    private int timeSlice = 4;

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    //一个定时触发的任务
    Timer interrupt;

    public RR_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
        interrupt = new Timer(true);
    }


    // 实现Scheduler接口的schedule()方法
    @Override
    public void schedule() {
        // 定义当前时间
        int currentTime = 0;
        interrupt.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!readyQueue.isEmpty()){
                    //终止正在运行的进程并把它添加到就绪队列的末尾
                    PCB pcb = readyQueue.get(0);
                    pcb.setStatus(PCB.ProcessStatus.READY);
                    readyQueue.remove(0);
                    readyQueue.add(pcb);
                }

            }
        },0, timeSlice* 500L);
        // 循环调度就绪队列中的进程
        while (!readyQueue.isEmpty()) {
            // 取出就绪队列中的第一个进程
            PCB pcb = readyQueue.get(0);

            //开启进程
            PCB_start(pcb);

            currentTime += pcb.getBurstTime() - pcb.getRemainingTime();
            System.out.println("当前时间：" + currentTime);
        }
        System.out.println("时间片轮换调度算法演示结束");
        System.out.println("-------------------------------------");
    }

    @Override
    public void insertProcess(PCB pcb) {
        System.out.println("进程" + pcb.getId() + "插入");
        readyQueue.add(pcb);
    }

    @Override
    public void run() {
        System.out.println("时间片轮换调度器运行");
        schedule();
    }
}
