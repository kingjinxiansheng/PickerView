package com.example.asus.pickerview;

import java.util.List;

/**
 * Created by ASUS on 2017/10/12.
 */

public class Bea {
    private List<中国1Bean> 中国1;

    public List<中国1Bean> get中国1() {
        return 中国1;
    }

    public void set中国1(List<中国1Bean> 中国1) {
        this.中国1 = 中国1;
    }

    public static class 中国1Bean {
        private List<String> 北京;
        private List<String> 上海;

        public List<String> get北京() {
            return 北京;
        }

        public void set北京(List<String> 北京) {
            this.北京 = 北京;
        }

        public List<String> get上海() {
            return 上海;
        }

        public void set上海(List<String> 上海) {
            this.上海 = 上海;
        }
    }
    /**
     * 中国 : {"北京":["昌平","朝阳"]}
     */




}
