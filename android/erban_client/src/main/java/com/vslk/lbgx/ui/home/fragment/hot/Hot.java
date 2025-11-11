package com.vslk.lbgx.ui.home.fragment.hot;

import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;

import java.util.List;

import lombok.Data;

public class Hot {
    @Data
    public static class Square {
        private List<PublicChatRoomAttachment> list;

        public Square(List<PublicChatRoomAttachment> list) {
            this.list = list;
        }
    }

    @Data
    public static class TopList {
        private List<HomeRoom> agreeRecommendRooms;

        public TopList(List<HomeRoom> agreeRecommendRooms) {
            this.agreeRecommendRooms = agreeRecommendRooms;
        }
    }


    public static class upTab {

    }
}
