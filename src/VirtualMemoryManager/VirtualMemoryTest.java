package VirtualMemoryManager;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/28 20:25
 * @description TODO 虚拟内存管理的测试
 * @modified lmio
 */
public class VirtualMemoryTest {
    public static void main(String[] args) {
        new FIFO(3, 10).run();
        //new LRU(3, 10).run();
        //new Clock(3, 10).run();
        //new LFU(3, 10).run();
    }
}
