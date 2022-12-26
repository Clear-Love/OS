package ProcessManager;

/**
 * @author lmio
 * @time 2022/12/24 13:38
 * @description TODO 一个调度器的接口，用于实现各种进程调度
 * @modified lmio
 * @version 1.0
 */
public interface Scheduler extends Runnable{
    /**
     * @author lmio
     * @description TODO 调度器
     * @time 20:14 2022/12/24
     * @name schedule
     * @returntype void
     **/
    void schedule();

    /**
     * @author lmio
     * @description TODO 插入进程
     * @time 17:26 2022/12/25
     * @name insertProcess
     * @returntype void
     **/
    void insertProcess(PCB pcb);
}
