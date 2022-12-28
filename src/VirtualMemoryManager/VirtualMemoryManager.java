package VirtualMemoryManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/28 15:50
 * @description TODO 虚拟储存器类 假设内存有16页，磁盘有100页
 * @modified lmio
 */
public abstract class VirtualMemoryManager {
    private static final int PAGE_SIZE = 4096;
    // 内存页数
    public final int Memory_PageNum;
    // 磁盘页数
    public final int Disk_PageNum;

    public int pageFaultCount;

    public List<Integer> pageSequence;

    // 内存中的页表
    public final LinkedHashMap<Integer, Page> memoryPages;
    // 磁盘中的页表
    public final LinkedHashMap<Integer, Page> diskPages;

    public VirtualMemoryManager(int Memory_PageNum, int Disk_PageNum) {
        this.Memory_PageNum =Memory_PageNum;
        this.Disk_PageNum = Disk_PageNum;
        this.pageFaultCount = 0;
        this.memoryPages = new LinkedHashMap<>(Memory_PageNum);
        this.diskPages = new LinkedHashMap<>(Disk_PageNum);

        System.out.println("初始化磁盘页面");
        for (int i = 0; i < Disk_PageNum; i++) {
            diskPages.put(i, new Page(i));
            System.out.print(".");
        }
        System.out.println("初始化磁盘页面成功");

        // 从文件中读取页面访问序列
        System.out.println("初始化访问序列...");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/VirtualMemoryManager/visit.txt"))) {
            String line = reader.readLine();
            String[] pageNumbers = line.split(" ");
            pageSequence = new LinkedList<>();
            for (String pageNumber : pageNumbers) {
                pageSequence.add(Integer.parseInt(pageNumber));
                System.out.print(".");
            }
            System.out.println("初始化访问序列成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("初始化访问序列失败");
        }

        System.out.println("访问序列：");
        System.out.println(Arrays.toString(pageSequence.toArray()));
    }

    public void loadPageFromDisk(int pageNumber) {
        // 从磁盘中加载页面
        if(diskPages.containsKey(pageNumber)){
            Page page = diskPages.get(pageNumber);
            memoryPages.put(pageNumber, new Page(page));
        } else{
            System.out.println(" 页面加载失败，页号超出范围");
            try {
                throw new IndexOutOfBoundsException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @author lmio
     * @description TODO 内存页面调度
     * @time 0:27 2022/12/29
     * @name run
     * @returntype void
     **/
    public abstract void run();
}
