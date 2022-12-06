package hj.board.ct.util;

import hj.board.ct.repository.BoardSearchCond;
import lombok.extern.slf4j.Slf4j;

/**
 * 페이지를 만들어준다
 */
@Slf4j
public class Paging {

    private int startPage;// 시작 페이지
    private int endPage; // 끝 페이지
    private int showedRangeOfPage; // 페이지가 보이는 범위
    private int nowPage; // 현재 페이지
    private int helperPage; // 페이지 번호를 만들 때 필요한 변수
    private BoardSearchCond boardSearchCond; // 검색 조건 담는 변수


    /**
     * Paging 객체 만들기
     *
     * totalBoards : 전체 게시글 수
     * showedBoards : 화면에 보이는 게시글 개수
     * showedRangeOfPage : 페이지가 보이는 범위
     * private int nowPage : 현재 페이지
     * private int helperPage : 페이지 번호를 만들 때 필요한 변수
     */
    public void createPaging(int totalBoards,
                             int showedBoards,
                             int showedRangeOfPage,
                             int nowPage,
                             BoardSearchCond boardSearchCond) {

        log.info("Paging createPaging");

        this.startPage = 1;
        this.endPage = (int)Math.ceil(totalBoards / (double)showedBoards);
        this.showedRangeOfPage = showedRangeOfPage;
        this.nowPage = nowPage;
        this.helperPage = nowPage;
        this.boardSearchCond = boardSearchCond;
    }

    // test 사용
    public void createPagingV2(int totalBoards,
                             int showedBoards,
                             int showedRangeOfPage,
                             int nowPage
                             ) {

        log.info("Paging createPaging");

        this.startPage = 1;
        this.endPage = (int)Math.ceil(totalBoards / (double)showedBoards);
        this.showedRangeOfPage = showedRangeOfPage;
        this.nowPage = nowPage;
        this.helperPage = nowPage;
    }

    /**
     * 페이징 출력하기 V1
     */
    public String printPaging() {

        // 출력 결과 담을 그릇 만들기
        String result = "";

        /// endPage 0 일 때 출력물 없음
        if(endPage == 0) {
            log.info("startPage1 = : {}", startPage);
            log.info("endPage1 = : {}", endPage);
            return result;
        }
        /// 게시글 1이상일 때 출력물 결과
        // endPage 1일 때
        if(endPage == 1) {
            log.info("endPage2 = : {}", endPage);
            result += "<a href='/board/list?nowPage=" + nowPage + "'>" + nowPage + "</a>";
            return result;
        }

        // 1 < endPage 아닐 때
        // 앞 부분(<<, < 있어야 하나?)
        log.info("endPage3 = : {}", endPage);

        if(nowPage != startPage) {

            result += "<a href='/board/list?nowPage=" + startPage + "'>" + "&laquo;" + "</a>";
            result += "<a href='/board/list?nowPage=" + (nowPage - 1) + "'>" + "&lt;" + "</a>";
        }

        // 중간 부분(페이지 번호가 나오는 부분)
        int stopLine = 1; // 반복문 제어 변수;
        while(stopLine <= showedRangeOfPage) {

            // helperPage가 endPage보다 크면 나가기
            if(helperPage > endPage) {
                break;
            }
            result += "<a href='/board/list?nowPage=" + helperPage + "'>" + helperPage + "</a>";

            // 페이지 번호, 제어 변수 1씩 증가 시키기
            helperPage +=1;
            stopLine += 1;
        }

        // 끝 부분(>, >> 있어야 하나?)
        if(nowPage != endPage) {
            // 한 칸 뒤로 이동 >
            result += "<a href='/board/list?nowPage=" + (nowPage + 1) + "'>" + "&gt;" + "</a>";
            // 맨 끝 페이지로 이동 >> &raquo;
            result += "<a href='/board/list?nowPage=" + endPage + "'>" + "&raquo;" + "</a>";
        }

        return result;
    }

