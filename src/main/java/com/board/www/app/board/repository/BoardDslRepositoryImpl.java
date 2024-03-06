package com.board.www.app.board.repository;

import com.board.www.app.board.dto.BoardDto;
import com.board.www.app.board.dto.BoardResDto;
import com.board.www.app.board.dto.QBoardResDto_Detail;
import com.board.www.app.board.dto.QBoardResDto_Index;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.board.www.app.account.domain.QAccount.account;
import static com.board.www.app.board.domain.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class BoardDslRepositoryImpl implements BoardDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public Page<BoardResDto.Index> searchIndex(BoardDto.KeywordType keywordType, String keyword, Pageable pageable) {
        List<BoardResDto.Index> content =
                factory.select(new QBoardResDto_Index(board.id
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

        JPAQuery<BoardResDto.Index> countQuery =
                factory.select(new QBoardResDto_Index(board.id
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

    @Override
    public BoardResDto.Detail searchDetail(Long id) {
        return factory.select(new QBoardResDto_Detail(board.id
                                                    , board.title
                                                    , board.content
                                                    , board.view
                                                    , board.createdAt
                                                    , board.isScret
                                                    , board.isRemoved
                                                    , account.id
                                                    , account.username))
                      .from(board).where(board.id.eq(id)
                                       , board.isDeleted.isFalse())
                      .leftJoin(account).on(account.id.eq(board.account.id), account.isDeleted.isFalse())
                      .fetchOne();

    }
}
