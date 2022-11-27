package hundun.miraifleet.arknights.amiya.botlogic.function.image;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.function.BaseFunction.UserLevelFunctionComponentConstructPack;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import xmmt.dituon.share.ImageSynthesis;

import java.awt.image.BufferedImage;

public class AmiyaImageFunction extends BaseFunction {

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
                "AmiyaImageFunction"
                );
        this.commandComponent = new CompositeCommandFunctionComponent();
    }

    public AmiyaImageFunction lazyInitSharedFunction(SharedPetFunction petFunction) {
        this.petFunction = petFunction;
        return this;
    }

    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {
        public CompositeCommandFunctionComponent() {
            super(plugin, botLogic, new UserLevelFunctionComponentConstructPack(characterName, functionName));
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
