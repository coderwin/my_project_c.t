package hj.board.ct.Service;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.MemberChecking;
import hj.board.ct.domain.UploadFile;
import hj.board.ct.repository.BoardRepository;
import hj.board.ct.repository.BoardSearchCond;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.service.BoardService;
import hj.board.ct.web.board.BoardUpdateForm;
import hj.board.ct.web.board.BoardViewForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    /**
     * 게시글 저장 확인 - 첨부파일 추가 전
     */
//    @Test
////    @Rollback(false)
//    public void 게시글_저장V1() throws IOException {
//
//        // given
//        // 회원 저장
//        Member member = Member.createMember(
//                "t3",
//                "123$@",
//                "hoho",
//                "t32@test",
//                "19990505",
//                MemberChecking.Y,
//                MemberChecking.N,
//                "010-1234",
//                "051-123");
//
//        memberRepository.save(member);
//
//        // 게시글 작성 객체 생성
//        BoardForm boardForm = new BoardForm(
//                member.getMemberId(),
//                "hello",
//                "test hello",
//                "picture1",
//                "pictureSave",
//                "12345");
//
//        // when
//        // 게시글 저장
//        Long saveNum
//                = boardService.saveBoard(member.getMemberId(), boardForm);
//
//        // then
//        // 게시글 존재하는 지 확인하기
//        Board findBoard
//                = boardRepository.findByNum(saveNum).orElse(null);
//
//        // 입력 데이터로 확인하기
//        // id 확인
//        boardValuesTest(findBoard.getMember().getMemberId(), boardForm.getMemberId());
//        // title 확인
//        boardValuesTest(findBoard.getBoardTitle(), boardForm.getBoardTitle());
//        // content 확인
//        boardValuesTest(findBoard.getBoardContent(), boardForm.getBoardContent());
////        // fileOriginalname 확인
////        boardValuesTest(findBoard.getFileOriginalname(), boardForm.getFileOriginalname());
////        // fileSavename 확인
////        boardValuesTest(findBoard.getFileSavename(), boardForm.getFileSavename());
//        // boardRock 확인
//        boardValuesTest(findBoard.getBoardRock(), boardForm.getBoardRock());
//    }

    // 등록한 게시물 value와 저장된 value 비교
    private static void boardValuesTest(String findValue, String boardFormValue) {
        assertThat(findValue).isEqualTo(boardFormValue);
    }

    /**
     * 게시글 저장 확인
     */
    @Test
