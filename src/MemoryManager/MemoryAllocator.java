package MemoryManager;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 17:30
 * @description TODO 内存调度器接口
 * @modified lmio
 */
public interface MemoryAllocator {

    // 分配内存空间
    void allocate(int size, String workName);

    // 释放内存空间
    void free(String name);

    // 获取已分配的内存空间信息
    void showAllocatedMemory();

    // 获取未分配的内存空间信息
    void showFreeMemory();

}
