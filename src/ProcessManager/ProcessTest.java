package ProcessManager;

import java.util.Vector;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 21:16
 * @description TODO 测试各种进程调用算法
 * @modified lmio
 */
public class ProcessTest {
    public static void main(String[] args) {
        schedule_start(new HPF_Schedule(newPCBlist()));
        schedule_start(new FCFS_Schedule(newPCBlist()));
        schedule_start(new HRRN_Schedule(newPCBlist()));
        schedule_start(new RR_Schedule(newPCBlist()));
        schedule_start(new SJF_Schedule(newPCBlist()));
        schedule_start(new MFQ_Schedule(newPCBlist()));

    }

    public static void schedule_start(Scheduler sd) {
        Thread th = new Thread(sd);
        th.setDaemon(true);
        th.start();
        PCB pcb11 = new PCB(11, "进程11", 6, 10, 1);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sd.insertProcess(pcb11);
        try {
            th.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vector<PCB> newPCBlist() {
        PCB pcb1 = new PCB(1, "进程1", 3, 0, 5);
        PCB pcb2 = new PCB(2, "进程2", 2, 1, 3);
        PCB pcb3 = new PCB(3, "进程3", 4, 2, 7);
        PCB pcb4 = new PCB(4, "进程4", 1, 3, 4);
        PCB pcb5 = new PCB(5, "进程5", 5, 4, 6);
        PCB pcb6 = new PCB(6, "进程6", 2, 5, 2);
        PCB pcb7 = new PCB(7, "进程7", 3, 6, 3);
        PCB pcb8 = new PCB(8, "进程8", 1, 7, 5);
        PCB pcb9 = new PCB(9, "进程9", 2, 8, 4);
        PCB pcb10 = new PCB(10, "进程10", 4, 9, 7);
        Vector<PCB> pcbList = new Vector<>();
        pcbList.add(pcb1);
        pcbList.add(pcb2);
        pcbList.add(pcb3);
        pcbList.add(pcb4);
        pcbList.add(pcb5);
        pcbList.add(pcb6);
        pcbList.add(pcb7);
        pcbList.add(pcb8);
        pcbList.add(pcb9);
        pcbList.add(pcb10);
        return pcbList;
    }
}
