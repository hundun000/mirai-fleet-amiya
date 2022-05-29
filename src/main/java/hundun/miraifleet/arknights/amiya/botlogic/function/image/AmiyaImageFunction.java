package hundun.miraifleet.arknights.amiya.botlogic.function.image;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;

import hundun.miraifleet.framework.core.helper.file.CacheableFileHelper;
import hundun.miraifleet.framework.core.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.image.share.function.hundun.miraifleet.image.share.function.ImageCoreKt;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import xmmt.dituon.share.BasePetService;
import xmmt.dituon.share.ConfigDTO;
import xmmt.dituon.share.ImageSynthesis;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.TimerTask;
import java.util.function.Function;

public class AmiyaImageFunction extends BaseFunction<Void>{

    @Getter
    private final CompositeCommandFunctionComponent commandComponent;

    private final BasePetService petService = new BasePetService();


    private final CacheableFileHelper petServiceCache;
    private BufferedImage defaultPetServiceFrom;
    private final SingletonDocumentRepository<ConfigDTO> petServiceConfigRepository;


    public AmiyaImageFunction(
            BaseBotLogic baseBotLogic,
            JvmPlugin plugin,
            String characterName
            ) {
        super(
                baseBotLogic,
                plugin,
                characterName,
                "AmiyaImageFunction",
                null
                );
        this.commandComponent = new CompositeCommandFunctionComponent(plugin, characterName, functionName);
        this.petServiceCache = new CacheableFileHelper(resolveFunctionCacheFileFolder(), "petService", plugin.getLogger());
        this.petServiceConfigRepository = new SingletonDocumentRepository<>(
                plugin,
                resolveFunctionConfigFile("petServiceConfig"),
                ConfigDTO.class,
                () -> Map.of(SingletonDocumentRepository.THE_SINGLETON_KEY, new ConfigDTO())
                );
        init();
    }

    private void init() {
        try {
            defaultPetServiceFrom = ImageIO.read(resolveFunctionDataFile("petService/defaultAvatar.png"));
        } catch (IOException e) {
            plugin.getLogger().error("init error:", e);
        }
        petService.readConfig(petServiceConfigRepository.findSingleton());
        petService.readData(resolveFunctionDataFile("petService/templates"));
    }

    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {
        public CompositeCommandFunctionComponent(JvmPlugin plugin, String characterName, String functionName) {
            super(plugin, characterName, functionName, functionName);
        }
        
        @SubCommand("玩test")
        public void playPhoneTest(CommandSender sender) {
            BufferedImage fromAvatarImage = defaultPetServiceFrom;
            BufferedImage toAvatarImage = defaultPetServiceFrom;
            String cacheId = MD5HashUtil.MD5HashUtilMain(fromAvatarImage) + "--" + MD5HashUtil.MD5HashUtilMain(toAvatarImage);
            plugin.getLogger().info("阿米娅玩手机 for cacheId = " + cacheId);
            petService(sender, fromAvatarImage, toAvatarImage, "阿米娅玩手机", cacheId);
        }
        

        @SubCommand("玩")
        public void playPhone(CommandSender sender, User target) {
            BufferedImage fromAvatarImage = sender.getUser() != null ? ImageSynthesis.getAvatarImage(sender.getUser().getAvatarUrl()) : defaultPetServiceFrom;
            BufferedImage toAvatarImage = ImageSynthesis.getAvatarImage(target.getAvatarUrl());
            String cacheId = MD5HashUtil.MD5HashUtilMain(fromAvatarImage) + "--" + MD5HashUtil.MD5HashUtilMain(toAvatarImage);
            plugin.getLogger().info("阿米娅玩手机 for cacheId = " + cacheId);
            petService(sender, fromAvatarImage, toAvatarImage, "阿米娅玩手机", cacheId);
        }

        private InputStream calculatePetServiceImage(BufferedImage from, BufferedImage to, String key) {
            var petServiceResult = petService.generateImage(from, to, key);
            return petServiceResult;
        }

        private void petService(CommandSender sender, BufferedImage from, BufferedImage to, String key, String cacheId) {
            Function<String, InputStream> uncachedFileProvider = it -> calculatePetServiceImage(from, to, key);
            File resultFile = petServiceCache.fromCacheOrProvider(cacheId, uncachedFileProvider);
            if (resultFile != null) {
                ExternalResource externalResource = ExternalResource.create(resultFile).toAutoCloseable();
                FunctionReplyReceiver receiver = new FunctionReplyReceiver(sender, plugin.getLogger());
                Message message = receiver.uploadImageOrNotSupportPlaceholder(externalResource);
                receiver.sendMessage(message);
            }
        }

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
            String resultString = "UNKNOWN_MD5";
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                resultString = byteArrayToHexString(md.digest(MD5HashUtil.readFileStr(image)));
            } catch (Exception ex) {

            }
            return resultString;
        }
    }


}
