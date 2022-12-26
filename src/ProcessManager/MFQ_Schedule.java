package ProcessManager;

import java.util.*;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/26 21:50
 * @description TODO 多级反馈队列调度算法
 * @modified lmio
 */
public class MFQ_Schedule extends PCBList implements Scheduler{
    //队列数，默认为3
    private int levelNum = 3;
    private final List<PCBlevelQueue> queues;
    Timer interrupt;
    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        if(levelNum > 1)
            this.levelNum = levelNum;
        else
            System.out.println("队列数应当大于1");
    }

    static class PCBlevelQueue{
        public int timeSlice;
        public int level;
        public Queue<PCB> pcbqueue;


        PCBlevelQueue(int level) {
            this.level = level;
            this.timeSlice = 2 + 2 * level;
            this.pcbqueue = new LinkedList<>();
        }
    }



    public MFQ_Schedule(Vector<PCB> pcbList) {
        super(pcbList);
        this.queues = new ArrayList<>();
        for (int i = 0; i < levelNum; i++) {
            queues.add(new PCBlevelQueue(i));
        }
        queues.get(0).pcbqueue = new LinkedList<>(readyQueue);
    }

    @Override
    public void schedule() {
        int currentTime = 0;
        while (!readyQueue.isEmpty()) {
            //找到运行队列
            PCBlevelQueue levelq = queues.get(0);
            for (PCBlevelQueue queue : queues) {
                levelq = queue;
                PCB prb = levelq.pcbqueue.peek();
                //若该队列为空，查找下一级队列
                if (prb != null) {
                    break;
                }
            }

            Queue<PCB> queue = levelq.pcbqueue;
            System.out.println("当前队列时间片：" + levelq.timeSlice);
            interrupt = new Timer(true);
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
            },0, (long) levelq.timeSlice * PCB.period);

            // 循环调度就绪队列中的进程
            while (!queue.isEmpty()) {
                // 取出就绪队列中的第一个进程
                PCB pcb = queue.peek();

                //开启进程
                PCB_start(pcb);

                queue.poll();
                if(pcb.getStatus() == PCB.ProcessStatus.READY){
                    //若在固定时间片内未完成，添加到下一级的末尾
                    System.out.println("进程" + pcb.getId() + "未在固定的时间片内完成，加入下级队列");
                    queues.get(levelq.level+1 == levelNum ? levelNum:levelq.level+1).pcbqueue.add(pcb);
                }

                currentTime += pcb.getBurstTime() - pcb.getRemainingTime();
                System.out.println("当前时间：" + currentTime);
            }
            interrupt.cancel();
        }
        System.out.println("多级反馈队列调度算法演示结束");
        System.out.println("-------------------------------------");
    }

    @Override
    public void insertProcess(PCB pcb) {
        System.out.println("进程" + pcb.getId() + "插入");
        readyQueue.add(pcb);
        queues.get(0).pcbqueue.add(pcb);
    }

    @Override
    public void run() {
        System.out.println("多级反馈调度算法开始运行");
        schedule();
    }

}
