package idusw.springboot.domain;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {
    private List<DTO> dtoList;

    private int totalPage;
    private int curPage;
    private int perPage;

    private int perPagination;

    private long totalRows;

    private long startRow;
    private long endRow;

    private int start, end;
    private boolean prev, next;

    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn, int perPagination) {
        totalRows = result.getTotalElements();
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        this.perPagination = perPagination;
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.curPage = pageable.getPageNumber() + 1;
        this.perPage = pageable.getPageSize();

        this.startRow = 1 + (curPage - 1) * perPage;
        this.endRow = startRow +  perPage - 1;

        int tempEnd = (int)(Math.ceil(curPage / ((double) perPagination))) * perPagination;

        start = tempEnd - (perPagination - 1);
        end = (totalPage > tempEnd)? tempEnd: totalPage;

        prev = start > 1;
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
