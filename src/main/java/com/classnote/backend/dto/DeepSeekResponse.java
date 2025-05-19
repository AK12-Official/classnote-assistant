package com.classnote.backend.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class DeepSeekResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    @Data
    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    @Data
    public static class Message {
        private String role;
        private String content;

        public String getContent() {
            return content;
        }
    }
}