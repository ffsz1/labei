package com.tongdaxing.xchat_core;


import com.tongdaxing.xchat_core.activity.ActivityCoreImpl;
import com.tongdaxing.xchat_core.activity.IActivityCore;
import com.tongdaxing.xchat_core.auth.AuthCoreImpl;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bills.BillsCoreImpl;
import com.tongdaxing.xchat_core.bills.IBillsCore;
import com.tongdaxing.xchat_core.file.FileCoreImpl;
import com.tongdaxing.xchat_core.file.IFileCore;
import com.tongdaxing.xchat_core.find.family.FamilyCoreImpl;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.gift.GiftCoreImpl;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.home.HomeCoreImpl;
import com.tongdaxing.xchat_core.home.IHomeCore;
import com.tongdaxing.xchat_core.im.avroom.AVRoomCoreImpl;
import com.tongdaxing.xchat_core.im.avroom.IAVRoomCore;
import com.tongdaxing.xchat_core.im.friend.IIMFriendCore;
import com.tongdaxing.xchat_core.im.friend.IMFriendCoreImpl;
import com.tongdaxing.xchat_core.im.login.IIMLoginCore;
import com.tongdaxing.xchat_core.im.login.IMLoginCoreImpl;
import com.tongdaxing.xchat_core.im.message.IIMMessageCore;
import com.tongdaxing.xchat_core.im.message.IMMessageCoreImpl;
import com.tongdaxing.xchat_core.im.notification.INotificationCore;
import com.tongdaxing.xchat_core.im.notification.NotificationCoreImpl;
import com.tongdaxing.xchat_core.im.room.IIMRoomCore;
import com.tongdaxing.xchat_core.im.room.IMRoomCoreImpl;
import com.tongdaxing.xchat_core.im.state.IPhoneCallStateCore;
import com.tongdaxing.xchat_core.im.state.PhoneCallStateCoreImpl;
import com.tongdaxing.xchat_core.im.sysmsg.ISysMsgCore;
import com.tongdaxing.xchat_core.im.sysmsg.SysMsgCoreImpl;
import com.tongdaxing.xchat_core.im.user.IIMUserCore;
import com.tongdaxing.xchat_core.im.user.IMUserCoreImpl;
import com.tongdaxing.xchat_core.linked.ILinkedCore;
import com.tongdaxing.xchat_core.linked.LinkedCoreImpl;
import com.tongdaxing.xchat_core.msg.ImsgCore;
import com.tongdaxing.xchat_core.msg.MsgCoreImpl;
import com.tongdaxing.xchat_core.order.IOrderCore;
import com.tongdaxing.xchat_core.order.OrderCoreImpl;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.PayCoreImpl;
import com.tongdaxing.xchat_core.pk.IPkCore;
import com.tongdaxing.xchat_core.pk.PKCoreImpl;
import com.tongdaxing.xchat_core.player.IPlayerCore;
import com.tongdaxing.xchat_core.player.IPlayerDbCore;
import com.tongdaxing.xchat_core.player.PlayerCoreImpl;
import com.tongdaxing.xchat_core.player.PlayerDbCoreImpl;
import com.tongdaxing.xchat_core.praise.IPraiseCore;
import com.tongdaxing.xchat_core.praise.PraiseCoreImpl;
import com.tongdaxing.xchat_core.realm.IRealmCore;
import com.tongdaxing.xchat_core.realm.RealmCoreImpl;
import com.tongdaxing.xchat_core.redpacket.IRedPacketCore;
import com.tongdaxing.xchat_core.redpacket.RedPacketCoreImpl;
import com.tongdaxing.xchat_core.room.IRoomCore;
import com.tongdaxing.xchat_core.room.RoomCoreImpl;
import com.tongdaxing.xchat_core.room.auction.AuctionCoreImpl;
import com.tongdaxing.xchat_core.room.auction.IAuctionCore;
import com.tongdaxing.xchat_core.room.face.FaceCoreImpl;
import com.tongdaxing.xchat_core.room.face.IFaceCore;
import com.tongdaxing.xchat_core.room.lotterybox.ILotteryBoxCore;
import com.tongdaxing.xchat_core.room.lotterybox.LotteryBoxCoreImpl;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_core.share.ShareCoreImpl;
import com.tongdaxing.xchat_core.user.AppInfoImpl;
import com.tongdaxing.xchat_core.user.AttentionCore;
import com.tongdaxing.xchat_core.user.AttentionCoreImpl;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.IUserDbCore;
import com.tongdaxing.xchat_core.user.UserCoreImpl;
import com.tongdaxing.xchat_core.user.UserDbCoreImpl;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.VersionsCoreImpl;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCore;
import com.tongdaxing.xchat_core.withdraw.WithdrawCoreImpl;
import com.tongdaxing.xchat_framework.coremanager.CoreFactory;
import com.tongdaxing.xchat_framework.coremanager.IAppInfoCore;

