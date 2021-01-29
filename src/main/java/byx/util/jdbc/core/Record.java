package byx.util.jdbc.core;

/**
 * 结果集：对数据库查询操作返回结果的封装。
 * 一个结果集由若干个数据行组成。
 * 初始时，结果集的当前行指向第一行之前。
 * 调用next方法可以让当前行向前移动一行。
 * 若当前已到达最后一行，则next调用返回false，否则返回true。
 * 结果集的当前行只能向前移动，不能向后移动。
 * @see Row
 */
public interface Record
{
    /**
     * 获取当前行
     * @return 当前行
     */
    Row getCurrentRow();

    /**
     * 移动到下一行
     * @return 若当前已到达最后一行，则返回false，否则返回true
     */
    boolean next();
}
