package com.board.www.app.board.dto;

import com.board.www.app.board.domain.Board;
import com.board.www.common.util.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

public class BoardResDto {
    @Getter
    public static class Create extends BoardDto {
        public Create(Board entity) {
            super.setId(entity.getId());
        }
    }

    @Getter
    public static class Index extends BoardDto {
        @QueryProjection
        public Index(Long id, String title, int view, LocalDateTime createdAt, String username) {
            this.setId(id);
            this.setTitle(title);
            this.setContent(null);
            this.setView(view);
            this.setIsScret(null);
            this.setIsRemoved(null);
            this.setCreatedBy(username);
            this.setCreatedAt(CommonUtils.getDateFormat(createdAt, "YYYY-MM-dd"));
        }
    }

    @JsonIgnoreProperties({ "isRemoved", "accountId" })
    @Getter
    public static class Detail extends BoardDto {
        private Long accountId;

        @QueryProjection
        public Detail(Long id, String title, String content, int view, LocalDateTime createdAt, boolean isScret, boolean isRemoved, Long accountId, String username) {
            this.setId(id);
            this.setTitle(title);
            this.setContent(content);
            this.setView(view);
            this.setIsScret(isScret);
            this.setCreatedBy(username);
            this.setCreatedAt(CommonUtils.getDateFormat(createdAt, "YYYY-MM-dd"));
            this.setIsRemoved(isRemoved);
            this.accountId = accountId;
        }
    }
}
