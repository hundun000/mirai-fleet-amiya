package hundun.miraifleet.arknights.amiya.botlogic.function.share;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.jetbrains.annotations.Nullable;

import hundun.miraifleet.arknights.amiya.botlogic.function.share.SharedPetFunction.MD5HashUtil;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.helper.file.CacheableFileHelper;
import hundun.miraifleet.framework.core.helper.repository.SingletonDocumentRepository;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import xmmt.dituon.share.BasePetService;
import xmmt.dituon.share.ConfigDTO;
import xmmt.dituon.share.ImageSynthesis;

import java.util.Map;
import java.util.function.Function;
/**
 * @author hundun
 * Created on 2022/06/02
 */
public class SharedPetFunction extends BaseFunction<Void> {
    
    private final CacheableFileHelper petServiceCache;
    @Getter
    private BufferedImage defaultPetServiceFrom;
    private final SingletonDocumentRepository<ConfigDTO> petServiceConfigRepository;
    private BasePetService petService = new BasePetService();
    
    public SharedPetFunction(
            BaseBotLogic baseBotLogic,
            JvmPlugin plugin,
            String characterName
            ) {
        super(
                baseBotLogic,
                plugin,
                characterName,
                "SharedPetFunction",
                null
                );
        this.petServiceCache = new CacheableFileHelper(resolveFunctionCacheFileFolder(), "petService", plugin.getLogger());
        this.petServiceConfigRepository = new SingletonDocumentRepository<ConfigDTO>(
                plugin,
                resolveFunctionRepositoryFile("petServiceConfigRepository.json"),
                ConfigDTO.class,
                () -> Map.of(SingletonDocumentRepository.THE_SINGLETON_KEY, new ConfigDTO())
                );
        try {
            defaultPetServiceFrom = ImageIO.read(resolveFunctionDataFile("petService/defaultAvatar.png"));
        } catch (IOException e) {
            plugin.getLogger().error("init error:", e);
        }
        petService.readConfig(petServiceConfigRepository.findSingleton());
        petService.readData(resolveFunctionDataFile("petService/templates"));
    }
    
    @Nullable
    public File petService(BufferedImage from, BufferedImage to, String key) {
        String cacheId = key + "--" + MD5HashUtil.MD5HashUtilMain(from) + "--" + MD5HashUtil.MD5HashUtilMain(to);
        log.info("petService for cacheId = " + cacheId);
        Function<String, InputStream> uncachedFileProvider = it -> calculatePetServiceImage(from, to, key);
        File resultFile = petServiceCache.fromCacheOrProvider(cacheId, uncachedFileProvider);
        return resultFile;
    }
    
    private InputStream calculatePetServiceImage(BufferedImage from, BufferedImage to, String key) {
        var petServiceResult = petService.generateImage(from, to, key);
        return petServiceResult;
    }
    
    public BufferedImage userAvatarOrDefaultAvatar(CommandSender sender) {
        return sender.getUser() != null ? ImageSynthesis.getAvatarImage(sender.getUser().getAvatarUrl()) : this.getDefaultPetServiceFrom();
    }

    public BufferedImage userAvatarOrDefaultAvatar(NudgeEvent event) {
        return ImageSynthesis.getAvatarImage(event.getFrom().getAvatarUrl());
    }
    
    public static class MD5HashUtil {
        private final static String[] hexDigits = { "0", "1",
                "2","3", "4", "5", "6", "7",
                "8", "9", "a", "b", "c", "d", "e", "f" };

        public static String byteArrayToHexString(byte[] b) {
            StringBuffer resultSb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                resultSb.append(byteToHexString(b[i]));
            }
            return resultSb.toString();
        }

        private static String byteToHexString(byte b) {
            int n = b;
            if (n < 0)
                n = 256 + n;
            int d1 = n / 16;
            int d2 = n % 16;
            return hexDigits[d1] + hexDigits[d2];
        }


        public static byte[] readFileStr(BufferedImage image) throws IOException{

            ByteArrayOutputStream bs =new ByteArrayOutputStream();

            ImageOutputStream imOut =ImageIO.createImageOutputStream(bs);

            ImageIO.write(image,"png",imOut);

            byte[] re = bs.toByteArray();

            return re;
        }


        public static String MD5HashUtilMain(BufferedImage image) {
            if (image == null) {
                return "NULL";
            }
            String resultString = "UNKNOWN_MD5";
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                resultString = byteArrayToHexString(md.digest(MD5HashUtil.readFileStr(image)));
            } catch (Exception ex) {
                resultString = "UNKNOWN_MD5";
            }
            return resultString;
        }
    }

    @Override
    public AbstractCommand provideCommand() {
        return null;
    }

    

}
