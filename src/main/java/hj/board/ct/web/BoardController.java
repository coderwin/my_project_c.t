package hj.board.ct.web;

import hj.board.ct.domain.Board;
import hj.board.ct.domain.Member;
import hj.board.ct.domain.UploadFile;
import hj.board.ct.repository.BoardSearchCond;
import hj.board.ct.repository.MemberRepository;
import hj.board.ct.repository.UploadFileRepository;
import hj.board.ct.service.BoardService;
import hj.board.ct.util.FileStore;
import hj.board.ct.util.Paging;
import hj.board.ct.util.SessionConst;
import hj.board.ct.web.board.*;
import hj.board.ct.web.member.login.LoginMemberFormInSession;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 게시글 controller
 */
@RequestMapping(value = "/board")
@Controller
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;
    private final FileStore fileStore; // 첨부파일 서버 컴퓨터에 저장
    private final UploadFileRepository uploadFileRepository;// 첨부파일 이름 db에 저장

    /**
     * 전체 게시글 list 보기
     */
    @GetMapping("/list")
    @ApiOperation(value = "게시글 목록")
    public String showBoardList(@ModelAttribute("boardSearchCond") BoardSearchCond boardSearchCond,
                                @RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
                                @RequestParam(required = false, defaultValue = "") String id,
                                @RequestParam(required = false, defaultValue = "") String title,
                                @RequestParam(required = false, defaultValue = "") String content,
                                @RequestParam(required = false, defaultValue = "") String writingdate,
                                Model model) {

        // boardSearchCond가 처음에 페이지 누르면 값이 없다 == null
        if(boardSearchCond.getId() == null && boardSearchCond.getTitle() == null && boardSearchCond.getContent() == null && boardSearchCond.getWritingdate() == null) {
            id = "";
            title = "";
            content = "";
            writingdate = "";
            boardSearchCond = new BoardSearchCond(id, title, content, writingdate);
        }

        // id, title, content, writingdate 값이 한 개라도 있으면 boardSearchCond에 저장하기
        if(StringUtils.hasText(id) || StringUtils.hasText(title) || StringUtils.hasText(content) || StringUtils.hasText(writingdate)) {
            boardSearchCond = new BoardSearchCond(id, title, content, writingdate);
        }

        // 페이징 위해 필요한 offset, limit을 위한 변수
        int showedBoards = 5; // 화면에 보이는 게시글 수
        int startLimit = (nowPage - 1) * showedBoards;// offset 시작점

        /// 보여지는 게시글 가져오기
        List<BoardViewForm> boardListForShowing = boardService.readBoardList(boardSearchCond, startLimit, showedBoards);
        /// 전체 게시글 수 가져오기
        List<BoardViewForm> boardList = boardService.readBoardList(boardSearchCond);

        if(boardList.size() != 0) {
            // 페이징 만들기
            String paging = makePaging(nowPage, showedBoards, boardList, boardSearchCond);
            model.addAttribute("paging", paging);
        }

        model.addAttribute("boardList", boardListForShowing);

        return "boards/boardList";
    }

    // 페이징 출력하기
    private String makePaging(int nowPage, int showedBoards, List<BoardViewForm> boardList, BoardSearchCond boardSearchCond) {
        /// 페이징 만들기
        Paging paging = new Paging();

        int showedRangeOfPage = 5; // 페이지가 보이는 범위

        // 페이징에 필요한 변수값 입력
        paging.createPaging(boardList.size(), showedBoards, showedRangeOfPage, nowPage, boardSearchCond);

        // 페이징 출력
        return paging.printPagingV2();
    }

    /**
     * 게시글 작성 form 이동
     */
    @GetMapping("/write")
    @ApiOperation(value = "게시글 작성 페이지 이동")
    public String writeBoardForm(@ModelAttribute BoardForm boardForm) {

        return "boards/createBoardForm";
    }

    /**
     * 게시글 저장 하기
     */
    @PostMapping("/write")
    @ApiOperation(value = "게시글 등록")
    public String createBoard(@Validated @ModelAttribute BoardForm boardForm,
                              BindingResult bindingResult, HttpServletRequest request) throws IOException {

        // 에러 처리
        if (bindingResult.hasErrors()) {
            log.info("/write bindingResult : {}", bindingResult);
            return "boards/createBoardForm";
        }
        // session의 login 멤버 정보 가져오기
        LoginMemberFormInSession MemberInfo = getLoginMemberInfo(request);

        // member 생성하기
        Member member = memberRepository.findById(MemberInfo.getMemberId()).orElse(null);

        // 게시글 만들기
        Board board = Board.createBoard(
                member,
                boardForm.getBoardTitle(),
                boardForm.getBoardContent(),
                boardForm.getBoardRock()
        );

        // 게시글 저장하기
        boardService.saveBoardV2(board);

        // uploadFile 서버 컴퓨터에 저장하기 + uploadFiles 생성
        // 첨부파일 서버 컴퓨터에 저장하기
        List<UploadFile> uploadFileList = fileStore.storeFiles(boardForm.getMultipartFileList());

        // uploadFileList의 모든 uploadFile을 db에 저장하기
        saveUploadFiles(board, uploadFileList);

        // 목록으로 이동하기
        return "redirect:/board/list";
    }

    /**
     * 첨부파일 서버 컴퓨터에 저장하기
     */
    private void saveUploadFiles(Board board, List<UploadFile> uploadFileList) {
        uploadFileList.stream().forEach((uploadFile) -> {

            board.addUploadFile(uploadFile);// 첨부파일이 저장되는 board 저장하기
            uploadFileRepository.save(uploadFile);
        });
    }

    // 로그인한 member의 세션에 등록된 정보 가져오기
    private LoginMemberFormInSession getLoginMemberInfo(HttpServletRequest request) {
        // member id 가져오기
        HttpSession session = request.getSession(false);
        LoginMemberFormInSession MemberInfo = (LoginMemberFormInSession)session.getAttribute(SessionConst.LOGIN_MEMBER);

        return MemberInfo;
    }

    /**
     * 게시글 상세보기
     */
    @GetMapping("/{num}/detail")
    @ApiOperation(value = "게시글 상세보기")
    public String boardDetailForm(@PathVariable Integer num, Model model) {

        // 조회수 1 증가시키기
        boardService.updateHits(num);

        // 게시글 가져오기
        Board findBoard = boardService.readBoard(num);

        // BoardDetailForm으로 바꾸기
        BoardDetailForm boardDetailForm = BoardDetailForm.createBoardDetailForm(
                findBoard.getNum(),
                findBoard.getMember().getMemberId(),
                findBoard.getBoardTitle(),
                findBoard.getBoardContent(),
                findBoard.getBoardWritingdate(),
                findBoard.getBoardHits(),
                findBoard.getUploadFileList(),
                findBoard.getReplyList(),
                findBoard.getLikesList());

        // model에 게시글 담기
        model.addAttribute("boardDetailForm", boardDetailForm);

        // view로 가기
        return "boards/detailBoardForm";
    }

    /**
     * 게시글 수정 form 이동
     */
    @GetMapping("/{num}/edit")
    @ApiOperation(value = "게시글 수정 페이지 이동")
    public String BoardUpdateForm(@PathVariable("num") Integer num,
                                  @SessionAttribute(
                                          name = SessionConst.LOGIN_MEMBER) LoginMemberFormInSession loginMemberFormInSession,
                                  Model model) {

        // 게시글 작성자와 로그인 회원의 아이디를 검사한다
        String errMessage = "게시글을 수정할 수 없습니다.";
        Board findBoard = checkAuthorWithLoginId(num, loginMemberFormInSession, errMessage);

        // updateForm에 데이터 입력
        BoardUpdateForm boardUpdateForm = BoardUpdateForm.createBoardUpdateForm(
                findBoard.getBoardTitle(),
                findBoard.getBoardContent(),
                findBoard.getBoardRock(),
                findBoard.getUploadFileList());

        model.addAttribute("boardUpdateForm", boardUpdateForm);
        // 아이디도 넘겨주기
        model.addAttribute("boardMemberId", findBoard.getMember().getMemberId());
        // 이동하기
        return "boards/updateBoardForm";
    }

    /**
     * 게시글 수정
     */
    @PostMapping("/{num}/edit")
    @ApiOperation(value = "게시글 수정")
    public String updateBoard(@PathVariable Integer num,
                              @SessionAttribute(
                                      name = SessionConst.LOGIN_MEMBER) LoginMemberFormInSession loginMemberFormInSession,
                              @Validated @ModelAttribute BoardUpdateForm boardUpdateForm,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) throws IOException {

        // 게시글 작성자와 로그인 회원의 아이디를 검사한다
        String errMessage = "게시글을 수정할 수 없습니다.";
        Board findBoard = checkAuthorWithLoginId(num, loginMemberFormInSession, errMessage);

        // 작성자와 로그인 회원이 동일 인물일 경우
        // 데이터 입력 에러 검사한다
        if(bindingResult.hasErrors()) {
            log.info("/{}/edit bindingResult : {}", num, bindingResult);
            return "boards/updateBoardForm";
        }

        // BoardUpdateForm에 uploadFileList가 넘어 왔는지 확인하기
        log.info("/{num}/edit 수정 시작 uploadFileList : {}", num, boardUpdateForm.getUploadFileList());

        // uploadFile 삭제하기
        // uploadFile 삭제 한다.
//        uploadFileRepository.deleteByBoardNum(num);

        // 게시글 수정하기
        boardService.updateBoard(num, boardUpdateForm);

        // 첨부파일을 서버에 저장하기
        List<UploadFile> uploadFileList = fileStore.storeFiles(boardUpdateForm.getMultipartFileList());

        // 첨부파일 정보를 db에 등록하기
        saveUploadFiles(findBoard, uploadFileList);

        // {num}에 데이터 입력하기
        redirectAttributes.addAttribute("num", num);

        return "redirect:/board/{num}/detail";
    }


    /**
     * 게시글 삭제 실행
     */
    @PostMapping("/{num}/delete")
    @ApiOperation(value = "게시글 삭제")
    public String deleteBoard(@PathVariable Integer num,
                              @SessionAttribute(
                                      name = SessionConst.LOGIN_MEMBER) LoginMemberFormInSession loginMemberFormInSession) {

        // 게시글 작성자와 로그인 회원의 아이디를 검사한다
        String errMessage = "게시글을 삭제할 수 없습니다.";
        checkAuthorWithLoginId(num, loginMemberFormInSession, errMessage);

        // 작성자와 로그인 회원이 동일 인물일 경우
        // 게시글을 삭제한다
        boardService.deleteBoard(num);

        // 게시물의 UploadFile도 삭제한다
        // 주인부터 지워야 할까?
        uploadFileRepository.deleteByBoardNum(num);

        // list로 이동
        return "redirect:/board/list";
    }

    // 게시글 작성자와 로그인 회원의 아이디를 검사한다
    private Board checkAuthorWithLoginId(Integer num, LoginMemberFormInSession loginMemberFormInSession, String errMessage) {
        // Board 불러오기
        Board findBoard = boardService.readBoard(num);

        // 로그인 회원과 작성자가 맞는지 검사한다.
        // X
        if(findBoard == null & !loginMemberFormInSession.getMemberId().equals(findBoard.getMember().getMemberId())) {
            throw new IllegalStateException(errMessage);
        }

        return findBoard;
    }

    //********** <img />로 이미지 조회하기   *************//
    /**
     * 저장되어 있는 이미지 파일을 보기
     */
    @ResponseBody
    @GetMapping("/images/{savedFileName}")
    @ApiOperation(value = "게시글 첨부파일 조회")
    public Resource showImage(@PathVariable String savedFileName) throws MalformedURLException {
        log.info("이미지 불러오기 saveFileName : {}", savedFileName);
        log.info("이미지 저장 root fileStore.getFullPath(savedFileName) : {}", fileStore.getFullPath(savedFileName));
        // 파일이 저장되어있는 root 가져오기

        return new UrlResource("file:" + fileStore.getFullPath(savedFileName));
    }

