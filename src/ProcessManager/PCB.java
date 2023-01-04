package ProcessManager;

/**
 * @author lmio
 * @time 2022/12/24 13:27
 * @description TODO 表示进程类应该包含进程的基本信息
 * @modified lmio
 * @version 1.0
 */


public class PCB implements Runnable{
    public static int period = 500; //无实际作用，用于模拟进程运行的等待时间，进程信息的更新周期
    private final int id;  // 进程ID
    private final String name;  // 进程名称
    private int priority;  // 进程优先级
    private final int arrivalTime;  // 进程到达时间
    private int burstTime;  // 进程需要的时间片
    private int remainingTime;  // 进程剩余的时间片
    private int waitingTime;  // 进程等待时间
    private ProcessStatus status;  // 进程状态
    private double responseRatio; //进程响应比

    // 定义进程状态的枚举类型
    public enum ProcessStatus {
        NEW, READY, RUNNING, BLOCKED, TERMINATED
    }
    public synchronized void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public synchronized ProcessStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }


    public int getBurstTime() {
        return burstTime;
    }

    public synchronized void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }


    public int getWaitingTime() {
        return waitingTime;
    }

    public void  setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public double getResponseRatio() {
        return responseRatio;
    }

    public void setResponseRatio(double responseRatio) {
        this.responseRatio = responseRatio;
    }

    // 构造函数
    public PCB(int id, String name, int priority, int arrivalTime, int burstTime) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.status = ProcessStatus.NEW;
        this.responseRatio = 1;
    }

    // 实现Runnable接口的run()方法
    @Override
    public void run() {
        // 设置进程状态为RUNNING
        setStatus(ProcessStatus.RUNNING);

        // 进程按照时间片的数量循环执行
        while (remainingTime > 0 && this.status == ProcessStatus.RUNNING) {
            // 模拟进程执行，休眠1秒
            System.out.println("进程" + id + "正在运行");
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 减少剩余时间片
            remainingTime--;
        }

        // 设置进程状态为TERMINATED 若被优先级更高的进程打断，则设置为READY
        if(remainingTime == 0){
            setStatus(ProcessStatus.TERMINATED);
            System.out.println("进程" + this.id + "终止");
        } else{
            setStatus(ProcessStatus.READY);
            System.out.println("进程" + this.id + "被其它进程抢夺");
        }

    }

    @Override
    public String toString() {
        return "PCB{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
