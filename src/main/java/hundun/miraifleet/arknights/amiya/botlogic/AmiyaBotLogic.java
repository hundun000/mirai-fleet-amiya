package hundun.miraifleet.arknights.amiya.botlogic;

import hundun.miraifleet.arknights.amiya.botlogic.function.AmiyaChatFunction;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.MiraiCodeFunction;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.ReminderFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class AmiyaBotLogic extends BaseBotLogic {

    AmiyaChatFunction amiyaChatFunction;
    
    RepeatFunction repeatFunction;
    
    WeiboFunction weiboFunction;
    ReminderFunction reminderFunction;
    
    AllCompositeCommandProxy allCompositeCommandProxy;
    
    public AmiyaBotLogic(JvmPlugin plugin) {
        super(plugin, "阿米娅");
        
        amiyaChatFunction = new AmiyaChatFunction(this, plugin, characterName);
        functions.add(amiyaChatFunction);
        
        repeatFunction = new RepeatFunction(this, plugin, characterName);
        functions.add(repeatFunction);
        
        weiboFunction = new WeiboFunction(this, plugin, characterName);
        functions.add(weiboFunction);
        
        reminderFunction = new ReminderFunction(this, plugin, characterName);
        functions.add(reminderFunction);
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
    }
    
    @Override
    public void onBotLogicEnable() {
        super.onBotLogicEnable();
        
        CommandManager.INSTANCE.registerCommand(allCompositeCommandProxy, false);
    }
    

}