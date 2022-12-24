package ProcessManager;

/**
 * @author lmio
 * @time 2022/12/24 13:27
 * @description TODO 表示进程类应该包含进程的基本信息
 * @modified lmio
 * @version 1.0
 */


public class PCB implements Runnable{
    private int id;  // 进程ID
    private String name;  // 进程名称
    private int priority;  // 进程优先级
    private int arrivalTime;  // 进程到达时间
    private int burstTime;  // 进程需要的时间片
    private int remainingTime;  // 进程剩余的时间片
    private int waitingTime;  // 进程等待时间
    private ProcessStatus status;  // 进程状态
    private double responseRatio;

    // 定义进程状态的枚举类型
    public enum ProcessStatus {
        NEW, READY, RUNNING, BLOCKED, TERMINATED
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 减少剩余时间片
            remainingTime--;
        }

        // 设置进程状态为TERMINATED 若被优先级更高的进程打断，则设置为READY
        if(remainingTime == 0)
            setStatus(ProcessStatus.TERMINATED);
        else
            setStatus(ProcessStatus.READY);
    }
}
