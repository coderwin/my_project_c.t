package hj.board.ct.service;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.UploadFile;
import hj.board.ct.repository.BoardRepository;
import hj.board.ct.repository.BoardSearchCond;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.util.FileStore;
import hj.board.ct.web.board.BoardViewForm;
import hj.board.ct.web.board.BoardUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기만 가능
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final FileStore fileStore; // 첨부파일 서버 컴퓨터에 저장

    /**
     * 게시글 저장하기
     */
    @Transactional
    public Integer saveBoardV2(Board board){

        // 게시글 저장하기
        boardRepository.save(board);

        // 저장 후, 게시물 상세보기에 사용 및 test에 사용
        return board.getNum();
    }

    /**
     * 게시글 수정하기
     */
    @Transactional// 데이터 수정 가능
    public void updateBoard(Integer boardNum, BoardUpdateForm boardUpdateForm) {
        // board 가져오기
        Board board = boardRepository.findByNum(boardNum).orElse(null);

        // 수정된 부분 수정하기
        if(board != null) {
            board.setBoardTitle(boardUpdateForm.getTitle());
            board.setBoardContent(boardUpdateForm.getContent());
            board.setBoardUpdatedate(LocalDateTime.now());
            board.setBoardRock(boardUpdateForm.getRock());
        }
    }

    /**
     * 게시글 삭제하기
     */
    @Transactional
    public void deleteBoard(Integer boardNum) {
        //
        Board board = boardRepository.findByNum(boardNum).get();

        // board 삭제하기
        boardRepository.delete(board);
    }

    /**
     * 게시글 모두 불러오기 + 검색조건에 맞춰서(by 아이디, 제목, 내용, 날짜) + limit
     */
    public List<BoardViewForm> readBoardList(BoardSearchCond boardSearchCond, int startLimit, int showedBoard) {
        // 게시글 모두 불러오기
        List<Board> boardList = boardRepository.findAll(boardSearchCond, startLimit, showedBoard);

        // 이미지 파일만 담는다.
        List<BoardViewForm> boardViewFormList = getBoardFormsWithCheckingFiles(boardList);

        return boardViewFormList;
    }

    /**
     * 게시글 모두 불러오기 + 검색조건에 맞춰서(by 아이디, 제목, 내용, 날짜)
     */
    public List<BoardViewForm> readBoardList(BoardSearchCond boardSearchCond) {
        // 게시글 모두 불러오기
        List<Board> boardList = boardRepository.findAll(boardSearchCond);

        List<BoardViewForm> boardViewFormList = getBoardForms(boardList);

        return boardViewFormList;
    }

    // BoardFormList를 만들어 준다.
    private List<BoardViewForm> getBoardForms(List<Board> boardList) {
        // view에 보여줄 데이터로만 만들기
        List<BoardViewForm> boardViewFormList = new ArrayList<>();

        boardList.forEach((b) -> {

            BoardViewForm boardViewForm = BoardViewForm.createBoardForm(
                    b.getNum(),
                    b.getMember().getMemberId(),
                    b.getBoardTitle(),
                    b.getBoardContent(),
                    b.getBoardWritingdate(),
                    b.getBoardUpdatedate(),
                    b.getBoardRock(),
                    b.getUploadFileList(),
                    b.getBoardHits(),
                    b.getReplyList(),
                    b.getLikesList()
            );

            // 만든 데이터 list에 담기
            boardViewFormList.add(boardViewForm);
        });
        return boardViewFormList;
    }

    // 이미지 파일만 boardForm에 저장
    private List<BoardViewForm> getBoardFormsWithCheckingFiles(List<Board> boardList) {
        // view에 보여줄 데이터로만 만들기
        List<BoardViewForm> boardViewFormList = new ArrayList<>();

        boardList.forEach((b) -> {

            BoardViewForm boardViewForm = BoardViewForm.createBoardForm(
                    b.getNum(),
                    b.getMember().getMemberId(),
                    b.getBoardTitle(),
                    b.getBoardContent(),
                    b.getBoardWritingdate(),
                    b.getBoardUpdatedate(),
                    b.getBoardRock(),
                    BoardViewForm.checkExe(b.getUploadFileList()),
                    b.getBoardHits(),
                    b.getReplyList(),
                    b.getLikesList()
            );

            // 만든 데이터 list에 담기
            boardViewFormList.add(boardViewForm);
        });
        return boardViewFormList;
    }

    /**
     * 게시글 데이터 불러오기 - 1개 _ by Num
     */
    public Board readBoard(Integer num) {
        return boardRepository.findByNum(num).orElse(null);
    }

    /**
     * 회원 아이디에 맞는 게시글 불러오기 _ member_id
     */
    public List<BoardViewForm> readBoardListByMemberId(String id) {
        List<Board> boardList = boardRepository.findAllById(id);

        List<BoardViewForm> boardViewFormList = getBoardForms(boardList);

        return boardViewFormList;
    }

    /**
     * 회원 아이디에 맞는 게시글 불러오기 _ join 이용 _ member_id
     */
    public List<BoardViewForm> readBoardListUsingJoin(String id) {
        List<Board> boardList = boardRepository.findAllByMember(id);

        List<BoardViewForm> boardViewFormList = getBoardForms(boardList);

        return boardViewFormList;
    }


    //******* view and Controller 만들면서 새로 만든 메서드 ********//
    /**
     * 조회수 증가
     */
    @Transactional
    public void updateHits(Integer num) {

        // 수정하려는 board 가져오기
        Board board = boardRepository.findByNum(num).orElse(null);

        if(board != null) {
            // 수정 진행하기
            board.setBoardHits(board.getBoardHits() + 1);
        }
    }

















}
