package MemoryAllocte;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 17:23
 * @description TODO 内存空间块
 * @modified lmio
 */
public class MemoryBlock{
    public int startAddress;   // 起始地址
    public int size;           // 大小

    public boolean allocated;

    private String workName; //已分配的工程名

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public MemoryBlock(int startAddress, int size) {
        this.startAddress = startAddress;
        this.allocated = false;
        this.size = size;
        this.workName = "";
    }

    @Override
    public String toString() {
        return workName +
                "\t" + startAddress +
                "\t" + size;
    }
}
