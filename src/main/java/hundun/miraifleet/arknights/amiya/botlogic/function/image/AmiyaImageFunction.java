package hundun.miraifleet.arknights.amiya.botlogic.function.image;

import hundun.miraifleet.arknights.amiya.botlogic.function.share.SharedPetFunction;
import hundun.miraifleet.arknights.amiya.botlogic.function.share.SharedPetFunction.MD5HashUtil;
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

    private SharedPetFunction petFunction;

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
    }

    public void lazyInitSharedFunction(SharedPetFunction petFunction) {
        this.petFunction = petFunction;
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
            if (!checkCosPermission(sender)) {
                return;
            }
            BufferedImage toAvatarImage = petFunction.getDefaultPetServiceFrom();
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(sender, plugin.getLogger());
            playPhoneCore(receiver, toAvatarImage);
        }
        

        @SubCommand("玩")
        public void playPhone(CommandSender sender, User target) {
            if (!checkCosPermission(sender)) {
                return;
            }
            BufferedImage toAvatarImage = ImageSynthesis.getAvatarImage(target.getAvatarUrl());
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(sender, plugin.getLogger());
            playPhoneCore(receiver, toAvatarImage);
        }

        
        
        

    }

    private void playPhoneCore(FunctionReplyReceiver receiver, BufferedImage toAvatarImage) {
        var resultFile = petFunction.petService(null, toAvatarImage, "阿米娅玩手机");
        if (resultFile != null) {
            ExternalResource externalResource = ExternalResource.create(resultFile).toAutoCloseable();
            Message message = receiver.uploadImageOrNotSupportPlaceholder(externalResource);
            receiver.sendMessage(message);
        }
    }

}
