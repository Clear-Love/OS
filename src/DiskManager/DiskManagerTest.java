package DiskManager;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 17:12
 * @description TODO 测试磁盘调度
 * @modified lmio
 */
public class DiskManagerTest {
    public static void main(String[] args) {
        new FCFS(53, 199).schedule();
        new SSF(53, 199).schedule();
        new SCAN(53, 199).schedule();
        new SCS(53, 199).schedule();
    }
}
