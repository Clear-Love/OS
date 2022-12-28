package VirtualMemoryManager;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/28 17:21
 * @description TODO 物理页
 * @modified lmio
 */
public class Page {
    private final int pageNum;
    // 状态位，用于表示该页是否有效
    //public boolean valid;
    // 访问字段，用于记录该页在一段时间被访问的次数
    public int accessCount;
    // 修改位，表示该页在调入内存后是否有被修改过，简单实现暂时忽略
    public boolean modified;

    // 硬盘地址，用于指出该页在硬盘上的地址，简单实现暂时忽略
    //private int diskAddress;

    // 页面内容
    private String content;

//    public String write(String newContent){
//        accessCount++;
//        modified = true;
//        System.out.println("写入" + "页面" + pageNum);
//        content = newContent;
//        return content;
//    }
//
//    public String read(){
//        accessCount++;
//        modified = true;
//        System.out.println("[读取]" + "页面" + pageNum);
//        return content;
//    }

    public Page(int pageNum) {
        this.pageNum = pageNum;
        this.accessCount = 0;
        this.modified = false;
        this.content = "页面" + pageNum + "数据";
    }

    public Page(Page page) {
        this.pageNum = page.pageNum;
        this.accessCount = page.accessCount;
        this.modified = page.modified;
        this.content = page.content;
    }
}
