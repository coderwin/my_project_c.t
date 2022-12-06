package hj.board.ct.repository;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class UploadFileRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UploadFileRepository uploadFileRepository;

    /**
     * 첨부파일 저장 확인
     */
    @Test
    @Rollback(value = false)
    public void 첨부파일_저장() {

        // given
        // Member 생성
        Member member = Member.createMember("test123",
                "12345",
                "하하",
                "test@test",
                "19991212",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-1234"
        );

        memberRepository.save(member); // 영속성 컨텍스트에 위치하고 있다

        // board 생성
        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        boardRepository.save(board);

        // 첨부파일 생성
        UploadFile uploadFile1 = new UploadFile("123.jpg", "1234.jpg");
        uploadFile1.addBoard(board);

        UploadFile uploadFile2 = new UploadFile("1235.jpg", "1236.jpg");
        uploadFile2.addBoard(board);

        // when
        // 저장
        uploadFileRepository.save(uploadFile1);
        uploadFileRepository.save(uploadFile2);

        // then
        // 저장되었는지 확인하기
        List<UploadFile> findUploadFiles = uploadFileRepository.findAllByBoardNum(board.getNum());

        assertThat(findUploadFiles).containsExactly(uploadFile1, uploadFile2);


    }

}
