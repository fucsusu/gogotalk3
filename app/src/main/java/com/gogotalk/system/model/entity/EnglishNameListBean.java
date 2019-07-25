package com.gogotalk.system.model.entity;

import java.util.List;

public class EnglishNameListBean {

    private List<String> recommendData;
    private List<GroupDataBean> groupData;

    public List<String> getRecommendData() {
        return recommendData;
    }

    public void setRecommendData(List<String> recommendData) {
        this.recommendData = recommendData;
    }

    public List<GroupDataBean> getGroupData() {
        return groupData;
    }

    public void setGroupData(List<GroupDataBean> groupData) {
        this.groupData = groupData;
    }

    public static class GroupDataBean {
        private String FirstWord;
        private List<String> EnglishNames;

        public String getFirstWord() {
            return FirstWord;
        }

        public void setFirstWord(String FirstWord) {
            this.FirstWord = FirstWord;
        }

        public List<String> getEnglishNames() {
            return EnglishNames;
        }

        public void setEnglishNames(List<String> EnglishNames) {
            this.EnglishNames = EnglishNames;
        }
    }
}
