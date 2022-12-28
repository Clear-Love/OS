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
    public void run() {
        for (int pageNumber : pageSequence) {
            // 如果内存中已经有了这个页面，则不需要进行缺页处理
            if (memoryPages.containsKey(pageNumber)) {
                System.out.println("页面" + pageNumber + "正常加载");
                //访问次数加一
                memoryPages.get(pageNumber).accessCount++;
                continue;
            }

            // 如果内存中没有这个页面，则需要进行缺页处理
            pageFaultCount++;
            System.out.println("页面" + pageNumber + "发生缺页");

            // 如果内存已满，则将内存中最先加入的页面淘汰出去
            if (memoryPages.size() == Memory_PageNum) {
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

                // 从磁盘中加载页面到内存中
                loadPageFromDisk(pageNumber);

                // 输出每次淘汰的页面号
                System.out.println("Evicted page: " + evictedPageNumber);
            }

            //直接加载页面到内存
            loadPageFromDisk(pageNumber);

            //访问次数加一
            memoryPages.get(pageNumber).accessCount++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // 输出缺页的总次数
        System.out.println("Total page faults: " + pageFaultCount);
        System.out.println("缺页率：" + (double)pageFaultCount/pageSequence.size());
    }

}
