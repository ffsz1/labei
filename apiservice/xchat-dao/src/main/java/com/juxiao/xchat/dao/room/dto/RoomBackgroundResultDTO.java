package com.juxiao.xchat.dao.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomBackgroundResultDTO implements Comparable<RoomBackgroundResultDTO> {

    private Integer id;

    private String name;

    private String uri;

    @Override
    public int compareTo(RoomBackgroundResultDTO o) {
        return o.id > this.id ? 1 : -1;
    }
}
