package com.joyfarm.farmstival.activity.constants;

import java.util.List;

public enum Status {
    APPLY("예약"),
    CANCEL("예약취소");

    private final String title;

    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    
    public static List<String[]> getList() {
        return List.of(
                new String[]{APPLY.name(), APPLY.title},
                new String[]{CANCEL.name(), CANCEL.title}
        ); //관리자에서 사용할 예정
    }
}
