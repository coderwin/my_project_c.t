package hj.board.ct.util;

import hj.board.ct.domain.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PagingTest {

    /**
     * 페이지가 잘 만들어 지는지 test
     */
    @Test
    public void 페이징V1_test() {

        // given
        // paging 생성
        Paging paging = new Paging();

        // when
        // 게시물이 없을 때
//        paging.createPaging(0, 10, 5, 1);
        // startPage가 1일 때
//        paging.createPaging(2, 10, 5, 1);
        // nowPage 1일때
//        paging.createPaging(11, 10, 5, 1);
        // nowPage 1이 아닐 때
//        paging.createPaging(23, 10, 5, 2);
        // nowPage 마지막 번호 일 때
        paging.createPagingV2(23, 10, 5, 3);

        // then
        System.out.println("paging test : " + paging.printPaging());
    }


    @Test
    public void 페이징V2_test() {

        // given
        // paging 생성
        Paging paging = new Paging();

        // when
        // 게시물이 없을 때
//        paging.createPaging(0, 10, 5, 1);
        // startPage가 1일 때
//        paging.createPaging(2, 10, 5, 1);
        // nowPage 1일때
//        paging.createPaging(11, 10, 5, 1);
        // nowPage 1일때
//        paging.createPaging(11, 2, 2, 1);
        // nowPage 4일때
        paging.createPagingV2(11, 2, 2, 3);
        // nowPage 4일때
//        paging.createPaging(11, 2, 4, 1);
        // nowPage 1이 아닐 때
//        paging.createPaging(23, 10, 5, 2);
        // nowPage 마지막 번호 일 때
//        paging.createPaging(23, 10, 5, 3);
        // nowPage 마지막 앞 일 때
//        paging.createPaging(23, 5, 2, 3);
        // nowPage 중간 번호 일 때
//        paging.createPaging(115, 5, 5, 11);
        // nowPage 중간에 있는 페이징박스 일 때
//        paging.createPaging(115, 5, 5, 20);
        // nowPage 마지막 번호가 있는 페이징박스 일 때
//        paging.createPaging(115, 5, 5, 22);
        // nowPage first 번호가 있는 페이징박스 일 때
//        paging.createPaging(115, 5, 5, 2);

        // then
        System.out.println("paging test : " + paging.printPagingV2());
    }



}
