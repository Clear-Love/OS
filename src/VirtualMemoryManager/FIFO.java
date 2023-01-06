package VirtualMemoryManager;

import java.util.Iterator;
import java.util.Map;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/28 21:17
 * @description TODO 先进先出置换算法
 * @modified lmio
 */
public class FIFO extends VirtualMemoryManager{

    public FIFO(int Memory_PageNum, int Disk_PageNum) {
        super(Memory_PageNum, Disk_PageNum);
    }

    @Override
    void replace() {
        System.out.println("内存已满，启用FIFO置换算法");
        // 使用迭代器遍历散列表
        Iterator<Map.Entry<Integer, Page>> iterator = memoryPages.entrySet().iterator();
        Map.Entry<Integer, Page> entry = iterator.next();
        int evictedPageNumber = entry.getKey();
        Page evictedPage = entry.getValue();

        // 若页面修改过，将该页写回磁盘
        if(evictedPage.modified){
            evictedPage.modified = false;
            diskPages.remove(evictedPageNumber);
            diskPages.put(evictedPageNumber, evictedPage);
        }

        // 从内存中删除被淘汰的页面
        iterator.remove();

        // 输出每次淘汰的页面号
        System.out.println("Evicted page: " + evictedPageNumber);
    }

}
