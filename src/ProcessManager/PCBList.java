package ProcessManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author lmio
 * @time 2022/12/24 19:09
 * @description TODO 就绪队列
 * @modified lmio
 * @version 1.0
 */
public class PCBList {
    public final  List<PCB> readyQueue;

    public PCBList(List<PCB> pcbList) {
        readyQueue = pcbList;
        Timer update = new Timer();
        update.schedule(new TimerTask() {
            @Override
            public void run() {
                // 更新所有进程的等待时间和响应比
                for (PCB pcb : readyQueue) {
                    pcb.setWaitingTime(pcb.getWaitingTime() + 1);
                    pcb.setResponseRatio(((double)pcb.getWaitingTime() + pcb.getBurstTime()) / pcb.getBurstTime());
                }
            }
        }, 0, 500);
    }



    /**
     * @author lmio
     * @description TODO 运行一个进程，运行结束将进程从就绪队列删除
     * @time 19:55 2022/12/24
     * @name PCB_start
     * @returntype void
     **/
    public void PCB_start(PCB pcb){
        // 启动进程
        Thread thread = new Thread(pcb);
        thread.start();

        // 等待进程执行结束或转到就绪
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 从就绪队列中移除进程
        if(pcb.getStatus() == PCB.ProcessStatus.READY){
            System.out.println("进程" + pcb.getId() + "被中断");
        }
        if(pcb.getStatus() == PCB.ProcessStatus.TERMINATED){
            System.out.println("进程" + pcb.getId() + "运行完毕");
            readyQueue.remove(pcb);
        }

    }


    /**
     * @author lmio
     * @description TODO 在固定的时间内运行一个进程，如果时间片用完，进程还在运行，那么将会把此进程从 CPU 释放出来
     * @time 19:53 2022/12/24
     * @name PCB_start
     * @returntype void
     **/
    public void PCB_start(PCB pcb, int limit_time){

        int start_remainTime = pcb.getRemainingTime();

        // 启动进程
        Thread thread = new Thread(pcb);
        thread.start();

        Timer waitPBC = new Timer();
        waitPBC.schedule(new TimerTask() {
            @Override
            public void run() {
                while(true){
                    if(start_remainTime - pcb.getRemainingTime() == limit_time){
                        //将进程从cpu中释放
                        pcb.setStatus(PCB.ProcessStatus.BLOCKED);
                        break;
                    }
                    if(pcb.getStatus() == PCB.ProcessStatus.TERMINATED)
                        break;
                }
            }
        }, 0);
        // 若进程终止从就绪队列中移除进程
        if(pcb.getStatus() == PCB.ProcessStatus.TERMINATED)
            readyQueue.remove(pcb);
    }
}
