package com.board.www.app.board.dto;

public class BoardTestDto {
    public static class create extends BoardDto {
        public create(String title, String content) {
            super.setTitle(title);
            super.setContent(content);
        }
    }
}
