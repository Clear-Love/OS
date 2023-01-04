package ProcessManager;

import java.util.Vector;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 21:34
 * @description TODO 最高优先级调度算法（抢占式）
 * @modified lmio
 */
public class HPF_Schedule extends Scheduler{

    public HPF_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
        readyQueue.sort((p1, p2) -> p2.getPriority() - p1.getPriority());
    }

    // 实现Scheduler接口的schedule()方法
    @Override
    public void schedule() {
        // 循环调度就绪队列中的进程
        while (true) {
            waitReadyQueue();
            // 找到优先级最高的进程
            PCB pcb = readyQueue.get(0);

            // 启动进程
            PCB_start(pcb);

            System.out.println("当前时间：" + currentTime);
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        System.out.println("进程" + pcb.getId() + "插入");
        int newPriority = pcb.getPriority();
        if(readyQueue.isEmpty()){
            addPCB(pcb);
            return;
        }
        if(newPriority > readyQueue.get(0).getPriority()){
            //将正在运行的进程转到就绪
            readyQueue.get(0).setStatus(PCB.ProcessStatus.READY);
            //插入到队列前面
            addPCB(pcb);
        }else {
            //用插入排序使就绪队列重新排序
            for (int i = readyQueue.size()-1; i >= 0; i--) {
                int priority = readyQueue.get(i).getPriority();
                if(newPriority < priority){
                    addPCB(pcb);
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
