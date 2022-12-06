package hj.board.ct.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

// 회원 전화번호
@Embeddable
@Getter
public class Phone {
    @Column(length = 11) @NotNull
    private String phone1; // 휴대전화1
    @Column(length = 11)
    private String phone2; // 집전화 or 휴대전화2

    protected Phone() {
    }

    public Phone(String phone1, String phone2) {
        this.phone1 = phone1;
        this.phone2 = phone2;
    }
}
