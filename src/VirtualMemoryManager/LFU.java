package VirtualMemoryManager;

import java.util.Iterator;
import java.util.Map;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 2:21
 * @description TODO 最不常用（LFU）算法,当发生缺页中断时，选择「访问次数」最少的那个页面，并将其淘汰
 * @modified lmio
 */
public class LFU extends VirtualMemoryManager{
    public LFU(int Memory_PageNum, int Disk_PageNum) {
        super(Memory_PageNum, Disk_PageNum);
    }

    @Override
    void replace() {
        System.out.println("内存已满，启用最不常用置换算法");
        // 使用迭代器遍历散列表
        Iterator<Map.Entry<Integer, Page>> iterator = memoryPages.entrySet().iterator();
        Map.Entry<Integer, Page> entry = iterator.next();
        int evictedPageNumber = entry.getKey();
        Page evictedPage = entry.getValue();

        while (iterator.hasNext()){
            Map.Entry<Integer, Page> e = iterator.next();
            if(e.getValue().accessCount < evictedPage.accessCount){
                evictedPage = e.getValue();
                evictedPageNumber = e.getKey();
            }
        }

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
