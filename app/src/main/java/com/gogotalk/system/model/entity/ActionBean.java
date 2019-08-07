package com.gogotalk.system.model.entity;

/**
 * Created by fucc
 * Date: 2019-08-07 15:07
 */
public class ActionBean {
    private int seq;
    private String role;
    private String action;
    private ActionData data;

    public static class ActionData {
        private String session_id;
        private String question_id;
        private String answer;
        private String user_id;
        private String user_name;

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }

    public ActionBean(int seq, String role, String action, ActionData data) {
        this.seq = seq;
        this.role = role;
        this.action = action;
        this.data = data;
    }
}