//    @Rollback(false)
    public void 게시글_저장V2() throws IOException {

        // given
        // 회원 저장
        Member member = Member.createMember(
                "t3",
                "123$@",
                "hoho",
                "t32@test",
                "19990505",
                MemberChecking.Y,
                MemberChecking.N,
                "010-1234",
                "051-123");

        memberRepository.save(member);

        // 게시글 작성 객체 생성
        Board board = Board.createBoard(member, "hello", "haha", "12345#@");

        // when
        // 게시글 저장
        Integer saveNum = boardService.saveBoardV2(board);

        // then
        // 게시글 존재하는 지 확인하기
        Board findBoard
                = boardRepository.findByNum(saveNum).orElse(null);

        // 입력 데이터로 확인하기
        // id 확인
        boardValuesTest(findBoard.getMember().getMemberId(), board.getMember().getMemberId());
        // title 확인
        boardValuesTest(findBoard.getBoardTitle(), board.getBoardTitle());
        // content 확인
        boardValuesTest(findBoard.getBoardContent(), board.getBoardContent());
        boardValuesTest(findBoard.getBoardRock(), board.getBoardRock());
//        boardValuesTest(findBoard.getBoardRock(), "1234");
    }

    /**
     * 게시글 불러오기 확인 _ 1개
     */
    @Test
    public void 게시글_불러오기_1개() {

        // given
        // 회원 불러오기
        Member member = memberRepository.findByNum(1);
        // 게시글 작성하기
        Board board = Board.createBoard(
                member,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board);

        // when
        // 게시글 불러오기
        Board findBoard = boardService.readBoard(board.getNum());

        // then
        // 같은 board인지 확인
        assertThat(findBoard).isEqualTo(board);
    }

    /**
     * 게시글 수정 확인
     */
    @Test
    public void 게시글_수정() throws InterruptedException {

        // given
        // 회원 불러오기
        Member member = memberRepository.findByNum(1);
        // 게시글 작성하기
        Board board = Board.createBoard(
                member,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board);

        // 게시글 저장 시, 생성날짜, 수정날짜 같은지 확인하기
        assertThat(board.getBoardWritingdate()).isEqualTo(board.getBoardUpdatedate());
//        assertThat(board.getBoardWritingdate()).isNotEqualTo(board.getBoardUpdatedate());

        // 0.1초 지연 시키기
        Thread.sleep(100L);

        // 게시글 수정
        BoardUpdateForm boardUpdateForm = new BoardUpdateForm(
                "member",
                "yaho",
                "@!12");

        // when
        // 게시글 수정하기
        boardService.updateBoard(board.getNum(), boardUpdateForm);

        // then
        // 게시글 가져오기
        Board findBoard = boardRepository.findByNum(board.getNum()).orElse(null);

        // 게시글과 수정한 values와 비교하기
        boardValuesTest(findBoard.getBoardTitle(), boardUpdateForm.getTitle());
        boardValuesTest(findBoard.getBoardContent(), boardUpdateForm.getContent());
        boardValuesTest(findBoard.getBoardRock(), boardUpdateForm.getRock());

        // 게시글과 기존 게시글 비교하기
        assertThat(findBoard).isEqualTo(board);

        // board 생성날짜와 수정날짜가 다른지 확인하기
        assertThat(findBoard.getBoardWritingdate()).isNotEqualTo(findBoard.getBoardUpdatedate());
        // board 생성날짜와 수정날짜가 같은지 확인하기
//        assertThat(findBoard.getBoardWritingdate()).isEqualTo(findBoard.getBoardUpdatedate());
    }

    /**
     * 게시글 삭제하기 확인
     */
    @Test
    public void 게시글_삭제() {

        // given
        // 회원 불러오기
        Member member = memberRepository.findByNum(1);
        // 게시글 작성하기
        Board board = Board.createBoard(
                member,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board);

        // 게시글 삭제전 존재하는 지 확인
        Board findBoard1 = boardRepository.findByNum(board.getNum()).orElse(null);
        assertThat(findBoard1).isEqualTo(board);
//        assertThat(findBoard1).isNull();

        // when
        // 게시글 삭제하기
        boardService.deleteBoard(board.getNum());

        // then
        // 게시글이 있는지 없는지 확인하기
        Board findBoard2 = boardRepository.findByNum(board.getNum()).orElse(null);

        // 게시글이 없을 때
        assertThat(findBoard2).isNull();
    }

    /**
     * 모든 게시글 불러오기 + 검색 확인
     */
    @Test
    public void 모든_게시글_불러오기_with_검색() {
        // given
        // 회원 불러오기
        Member member1 = memberRepository.findByNum(1);
        Member member2 = memberRepository.findByNum(2);

        // 게시글 작성하기
        Board board1 = Board.createBoard(
                member1,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board1);

        Board board2 = Board.createBoard(
                member2,
                "hallo",
                "ok gogo",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board2);

        Board board3 = Board.createBoard(
                member1,
                "hello2",
                "let's go 123",
                "12345");

        // 게시글 저장하기(third person)
        boardRepository.save(board3);

        // when
        // 아무것도 없을 때
        findBoardListTest("", "", "", "", 3);
        // id만 있을 때
        findBoardListTest("12", "", "", "", 1);
        // title만 있을 때
        findBoardListTest("", "he", "", "", 2);
        // content만 있을 때
        findBoardListTest("", "", "12", "", 1);
        // writingdate만 있을 때
        findBoardListTest("", "", "", "20221022", 3);
        // id, title
        findBoardListTest("1", "all", "", "", 1);
        // id, content
        findBoardListTest("test", "", "go", "", 2);
        // id, writingdate
        findBoardListTest("st12", "", "", "20221022", 1);
        // title, content
        findBoardListTest("", "hello", "go", "", 1);
        // title, writingdate
        findBoardListTest("", "hello", "", "20221022", 2);
        // content, writingdate
        findBoardListTest("", "", "go 12", "20221022", 1);
        // id, title, content
        findBoardListTest("test", "ll", "g", "", 2);
        // id, title, writingdate
        findBoardListTest("es", "ll", "", "20221022", 3);
        // title, content, writingdate
        findBoardListTest("", "ll", "go", "20221022", 2);
        // id, title, content, wrigingdate
        findBoardListTest("test", "ll", "es", "20221022", 1);
    }

    private void findBoardListTest(String id, String title, String content, String writingdate, int expectedNum) {
        // 검색 조건 입력하기
        BoardSearchCond boardSearchCond = new BoardSearchCond(id, title, content, writingdate);

        // 게시글 불러오기 with 검색
        List<BoardViewForm> findBoardViewFormList= boardService.readBoardList(boardSearchCond);

        // then
        // board의 값과 BoardViewForm의 값으로 비교하기
        // or 개수로 배교하기
        assertThat(findBoardViewFormList.size()).isEqualTo(expectedNum);
    }

    /**
     * 회원 아이디에 맞는 게시물 불러오기
     */
    @Test
    public void 회원_아이디에_맞는_게시물_불러오기() {

        // given
        // 회원 불러오기
        Member member1 = memberRepository.findById("test1").orElse(null);
        Member member2 = memberRepository.findById("test12").orElse(null);

        // 게시글 작성하기
        Board board1 = Board.createBoard(
                member1,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board1);

        Board board2 = Board.createBoard(
                member2,
                "hallo",
                "ok gogo",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board2);

        Board board3 = Board.createBoard(
                member1,
                "hello2",
                "let's go 123",
                "12345");

        // 게시글 저장하기(third person)
        boardRepository.save(board3);

        // when
        // 게시글 불러오기 by Id
        List<BoardViewForm> boardViewFormListMadeByMember1 =
                boardService.readBoardListByMemberId(member1.getMemberId());
        List<BoardViewForm> boardViewFormListMadeByMember2 =
                boardService.readBoardListByMemberId(member2.getMemberId());

        // then
        // 같은 게시물인지 확인하기 _ 게시글 수로 비교하기
        assertThat(boardViewFormListMadeByMember1.size()).isEqualTo(2);
        assertThat(boardViewFormListMadeByMember2.size()).isEqualTo(1);
//        assertThat(boardViewFormListMadeByMember2.size()).isEqualTo(0);
    }

    /**
     * 회원 아이디에 맞는 게시물 불러오기 using join
     */
    @Test
    public void 회원_아이디에_맞는_게시물_불러오기_using_join() {

        // given
        // 회원 불러오기
        Member member1 = memberRepository.findById("test1").orElse(null);
        Member member2 = memberRepository.findById("test12").orElse(null);

        // 게시글 작성하기
        Board board1 = Board.createBoard(
                member1,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board1);

        Board board2 = Board.createBoard(
                member2,
                "hallo",
                "ok gogo",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board2);

        Board board3 = Board.createBoard(
                member1,
                "hello2",
                "let's go 123",
                "12345");

        // 게시글 저장하기(third person)
        boardRepository.save(board3);

        // when
        // 게시글 불러오기 by Id
        List<BoardViewForm> boardViewFormListMadeByMember1 =
                boardService.readBoardListUsingJoin(member1.getMemberId());
        List<BoardViewForm> boardViewFormListMadeByMember2 =
                boardService.readBoardListUsingJoin(member2.getMemberId());

        // then
        // 같은 게시물인지 확인하기 _ 게시글 수로 비교하기
        assertThat(boardViewFormListMadeByMember1.size()).isEqualTo(2);
        assertThat(boardViewFormListMadeByMember2.size()).isEqualTo(1);
//        assertThat(boardViewFormListMadeByMember2.size()).isEqualTo(0);
    }

    //******* view and Controller 만들면서 새로 만든 메서드 ********//
    /**
     * 조회수 증가 확인
     */
    @Test
