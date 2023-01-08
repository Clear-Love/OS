package ProcessManager;

import java.util.*;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/26 21:50
 * @description TODO 多级反馈队列调度算法
 * @modified lmio
 */
public class MFQ_Schedule extends  Scheduler{
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
        queues.get(0).pcbqueue = new LinkedList<>();
    }

    @Override
    public void schedule() {

        while (true) {
            //等待就绪队列不为空
            waitReadyQueue();
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

            // 当前队列
            Queue<PCB> queue = levelq.pcbqueue;
            int level = levelq.level;
            interrupt = new Timer(true);
            interrupt.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!queue.isEmpty()){
                        //终止正在运行的进程并把它添加到就绪队列的末尾
                        nowProcess.setStatus(PCB.ProcessStatus.READY);
                    }
                }
            },0, (long) levelq.timeSlice * PCB.period);

            // 循环调度就绪队列中的进程
            while (!queue.isEmpty()) {
                // 取出就绪队列中的第一个进程
                PCB now = queue.peek();

                //开启进程
                PCB_start(now);

                queue.poll();
                if(now.getStatus() == PCB.ProcessStatus.READY){
                    //若在固定时间片内未完成，添加到下一级的末尾
                    queues.get(Math.min(levelq.level + 1, levelNum-1)).pcbqueue.add(now);
                }
            }
            interrupt.cancel();
        }
    }

    @Override
    public void insertProcess(PCB pcb) {
        addPCB(pcb);
        queues.get(0).pcbqueue.add(pcb);
    }

    @Override
    public void run() {
        System.out.println("多级反馈调度算法开始运行");
        schedule();
    }

}
