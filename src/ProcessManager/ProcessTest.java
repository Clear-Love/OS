package ProcessManager;

import java.util.*;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 21:16
 * @description TODO 测试各种进程调用算法
 * @modified lmio
 */



public class ProcessTest {
    public static void main(String[] args) {

        schedule_start(new HRRN_Schedule(new Vector<>()));

    }

    public static void schedule_start(Scheduler sd) {
        Vector<PCB> newList =  newPCBlist();
        Thread th = new Thread(sd);
        th.setDaemon(true);
        th.start();
        Timer insertPCB = new Timer(true);
        insertPCB.schedule(new TimerTask() {
            @Override
            public void run() {
                Iterator<PCB> it = newList.iterator();
                while (it.hasNext()){
                    PCB pcb = it.next();
                    if(sd.currentTime >= pcb.getArrivalTime()){
                        sd.insertProcess(pcb);
                        it.remove();
                    }
                }
            }
        }, 0, PCB.period);

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
        PCB pcb11 = new PCB(11, "进程11", 6, 12, 6);
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
        pcbList.add(pcb11);
        return pcbList;
    }

}