//    /**
//     * 로컬에 저장되어 있는 이미지 파일을 보기
//     *  - 배포에 이용
//     */
//    @ResponseBody
//    @GetMapping("/images/{savedFileName}/local")
//    public Resource showImageInLocal(@PathVariable String savedLocalFileName) throws MalformedURLException {
//        log.info("로컬 이미지 불러오기 saveFileName : {}", savedLocalFileName);
//
//        String localDirectory = "/home/ec2-user/my_project9/upload/images/";
//        log.info("로컬 이미지 저장 root fileStore.getFullPath(savedFileName) : {}", localDirectory + savedLocalFileName);
//        // 파일이 저장되어있는 root 가져오기
//
//        return new UrlResource("file:" + localDirectory + savedLocalFileName);
//    }

    // ********** 첨부파일 다운로드 받기 ************ //
    /**
     * 첨부파일 다운로드 받기
     */
    @GetMapping("/attach/{uploadFileNum}")
    @ApiOperation(value = "게시글 첨부파일 다운로드")
    public ResponseEntity<Resource> downloadUploadFile(@PathVariable("uploadFileNum") Long num) throws MalformedURLException {

        log.info("/attach/{} downloadUploadFile", num);
        // 다운받을 uploadFile 가져오기
        UploadFile uploadFile = uploadFileRepository.findByNum(num);
        String uploadFileName = uploadFile.getUploadFileName(); // 원본 파일 이름
        String storeFileName = uploadFile.getStoreFileName(); // 서버에 저장된 파일 이름

        // 서버 내부 파일을 가져오자
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        // 원본 파일 이름을 encoding 한다
        String uploadFileNameByEncoding = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        log.info("uploadFileName : uploadFileNameByEncoding = {} : {}", uploadFileName, uploadFileNameByEncoding);

        // 원본 파일 이름으로 다운로드 파일이름을 설정해준다
        String contentDisposition = "attachment; filename=\"" + uploadFileNameByEncoding + "\"";

        // 파일 다운로드 시작
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);

    }

    //************** RestAPI **************//
    /**
     * 게시글 비밀번호 가져오기
     */
    @GetMapping("/pwd/{boardNum}")
    @ApiOperation(value = "게시글 비밀번호 조회")
    public @ResponseBody BoardPwdForm getBoardPwd(@PathVariable Integer boardNum) {

        // 게시글 비밀번호 가져오기
        Board findBoard = boardService.readBoard(boardNum);

        // 데이터 전달 객체 생성하기
        BoardPwdForm boardPwdForm = null;
        if(findBoard != null) {
            boardPwdForm = BoardPwdForm.createBoardPwdForm(findBoard.getBoardRock());
        }

        log.info("getBoardPwd boardPwd : " + boardPwdForm.getBoardPwd());
        log.info("getBoardPwd boardPwdForm : " + boardPwdForm);

        return boardPwdForm;
    }

    /**
     * uploadFile 삭제하기
     */
    @DeleteMapping("/uploadFile/{num}")
    @ApiOperation(value = "게시글 첨부파일 삭제")
    public @ResponseBody void deleteUploadFile(@PathVariable Long num) {
        log.info("uploadFile 삭제 시작 num : {}", num);
        // uploadFile 삭제하기
        UploadFile findUploadFile = uploadFileRepository.findByNum(num);
        log.info("deleteUploadFile findUploadFile : {}", findUploadFile);

        uploadFileRepository.delete(findUploadFile);
        log.info("uploadFile 삭제 끝");
    }


}
