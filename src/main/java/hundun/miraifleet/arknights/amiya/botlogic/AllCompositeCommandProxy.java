package hundun.miraifleet.arknights.amiya.botlogic;

import hundun.miraifleet.arknights.amiya.botlogic.function.image.AmiyaImageFunction;
import hundun.miraifleet.framework.core.function.AbstractAllCompositeCommandProxy;
import hundun.miraifleet.framework.starter.botlogic.function.character.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.music.share.function.music.MusicMidiFunction;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;

/**
 * 目前只能用唯一的CompositeCommand注册所有SubCommand，未来改为分别注册。
 * https://github.com/mamoe/mirai-console/issues/397
 * @author hundun
 * Created on 2021/08/11
 */
public class AllCompositeCommandProxy extends AbstractAllCompositeCommandProxy<AmiyaBotLogic> {

    
    
    public AllCompositeCommandProxy(
            AmiyaBotLogic botLogic, 
            JvmPlugin plugin,
            String characterName
            ) {
        super(botLogic, plugin, characterName);
    }
    
    @SubCommand("刷新微博订阅")
    public void updateAndGetUserInfoCache(CommandSender sender) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().updateAndGetUserInfoCache(sender);
    }
    
    @SubCommand("微博订阅")
    public void listListen(CommandSender sender) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listListen(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopSummary(CommandSender sender) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listTopSummary(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopForUid(CommandSender sender, String name) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listTopForName(sender, name);
    }

    @SubCommand("立刻私聊")
    public void chat(CommandSender sender, User target, String messageCode) {
        botLogic.getFunction(DriveFunction.class).getCommandComponent().chat(sender, target, messageCode);
    }
    
    @SubCommand("立刻群聊")
    public void chat(CommandSender sender, Group target, String messageCode) {
        botLogic.getFunction(DriveFunction.class).getCommandComponent().chat(sender, target, messageCode);
    }

    @SubCommand("help")
    public void help(CommandSender sender) {
        botLogic.getFunction(CharacterHelpFunction.class).getCommandComponent().help(sender);
    }
    
//    @SubCommand("音乐")
//    public void help(CommandSender sender, String arg, String... args) {
//        botLogic.getFunction(MusicSearchSimpleFunction.class).getCommandComponent().fromCommand(sender, arg, args);
//    }
//    
//    @SubCommand("QQ音乐")
//    public void searchQQ(CommandSender sender, String arg, String... args) {
//        botLogic.getFunction(MusicSearchCompositeFunction.class).getCommandComponent().searchQQ(sender, arg, args);
//    }
//    
//    @SubCommand({"网易", "网易云"})
//    public void searchNetEase(CommandSender sender, String arg, String... args) {
//        botLogic.getFunction(MusicSearchCompositeFunction.class).getCommandComponent().searchNetEase(sender, arg, args);
//    }
    
    @SubCommand({"midi"})
    public void midi(CommandSender sender, @Name(value = "midiCode") String midiCode) {
        botLogic.getFunction(MusicMidiFunction.class).getCommandComponent().midi(sender, midiCode);
    }
    
    @SubCommand("玩")
    public void playPhoneTest(CommandSender sender, User target) {
        botLogic.getFunction(AmiyaImageFunction.class).getCommandComponent().playPhone(sender, target);
    }
    
    @SubCommand("玩test")
    public void playPhoneTest(CommandSender sender) {
        botLogic.getFunction(AmiyaImageFunction.class).getCommandComponent().playPhoneTest(sender);
    }
}
