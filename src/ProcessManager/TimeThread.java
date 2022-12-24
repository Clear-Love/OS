package ProcessManager;

import java.util.List;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 20:38
 * @description TODO 创建一个线程每秒更新就绪队列的等待时间
 * @modified lmio
 */
class TimeThread extends Thread {
    // 定义一个List来保存所有的进程
    private final List<PCB> processList;

    // 构造函数
    public TimeThread(List<PCB> processList) {
        this.processList = processList;
    }

    @Override
    public void run() {
        // 循环每秒钟更新所有进程的等待时间
        while (!processList.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 更新所有进程的等待时间和响应比
            for (PCB pcb : processList) {
                pcb.setWaitingTime(pcb.getWaitingTime() + 1);
                pcb.setResponseRatio(((double)pcb.getWaitingTime() + pcb.getBurstTime()) / pcb.getBurstTime());
            }
        }
    }
}
