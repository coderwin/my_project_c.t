package hj.board.ct;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.UploadFile;
import hj.board.ct.repository.BoardRepository;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.repository.UploadFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 전체적 test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class AllTest {

    @Autowired
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UploadFileRepository uploadFileRepository;

    /**
     * 게시글에_사진_데이터가_포함되는지_확인
     */
    @Test
    public void 게시글에_사진_데이터가_포함되는지_확인() {
        // given
        // member 생성
        // 게시글 생성
        Member member = Member.createMember("test9",
                "1234",
                "name",
                "test@test",
                "19990202", MemberChecking.Y, MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        Board board = Board.createBoard(member, "hello", "hi", "123!@");

        // when
        boardRepository.save(board);

        // 첨부파일 생성
        UploadFile uploadFile1 = new UploadFile("123.jpg", "1234.jpg");
        uploadFile1.addBoard(board);

        UploadFile uploadFile2 = new UploadFile("1235.jpg", "1236.jpg");
        uploadFile2.addBoard(board);

        // 저장
        uploadFileRepository.save(uploadFile1);
        uploadFileRepository.save(uploadFile2);

        // 영속성 컨테이너 db 출력 후, 정리하기
        em.flush();
        em.clear();

        // when
        // board에서 uploadFileList 가져오기
        Board findBoard = boardRepository.findByNum(board.getNum()).orElse(null);
        List<UploadFile> findUploadFileList = findBoard.getUploadFileList();

        // then
        // uploadFile 존재하는지 확인하기
        findUploadFileList.stream().forEach((uploadFile) -> {
            log.info(uploadFile.toString());
        });

//        Assertions.assertThat(findUploadFileList).containsExactly(uploadFile1, uploadFile2);
//        Assertions.assertThat(findUploadFileList).isNull();
        Assertions.assertThat(findUploadFileList.size()).isEqualTo(2);
//        Assertions.assertThat(findUploadFileList.size()).isEqualTo(1);
    }










}
