package wsm.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Page<T> {
    public Page(int curPage, int pageSize, int totalCounts, List<T> data) {
        assert totalCounts == data.size();
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalCount = totalCounts;
        this.data = data;

        if (this.totalCount % this.pageSize == 0) {
            this.totalPages = this.totalCount / this.pageSize;
        } else {
            this.totalPages = this.totalCount / this.pageSize + 1;
        }
    }

    public boolean hasFirstPage() {
        return getCurPage() > 1;
    }

    public boolean hasLastPage() {
        return getCurPage() < getTotalPages();
    }

    public boolean hasNextPage() {
        return getCurPage() < getTotalPages();
    }

    public boolean hasPreviousPage() {
        return getCurPage() > 1;
    }

    public int getTotalPages() {
        this.totalPages = this.totalCount / this.pageSize;
        if (this.totalCount % this.pageSize != 0) {
            this.totalPages++;
        }
        return this.totalPages;
    }

    public List<T> getCurPageData() {
        int firstIndex = (this.curPage - 1) * this.pageSize;
        return this.data.subList(firstIndex, firstIndex + this.pageSize);
    }

    //The current page
    private int curPage = 1;
    private int totalPages;
    //The size of each page
    private int pageSize = 20;
    //The total count of data
    private int totalCount;

    private List<T> data;

    public static void main(String[] args) {
        Page p = new Page();
        p.setTotalCount(53344);
        System.out.println("总页数" + p.getTotalPages());
        p.setCurPage(50);
        System.out.println("有首页:" + p.hasFirstPage());
        System.out.println("有上一页:" + p.hasPreviousPage());
        System.out.println("有下一页:" + p.hasNextPage());
        System.out.println("有尾页页:" + p.hasLastPage());

    }
}
