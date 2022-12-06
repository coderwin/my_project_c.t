package hj.board.ct.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 모든 게시글 조회 및 검색에 따른 조회를 위해 필요한 form
 */
@Getter
@Setter
@RequiredArgsConstructor
public class BoardSearchCond {

    private final String id; // 작성자 id
    private final String title; // 게시글 제목
    private final String content; // 게시글 내용
    private final String writingdate; // 글작성 날짜
}
