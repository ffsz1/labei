package com.vslk.lbgx.im.transfer;

import android.content.Intent;

import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.constant.RequestCode;
import com.tongdaxing.erban.R;

public class TransferAction extends BaseAction {

    public TransferAction() {
        super(R.mipmap.ic_send_hongbao, R.string.text_transfer_title);
    }

    @Override
    public void onClick() {
        TransferActivity.start(getActivity(), getAccount(), makeRequestCode(RequestCode.TRANSFER));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case RequestCode.TRANSFER:
//                TransUserBean transUserBean = (TransUserBean) data.getSerializableExtra("transfer");
//                if (transUserBean == null){
//                    ToastUtils.showLong("转账异常");
//                    break;
//                }
//                TransferAttachment transferAttachment = new TransferAttachment(CustomAttachment.CUSTOM_MSG_HEADER_TYPE_TRANSFER_FIRST, CustomAttachment.CUSTOM_MSG_HEADER_TYPE_TRANSFER_SECOND);
//                transferAttachment.setRecvUid(transUserBean.getRecvUid());
//                transferAttachment.setSendUid(transUserBean.getSendUid());
//                transferAttachment.setGoldNum(transUserBean.getGoldNum());
//                transferAttachment.setRecvName(transUserBean.getRecvName());
//                transferAttachment.setRecvAvatar(transUserBean.getRecvAvatar());
//                transferAttachment.setSendName(transUserBean.getSendName());
//                transferAttachment.setSendAvatar(transUserBean.getSendAvatar());
//                CustomMessageConfig customMessageConfig = new CustomMessageConfig();
//                customMessageConfig.enablePush = true;
//                IMMessage imMessage = MessageBuilder.createCustomMessage(transUserBean.getRecvUid() + "", SessionTypeEnum.P2P, "", transferAttachment, customMessageConfig);
//                getContainer().proxy.sendMessage(imMessage);
//                break;
//        }
    }
}
