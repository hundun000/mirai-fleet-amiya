package hundun.miraifleet.arknights.amiya.botlogic;

import java.util.Map;
import java.util.function.Supplier;

import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboViewFormat;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * 目前只能用唯一的CompositeCommand注册所有SubCommand，未来改为分别注册。
 * https://github.com/mamoe/mirai-console/issues/397
 * @author hundun
 * Created on 2021/08/11
 */
public class AllCompositeCommandProxy extends CompositeCommand {

    
    AmiyaBotLogic botLogic;
    
    
    public AllCompositeCommandProxy(
            AmiyaBotLogic botLogic, 
            JvmPlugin plugin,
            String characterName
            ) {
        super(plugin, characterName, new String[]{}, "我是" + characterName, plugin.getParentPermission(), CommandArgumentContext.EMPTY);
        this.botLogic = botLogic;
    }
    
    @SubCommand("查询报时")
    public void listHourlyChatConfig(CommandSender sender) {
        botLogic.reminderFunction.listHourlyChatConfig(sender);
    }
    
    @SubCommand("查询提醒")
    public void listReminderListChatConfig(CommandSender sender) {
        botLogic.reminderFunction.listReminderListChatConfig(sender);
    }
    
    @SubCommand("删除提醒")
    public void deleteReminderListChatConfig(CommandSender sender, int id) {
        botLogic.reminderFunction.deleteReminderListChatConfig(sender, id);
    }
    
    @SubCommand("创建提醒")
    public void insertReminderListChatConfig(CommandSender sender, 
            String cornRawFomat,
            String countRawFomat,
            String text
            ) {
        botLogic.reminderFunction.insertReminderListChatConfig(sender, cornRawFomat, countRawFomat, text);
    }
    
    
    @SubCommand("微博订阅")
    public void listListen(CommandSender sender) {
        botLogic.weiboFunction.listListen(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopSummary(CommandSender sender) {
        botLogic.weiboFunction.listTopSummary(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopForUid(CommandSender sender, String name) {
        botLogic.weiboFunction.listTopForUid(sender, name);
    }

    @SubCommand("驾驶")
    public void chat(CommandSender sender, String messageCode) {
        botLogic.driveFunction.chat(sender, messageCode);
    }

}