//    @Rollback(value = false)
    public void 조회수_증가() {
        // given
        // Member 불러오기
        Member member = memberRepository.findByNum(1);

        log.info("조회수_증가 test member : {}", member);

        // 게시글 작성하기
        Board board = Board.createBoard(
                member,
                "hello",
                "test hello",
                "12345");

        // 게시글 저장하기
        boardRepository.save(board);

        log.info("조회수_증가 test board : {}", board);
        log.info("조회수_증가 test board.hits : {}", board.getBoardHits());

////        em.flush();
////        em.clear();
//
//        log.info("조회수_증가 test board : {}", board);
//        log.info("조회수_증가 test board.hits : {}", board.getBoardHits());

        // when
        // 조회수 증가 메서드 실행
        boardService.updateHits(board.getNum());

        // then
        Board findBoard = boardRepository.findByNum(board.getNum()).orElse(null);

        // 조회수가 1 증가 했는지 확인하기
        assertThat(findBoard.getBoardHits()).isEqualTo(board.getBoardHits());
        assertThat(findBoard.getBoardHits()).isEqualTo(1L);

    }

    /**
     * 이미지 파일만 추출하는 pattern 확인
     */
    @Test
    public void 이미지_파일만_추출하는_pattern() {

        // given
        String pattern = ".*(?<=\\.(jpg|JPG|png|PNG|jpeg|JPEG|gif|GIF))";
//        String pattern = ".*(?<=\\.(jpg|png|jpeg|gif))";

        String file1 = "dfsd.jpg";
        String file2 = "hello.png";
        String file3 = "hello234.sdf.jpeg";
        String file4 = "sdfasfd.jpge";
        String file5 = ".jpgsdfasfd.jpge";
//        String file6 = ".jpgsdfasfd.jpge.jpg";
//        String file7 = ".jpgsdfasfd.jpge.JPG";

        List<String> files = new ArrayList<>();

        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);
        files.add(file5);
