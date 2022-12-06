package hj.board.ct.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * 업로드 파일 or 이미지 파일 테이블
 */
@Entity
@Getter @Setter
@ToString
public class UploadFile {

    @Id
    @GeneratedValue
    @Column(name = "uploadFile_num")
    private Long num; // 파일 or 이미지 파일 순번
    @Column(length = 300)
    private String uploadFileName; // 원본 파일 이름
    @Column(length = 300)
    private String storeFileName; // 저장된 파일 이름

//    @Enumerated(EnumType.STRING)
//    private UploadFileType uploadFileType; // 업로드 파일 타입이 이미지?, 일반?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_num")
    private Board board; // 그림이 저장되어 있는 게시글


    // 생성자
    public UploadFile() {
    }

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    // ** 생성 로직 ** //

    /**
     * test에 사용
     */
    public static UploadFile createUploadFile(String uploadFileName, String storeFileName) {

        UploadFile uploadFile = new UploadFile();

        uploadFile.setUploadFileName(uploadFileName);
        uploadFile.setStoreFileName(storeFileName);

        return uploadFile;
    }

    // ** 비즈니스 로직 ** //
    /**
     * uploadFile에 board 추가하기
     */
    public void addBoard(Board board) {
        this.board = board;
    }

    /**
     * 첨부파일을
     */
}