    /**
     * 페이징 출력하기 V2
     */
    public String printPagingV2() {

        // 조건 출력해보기
        log.info("paging boardSearchCond : {} {} {} {}", boardSearchCond.getId(), boardSearchCond.getTitle(), boardSearchCond.getContent(), boardSearchCond.getWritingdate());

        // 출력 결과 담을 그릇 만들기
        String result = "";
        int resultNextDirectionNum = (int)Math.ceil(nowPage / (double)showedRangeOfPage); // 페이징박스를 하나의 페이지로 보는 number
        int endDirectionNum = (int)Math.ceil(endPage / (double)showedRangeOfPage); // 마지막 페이지의 페이지 위치 number

        /// endPage 0 일 때 출력물 없음
        if(endPage == 0) {
            log.info("startPage1 = : {}", startPage);
            log.info("endPage1 = : {}", endPage);
            return result;
        }
        /// 게시글 1이상일 때 출력물 결과
        // endPage 1일 때
        if(endPage == 1) {
            log.info("endPage2 = : {}", endPage);
            result += "<a class='nowPageNum'>" + nowPage + "</a>";
            return result;
        }

        // 1 < endPage 아닐 때
        // 앞 부분(<<, < 있어야 하나?)
        log.info("endPage3 = : {}", endPage);
        int firstPageInPagingBox = (int)Math.floor((nowPage - 1) / (double)showedRangeOfPage); // 페이징박스에서 첫번째 페이지 number
        int nextPagingBox = 0; // 페이징박스로 이동 변수

        if(firstPageInPagingBox != 0) {
            nextPagingBox =  (firstPageInPagingBox - 1) * showedRangeOfPage + 1;
        } else {
            nextPagingBox =  firstPageInPagingBox * showedRangeOfPage + 1;
        }

        // showedRangeOfPage와 같거나 작을 때
        if(nowPage > showedRangeOfPage) {
            // 이전 페이징박스로 이동
            result += "<a href='/board/list?nowPage=" + nextPagingBox + "&id=" + boardSearchCond.getId()+ "&title=" + boardSearchCond.getTitle() + "&content=" + boardSearchCond.getContent() + "&writingdate=" + boardSearchCond.getWritingdate() + "'>" + "&lt;" + "</a>"; // <
        }

        // 중간 부분(페이지 번호가 나오는 부분)
        int stopLine = 1; // 반복문 제어 변수;
        int countPage =  firstPageInPagingBox * showedRangeOfPage + 1;//  페이지 번호가 보이게 도와주는 변수


        // showedRangeOfPage만큼 반복한다
        while(stopLine <= showedRangeOfPage) {
            // helperPage가 endPage보다 크면 나가기
            if(countPage > endPage) {
                break;
            }

            // 현재 페이지는 다르게 표현
            if(countPage == nowPage) {
                result += "<a class='nowPageNum'>" + countPage + "</a>";
            } else {
                result += "<a href='/board/list?nowPage=" + countPage + "&id=" + boardSearchCond.getId()+ "&title=" + boardSearchCond.getTitle() + "&content=" + boardSearchCond.getContent() + "&writingdate=" + boardSearchCond.getWritingdate() + "'>" + countPage + "</a>";
            }

            // 페이지 번호, 제어 변수 1씩 증가 시키기
            countPage +=1;
            stopLine += 1;
        }

        // 끝 부분(>, >> 있어야 하나?)
        if(resultNextDirectionNum < endDirectionNum) {
            // 다음 페이징박스로 이동 >
            if(nowPage <= showedRangeOfPage) {
                result += "<a href='/board/list?nowPage=" + (nextPagingBox + showedRangeOfPage * 1) + "&id=" + boardSearchCond.getId()+ "&title=" + boardSearchCond.getTitle() + "&content=" + boardSearchCond.getContent() + "&writingdate=" + boardSearchCond.getWritingdate() + "'>" + "&gt;" + "</a>";
            } else {
                result += "<a href='/board/list?nowPage=" + (nextPagingBox + showedRangeOfPage * 2) + "&id=" + boardSearchCond.getId()+ "&title=" + boardSearchCond.getTitle() + "&content=" + boardSearchCond.getContent() + "&writingdate=" + boardSearchCond.getWritingdate() + "'>" + "&gt;" + "</a>";
            }
        }

        return result;
    }
}
