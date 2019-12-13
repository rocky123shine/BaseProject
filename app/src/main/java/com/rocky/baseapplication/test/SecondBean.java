package com.rocky.baseapplication.test;

import java.util.List;

public  class SecondBean {

        /**
         * text : 性别
         * value : ["女","男","儿童","中性"]
         * key : sex
         * type : 0 没有打开  1 打开  2 带输入 展开
         * num : 0  外面显示几个
         * is_single : false   是否 可多选
         * choose : 4 最多可选择几个
         */

        private String text;
        private String key;
        private int type;
        private int num;
        private boolean is_single;
        private String choose;
        private List<String> value;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public boolean isIs_single() {
            return is_single;
        }

        public void setIs_single(boolean is_single) {
            this.is_single = is_single;
        }

        public String getChoose() {
            return choose;
        }

        public void setChoose(String choose) {
            this.choose = choose;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
