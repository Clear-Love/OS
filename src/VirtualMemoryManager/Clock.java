package VirtualMemoryManager;

import java.util.LinkedList;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 0:56
 * @description TODO
 * @modified lmio
 */
public class Clock extends VirtualMemoryManager{

    //时钟指针
    int clock_point;
    //一个标记位f，f=1表示最近有使用f=0则表示该页面最近没有被使用，应该被置换。
    boolean[] f;
    //一个链表表示时钟
    private final LinkedList<Integer> clock;
    public Clock(int Memory_PageNum, int Disk_PageNum) {
        super(Memory_PageNum, Disk_PageNum);
        this.clock_point = 0;
        this.f = new boolean[Memory_PageNum];
        this.clock = new LinkedList<>();
    }

    @Override
    public void run() {
        for (int pageNumber : pageSequence) {
            // 如果内存中已经有了这个页面，则不需要进行缺页处理
            if (memoryPages.containsKey(pageNumber)) {
                System.out.println("页面" + pageNumber + "正常加载");
                //最近访问了这个页面，移到clock末尾
                clock.remove((Integer)pageNumber);
                clock.add(pageNumber);
                //访问次数加一
                memoryPages.get(pageNumber).accessCount++;
                continue;
            }

            // 如果内存中没有这个页面，则需要进行缺页处理
            pageFaultCount++;
            System.out.println("页面" + pageNumber + "发生缺页");

            // 如果内存已满，则将内存中最先加入的页面淘汰出去
            if (memoryPages.size() == Memory_PageNum) {
                System.out.println("内存已满，启用LRU置换算法");

                // 找到第一个最近未访问的
                while(f[clock_point]) {
                    f[clock_point] = false;
                    clock_point = (clock_point + 1)% Memory_PageNum;

                }
                int evictedPageNumber = clock.get(clock_point);
                Page evictedPage = memoryPages.get(evictedPageNumber);

                // 若页面修改过，将该页写回磁盘
                if(evictedPage.modified){
                    evictedPage.modified = false;
                    diskPages.remove(evictedPageNumber);
                    diskPages.put(evictedPageNumber, evictedPage);
                }

                // 维护prev链表
                clock.remove(clock_point);

                // 从内存中删除被淘汰的页面
                memoryPages.remove(evictedPageNumber);

                // 从磁盘中加载页面到内存中
                loadPageFromDisk(pageNumber);

                //访问次数加一
                memoryPages.get(pageNumber).accessCount++;

                // 输出每次淘汰的页面号
                System.out.println("Evicted page: " + evictedPageNumber);
            }

            //把新的页面插入这个位置，然后把表针前移一个位置
            clock.add(clock_point ,pageNumber);
            f[clock_point] = true;
            clock_point = (clock_point + 1)% Memory_PageNum;

            //直接加载页面到内存
            loadPageFromDisk(pageNumber);
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