/**
 * Created by lijun on 2014/11/23.
 * 注册所需的core到coreFactory中，以便稍后调用。
 */
public class CoreRegisterCenter {

    public static void registerCore() {
        //Auth
        if (!CoreFactory.hasRegisteredCoreClass(IAuthCore.class)) {
            CoreFactory.registerCoreClass(IAuthCore.class, AuthCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(ImsgCore.class)) {
            CoreFactory.registerCoreClass(ImsgCore.class, MsgCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IRealmCore.class)) {
            CoreFactory.registerCoreClass(IRealmCore.class, RealmCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IIMLoginCore.class)) {
            CoreFactory.registerCoreClass(IIMLoginCore.class, IMLoginCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IIMFriendCore.class)) {
            CoreFactory.registerCoreClass(IIMFriendCore.class, IMFriendCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IIMMessageCore.class)) {
            CoreFactory.registerCoreClass(IIMMessageCore.class, IMMessageCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IUserCore.class)) {
            CoreFactory.registerCoreClass(IUserCore.class, UserCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IUserDbCore.class)) {
            CoreFactory.registerCoreClass(IUserDbCore.class, UserDbCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IFileCore.class)) {
            CoreFactory.registerCoreClass(IFileCore.class, FileCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IIMRoomCore.class)) {
            CoreFactory.registerCoreClass(IIMRoomCore.class, IMRoomCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IGiftCore.class)) {
            CoreFactory.registerCoreClass(IGiftCore.class, GiftCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IFaceCore.class)) {
            CoreFactory.registerCoreClass(IFaceCore.class, FaceCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IHomeCore.class)) {
            CoreFactory.registerCoreClass(IHomeCore.class, HomeCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(ISysMsgCore.class)) {
            CoreFactory.registerCoreClass(ISysMsgCore.class, SysMsgCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IPraiseCore.class)) {
            CoreFactory.registerCoreClass(IPraiseCore.class, PraiseCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IPlayerCore.class)) {
            CoreFactory.registerCoreClass(IPlayerCore.class, PlayerCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IPlayerDbCore.class)) {
            CoreFactory.registerCoreClass(IPlayerDbCore.class, PlayerDbCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IRoomCore.class)) {
            CoreFactory.registerCoreClass(IRoomCore.class, RoomCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IPhoneCallStateCore.class)) {
            CoreFactory.registerCoreClass(IPhoneCallStateCore.class, PhoneCallStateCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IAVRoomCore.class)) {
            CoreFactory.registerCoreClass(IAVRoomCore.class, AVRoomCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IAuctionCore.class)) {
            CoreFactory.registerCoreClass(IAuctionCore.class, AuctionCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IIMUserCore.class)) {
            CoreFactory.registerCoreClass(IIMUserCore.class, IMUserCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IOrderCore.class)) {
            CoreFactory.registerCoreClass(IOrderCore.class, OrderCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(INotificationCore.class)) {
            CoreFactory.registerCoreClass(INotificationCore.class, NotificationCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(AttentionCore.class)) {
            CoreFactory.registerCoreClass(AttentionCore.class, AttentionCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(VersionsCore.class)) {
            CoreFactory.registerCoreClass(VersionsCore.class, VersionsCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IPayCore.class)) {
            CoreFactory.registerCoreClass(IPayCore.class, PayCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IShareCore.class)) {
            CoreFactory.registerCoreClass(IShareCore.class, ShareCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(ILinkedCore.class)) {
            CoreFactory.registerCoreClass(ILinkedCore.class, LinkedCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IWithdrawCore.class)) {
            CoreFactory.registerCoreClass(IWithdrawCore.class, WithdrawCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IBillsCore.class)) {
            CoreFactory.registerCoreClass(IBillsCore.class, BillsCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IRedPacketCore.class)) {
            CoreFactory.registerCoreClass(IRedPacketCore.class, RedPacketCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IActivityCore.class)) {
            CoreFactory.registerCoreClass(IActivityCore.class, ActivityCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(ILotteryBoxCore.class)) {
            CoreFactory.registerCoreClass(ILotteryBoxCore.class, LotteryBoxCoreImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IAppInfoCore.class)){
            CoreFactory.registerCoreClass(IAppInfoCore.class, AppInfoImpl.class);
        }

        if (!CoreFactory.hasRegisteredCoreClass(IPkCore.class)) {
            CoreFactory.registerCoreClass(IPkCore.class, PKCoreImpl.class);
        }
        //家族相关事件
        if (!CoreFactory.hasRegisteredCoreClass(IFamilyCore.class)) {
            CoreFactory.registerCoreClass(IFamilyCore.class, FamilyCoreImpl.class);
        }

    }
}
