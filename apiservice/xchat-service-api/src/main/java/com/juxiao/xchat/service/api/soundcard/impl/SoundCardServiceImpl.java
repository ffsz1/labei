package com.juxiao.xchat.service.api.soundcard.impl;

import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.soundcard.SoundCardService;
import com.juxiao.xchat.service.api.soundcard.conf.SoundCardConf;
import com.juxiao.xchat.service.api.soundcard.conf.SoundCardTimbres;
import com.juxiao.xchat.service.api.soundcard.conf.SoundcardTextConf;
import com.juxiao.xchat.service.api.soundcard.vo.SoundCardResultVO;
import com.juxiao.xchat.service.api.soundcard.vo.SoundCardTimbresVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class SoundCardServiceImpl implements SoundCardService {
    private final Random random = new Random();
    private final Logger logger = LoggerFactory.getLogger(SoundCardService.class);
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private SoundCardConf cardConf;
    @Autowired
    private SoundcardTextConf textConf;


    @Override
    public String getText(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO user = usersManager.getUser(uid);
        List<String> list;

        if (user == null || user.getGender() == null) {
            list = random.nextBoolean() ? textConf.getMales() : textConf.getFemales();
        } else if (user.getGender().intValue() == 1) {
            list = textConf.getMales();
        } else {
            list = textConf.getFemales();
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * @param uid
     * @param sound
     * @return
     */
    @Override
    public SoundCardResultVO analysis(Long uid, MultipartFile sound) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (sound == null) {
            logger.error("请上传要鉴别的声音, uid:{}", uid);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UsersDTO user = usersManager.getUser(uid);

        double volume;
        try {
            volume = this.getSoundVolume(sound);
        } catch (IOException e) {
            logger.error("[ 解析声音文件 ]IOException异常：", e);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            // } catch (UnsupportedAudioFileException e) {
            //      logger.error("[ 解析声音文件 ]异常：", e);
            //      return new WebServiceMessage<>(500, null, "不支持的声音文件类型");
            // } catch (LineUnavailableException e) {
            //      logger.error("[ 解析声音文件 ]LineUnavailableException异常：", e);
            //      return new WebServiceMessage<>(500, null, "不支持的声音文件类型");
        } catch (Exception e) {
            logger.error("[ 解析声音文件 ]异常：", e);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        int index = (int) (volume * 100);
        SoundCardResultVO resultVo = new SoundCardResultVO();
        SoundCardTimbres timbres;
        if (user.getGender() != null && user.getGender().intValue() == 1) {
            timbres = cardConf.getMales().get(index % 5);
        } else if (user.getGender() != null && user.getGender().intValue() == 2) {
            timbres = cardConf.getFemales().get(index % 5);
        } else {
            timbres = random.nextBoolean() ? cardConf.getMales().get(index % 5) : cardConf.getFemales().get(index % 5);
        }

        int percent = RandomUtils.randomRegionInteger(71, 83);
        resultVo.setMain(new SoundCardTimbresVO(timbres.getMain(), percent));

        int[] vicepercent = RandomUtils.randomSplit(100 - percent, 3, 6, 15);
        List<String> vices = timbres.getVice();
        int top = 0;
        int bottom = 0;

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < vicepercent.length; i++) {
            int vindex = random.nextInt(vices.size());
            if (set.contains(vindex)) {
                i--;
                continue;
            }

            String name = vices.get(vindex);
            set.add(vindex);
            if (timbres.getTop() != null && timbres.getTop().contains(name)) {
                top++;
            }

            if (timbres.getBottom() != null && timbres.getBottom().contains(name)) {
                bottom++;
            }
            resultVo.addVice(new SoundCardTimbresVO(name, vicepercent[i]));
        }

        if (top == 0 && bottom == 0) {
            resultVo.setTop(random.nextBoolean());
        } else {
            resultVo.setTop(top > bottom);
        }

        List<String> partners = timbres.getPartners();
        resultVo.setBestPartner(partners.get(RandomUtils.randomRegionInteger(0, partners.size())));
        resultVo.setFascinates(RandomUtils.randomRegionInteger(80, 100) / 10.0);
        resultVo.setPounceOn(RandomUtils.randomRegionInteger(80, 100) / 10.0);
        resultVo.setStarScore(random.nextInt(10) > 4 ? 5 : 4);
        return resultVo;
    }

    /**
     * 计算声音文件的值
     *
     * @param sound
     * @return
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    private double getSoundVolume(MultipartFile sound) throws IOException {
//
//        try {

//        AudioFormat format = stream.getFormat();
//        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
//            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
//            stream = AudioSystem.getAudioInputStream(format, stream);
//        }
//        DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
//        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        byte[] buffer = new byte[1024];
        long value = 0;
        try (InputStream stream = sound.getInputStream()) {
            int read;
            while (true) {
                read = stream.read(buffer, 0, 1024);
                if (read <= 0) {
                    break;
                }
                for (int i = 0; i < read; i++) {
                    value += buffer[i] * buffer[i];
                }
            }
            double mean = value / (double) buffer.length;
            return 10 * Math.log10(mean);
        } catch (IOException e) {
            throw e;
        }
    }
}
