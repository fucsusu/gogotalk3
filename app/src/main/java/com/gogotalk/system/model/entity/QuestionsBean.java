package com.gogotalk.system.model.entity;

import java.util.List;

public class QuestionsBean {

    /**
     * Head : 课程分级调查
     * Title : 我们需要对孩子的学习情况做一个简单的调查，以初步确定孩子的学习课程等级
     * List : [{"Key":1,"Question":"宝贝学习英语的时长","QuestionValue":[{"Key":1,"Text":"从未","Value":1},{"Key":2,"Text":"1 年以内","Value":2},{"Key":3,"Text":"1-2 年","Value":3},{"Key":4,"Text":"2 年以上","Value":4}]}]
     */

    private String Head;
    private String Title;
    private List<ListBean> List;

    public String getHead() {
        return Head;
    }

    public void setHead(String Head) {
        this.Head = Head;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        /**
         * Key : 1.0
         * Question : 宝贝学习英语的时长
         * QuestionValue : [{"Key":1,"Text":"从未","Value":1},{"Key":2,"Text":"1 年以内","Value":2},{"Key":3,"Text":"1-2 年","Value":3},{"Key":4,"Text":"2 年以上","Value":4}]
         */

        private double Key;
        private String Question;
        private List<QuestionValueBean> QuestionValue;

        public double getKey() {
            return Key;
        }

        public void setKey(double Key) {
            this.Key = Key;
        }

        public String getQuestion() {
            return Question;
        }

        public void setQuestion(String Question) {
            this.Question = Question;
        }

        public List<QuestionValueBean> getQuestionValue() {
            return QuestionValue;
        }

        public void setQuestionValue(List<QuestionValueBean> QuestionValue) {
            this.QuestionValue = QuestionValue;
        }

        public static class QuestionValueBean {
            /**
             * Key : 1.0
             * Text : 从未
             * Value : 1.0
             */

            private double Key;
            private String Text;
            private double Value;

            public double getKey() {
                return Key;
            }

            public void setKey(double Key) {
                this.Key = Key;
            }

            public String getText() {
                return Text;
            }

            public void setText(String Text) {
                this.Text = Text;
            }

            public double getValue() {
                return Value;
            }

            public void setValue(double Value) {
                this.Value = Value;
            }
        }
    }
}