//        files.add(file6);
//        files.add(file7);

        // when
        // 오른 것 담는 곳
        List<String> findFiles = files
                .stream()
                .filter((file) ->
//                    Pattern.matches(pattern, file)
                        file.matches(pattern)
                )
                .collect(Collectors.toList());

        // then
        // List size 확인
//        assertThat(files.size()).isEqualTo(4);
        assertThat(findFiles.size()).isEqualTo(3);

    }

    /**
     * boardList로 이동시 이미지 파일만 포함 확인 - readBoardList test
     */
    @Test
    public void 이미지파일만_담겼는지_확인하기() {
        // given
        // 회원 불러오기
        Member member1 = memberRepository.findByNum(1);
        Member member2 = memberRepository.findByNum(2);

        // 게시글 작성하기
        // 이미지 파일 생성
        UploadFile uploadFile1 = UploadFile.createUploadFile("hello.jpg", "hello.jpg");
        UploadFile uploadFile2 = UploadFile.createUploadFile("hello.png", "hello.png");
        UploadFile uploadFile3 = UploadFile.createUploadFile("hello.txt", "hello.txt");
        UploadFile uploadFile4 = UploadFile.createUploadFile("hello.hi.html", "hello.html");
        UploadFile uploadFile5 = UploadFile.createUploadFile("hello.GIF", "hello.GIF");
        UploadFile uploadFile6 = UploadFile.createUploadFile("hello.PNG", "hello.PNG");

        List<UploadFile> uploadFileList = new ArrayList<>();

        uploadFileList.add(uploadFile1);
        uploadFileList.add(uploadFile2);
        uploadFileList.add(uploadFile3);
        uploadFileList.add(uploadFile4);
        uploadFileList.add(uploadFile5);
        uploadFileList.add(uploadFile6);

        Board board1 = Board.createBoard(
                member1,
                "hello",
                "test hello",
                "12345");

        board1.setUploadFileList(uploadFileList);

        // 게시글 저장하기
        boardRepository.save(board1);


        // when
        BoardSearchCond boardSearchCond = new BoardSearchCond("", "", "", "");

        List<BoardViewForm> findBoardViewFormList = boardService.readBoardList(boardSearchCond, 0, 5);

        // then
        // 이미지 파일 수 확인하기
        BoardViewForm findBoardViewForm = findBoardViewFormList
                .stream()
                .filter((boardViewForm) ->
                        boardViewForm.getNum().equals(board1.getNum()))
                .findFirst()
                .get();

        assertThat(findBoardViewForm.getUploadFileList().size()).isEqualTo(4);

    }


}
