package hundun.miraifleet.arknights.amiya.botlogic;

import java.util.Map;
import java.util.function.Supplier;

import hundun.miraifleet.arknights.amiya.botlogic.function.image.AmiyaImageFunction;
import hundun.miraifleet.framework.core.function.AbstractAllCompositeCommandProxy;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.starter.botlogic.function.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.ReminderFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboViewFormat;
import hundun.miraifleet.music.share.function.music.MusicMidiFunction;
import hundun.miraifleet.music.share.function.music.search.MusicSearchCompositeFunction;
import hundun.miraifleet.music.share.function.music.search.MusicSearchSimpleFunction;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.permission.Permission;
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
    
    @SubCommand("查询报时")
    public void listHourlyChatConfig(CommandSender sender) {
        botLogic.getFunction(ReminderFunction.class).getCommandComponent().listHourlyChatConfig(sender);
    }
    
    @SubCommand("查询提醒")
    public void listReminderListChatConfig(CommandSender sender) {
        botLogic.getFunction(ReminderFunction.class).getCommandComponent().listReminderListChatConfig(sender);
    }
    
    @SubCommand("debugTimerCallReminderItem")
    public void debugTimerCallReminderItem(CommandSender sender, String timeString) {
        botLogic.getFunction(ReminderFunction.class).getCommandComponent().debugTimerCallReminderItem(sender, timeString);
    }
    
    @SubCommand("删除提醒")
    public void deleteReminderListChatConfig(CommandSender sender, int id) {
        botLogic.getFunction(ReminderFunction.class).getCommandComponent().deleteReminderListChatConfig(sender, id);
    }
    
    @SubCommand("创建提醒")
    public void insertReminderListChatConfig(CommandSender sender, 
            String cornRawFomat,
            String countRawFomat,
            String text
            ) {
        botLogic.getFunction(ReminderFunction.class).getCommandComponent().insertReminderListChatConfig(sender, cornRawFomat, countRawFomat, text);
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
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listTopForUid(sender, name);
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
    public void midi(CommandSender sender, String arg, String... args) {
        botLogic.getFunction(MusicMidiFunction.class).getCommandComponent().midi(sender, arg, args);
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
