package hj.board.ct.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * - Member의 LocalDateTime의 findAll 메서드 검색에 사용
 * - Board의 LocalDateTime의 findAll 메서드 검색에 사용
 *
 *  Stirng의 모양이 yyyyMMdd에서 사용한다
 */
public class LocalDateParser {

    private LocalDate searchDate;

    public LocalDateParser(String currentDate) {
        this.searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    // 해당 날짜의 시작 시간(ex: 2020-02-02 00:00:00)
    public LocalDateTime startDate() {
        return this.searchDate.atStartOfDay();
    }

    // 해당 날짜의 끝 시간(ex: 2020-02-02 23:59:59)
    public LocalDateTime endDate() {
        return LocalDateTime.of(this.searchDate, LocalTime.of(23, 59, 59));
    }

    // localDate를 String으로 바꾸기
    public static String changeTypeForBirthdayInMemberUpdateForm(LocalDate birthday) {
        // yyyy-MM-dd 형식을 String으로 바꾸기
        String parseLocalDate = birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return parseLocalDate;
    }

}
