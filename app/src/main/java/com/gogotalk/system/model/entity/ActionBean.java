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
        private String session_id = "";
        private String question_id = "";
        private boolean answer;
        private String user_id = "";
        private String user_name = "";
        private int jb_num = 0;
        private String prompt_id ="";
        private String result="";

        public String getPrompt_id() {
            return prompt_id;
        }

        public void setPrompt_id(String prompt_id) {
            this.prompt_id = prompt_id;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

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

        public boolean isAnswer() {
            return answer;
        }

        public void setAnswer(boolean answer) {
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

        public int getJb_num() {
            return jb_num;
        }

        public void setJb_num(int jb_num) {
            this.jb_num = jb_num;
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionData getData() {
        return data;
    }

    public void setData(ActionData data) {
        this.data = data;
    }

    public ActionBean(int seq, String role, String action, ActionData data) {
        this.seq = seq;
        this.role = role;
        this.action = action;
        this.data = data;
    }
}
