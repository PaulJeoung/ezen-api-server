package code.code_api.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data // @Getter + @Setter + @ToString + @RequiredArgsConstructor 들을 포함함
public class PageResponseDTO<E> { // 쓸곳이 많아서 제네릭으로 클래스를 만들기
    // DTO 목록
    private List<E> dtoList;

    // PageNumberList, 숫자 박스
    private List<Integer> pageNumList;

    // 현재페이지정보
    private PageRequestDTO pageRequestDTO;

    // prev, next 화살표정보
    private boolean prev, next;

    // 총데이터, 이전페이지, 다음페이지, 총페이지, 현재페이지
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(List<E> dtoList, PageRequestDTO pageRequestDTO, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDTO = pageRequestDTO;
        this.totalCount = (int)totalCount;

        int end = (int)(Math.ceil(pageRequestDTO.getPage() / 10.0) * 10.0); // nextPage 끝점
        int start = end - 9; // prevPage 시작점
        int last = (int)(Math.ceil(totalCount / (double)pageRequestDTO.getSize())); // 라스트 페이지 넘버 10
        // 마지막 페이지가 last 이면,
        end = end > last ? last : end;
        // 이전, 1보다 크면 true
        this.prev = start > 1;
        // last 부분에 next 비활성화를 위해서
        this.next = totalCount > end * pageRequestDTO.getSize();
        // 숫자 박스 만들기
        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        // prev, next 있거나 없거나 케이스
        this.prevPage = prev ? start - 1 : 0;
        this.nextPage = next ? end + 1 : 0;
        this.totalPage = this.pageNumList.size();
        this.current = pageRequestDTO.getPage();

    }
}