package priv.ana.common;

import lombok.Data;
import priv.ana.pojo.vo.QuestionResponseVO;

import java.util.List;
import java.util.Map;

/**
 * 分页响应 VO
 */
@Data
public class PaginationResponse<T> {
    /**
     * 分页内容列表
     */
    private List<T> records;
    /**
     * 当前页码 (从0开始)
     */
    private long current;
    /**
     * 每页大小
     */
    private long size;
    /**
     * 总元素数量
     */
    private long total;
    /**
     * 总页数
     */
    private long pages;
}

