package com.board.www.app.board.repository;

import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.dto.QBoardDto_ResponseIndexDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.board.www.app.account.domain.QAccount.account;
import static com.board.www.app.board.domain.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class BoardDslRepository {
    private final JPAQueryFactory factory;

    public Page<BoardDto.ResponseIndexDto> findAll(BoardDto.KeywordType keywordType, String keyword, Pageable pageable) {
        List<BoardDto.ResponseIndexDto> content =
                factory.select(new QBoardDto_ResponseIndexDto(board.id
                                                            , board.title
                                                            , board.view
                                                            , board.createdAt
                                                            , account.username))
                       .from(board)
                       .leftJoin(account).on(account.id.eq(board.account.id), account.isDeleted.isFalse())
                       .where(keywordEq(keywordType, keyword)
                            , board.isRemoved.isFalse()
                            , board.isDeleted.isFalse())
                       .offset(pageable.getOffset())
                       .limit(pageable.getPageSize())
                       .orderBy(board.id.desc())
                       .fetch();

        JPAQuery<BoardDto.ResponseIndexDto> countQuery =
                factory.select(new QBoardDto_ResponseIndexDto(board.id
                                                            , board.title
                                                            , board.view
                                                            , board.createdAt
                                                            , account.username))
                                   .from(board)
                                   .leftJoin(account).on(account.id.eq(board.account.id), account.isDeleted.isFalse())
                                   .where(keywordEq(keywordType, keyword)
                                        , board.isRemoved.isFalse()
                                        , board.isDeleted.isFalse());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression keywordEq(BoardDto.KeywordType keywordType, String keyword) {
        if (!hasText(keyword)) {
            return null;
        }

        // 제목
        if (BoardDto.KeywordType.TITLE == keywordType) {
            return board.title.contains(keyword);
        }
        // 등록자명
        else if (BoardDto.KeywordType.CREATEDBY == keywordType) {
            return account.username.contains(keyword);
        }

        return board.title.contains(keyword).or(account.username.contains(keyword));
    }
}
