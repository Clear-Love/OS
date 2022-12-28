package MemoryAllocte;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 17:23
 * @description TODO 内存空间块
 * @modified lmio
 */
public class MemoryBlock implements Comparable<MemoryBlock>{
    public int startAddress;   // 起始地址
    public int size;           // 大小
    public boolean allocated;  // 是否已分配

    private String workName; //已分配人的工程名

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public MemoryBlock(int startAddress, int size) {
        this.startAddress = startAddress;
        this.size = size;
        this.allocated = false;
        this.workName = "";
    }

    @Override
    public String toString() {
        return workName +
                "\t" + startAddress +
                "\t" + size +
                "\t" + allocated;
    }

    @Override
    public int compareTo(MemoryBlock other) {
        return this.startAddress - other.startAddress;
    }
}
