package MemoryAllocte;

import java.util.Comparator;

/**
 * @author lmio
 * @version 1.0
 * @time 2023/1/1 21:30
 * @description TODO 首次适应算法（First Fit）
 * @modified lmio
 */
public class FF_MemoryAllocator extends variableMemoryAllocate{

    public FF_MemoryAllocator(int totalSize) {
        super(totalSize);
        this.comparator = Comparator.comparingInt(p -> p.startAddress);
    }

}
