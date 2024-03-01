package com.board.www.app.board.utils;

import com.board.www.app.board.dto.BoardDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Component
public class BoardUtils {
    @Autowired private Validator validator;

    public List<BoardDto> givenBoards(int size) {
        List<BoardDto> result = new ArrayList<>();
        for (int i=1; i<size+1; i++) {
            BoardDto dto = new BoardDto();
            dto.setTitle("title" + i);
            dto.setContent("content" + i);
            assertThat(errorSize(dto)).isEqualTo(0); // VALIDATE
            result.add(dto);
        }
        return result;
    }

    public int errorSize(BoardDto dto) {
        Set<ConstraintViolation<BoardDto>> validate = validator.validate(dto);
        Iterator<ConstraintViolation<BoardDto>> iterator = validate.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<BoardDto> next = iterator.next();
            System.out.println(next.getMessage());
        }
        return validate.size();
    }
}
