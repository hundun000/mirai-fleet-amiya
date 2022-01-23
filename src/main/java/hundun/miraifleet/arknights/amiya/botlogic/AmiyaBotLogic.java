package hundun.miraifleet.arknights.amiya.botlogic;

import hundun.miraifleet.arknights.amiya.botlogic.function.AmiyaChatFunction;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.MiraiCodeFunction;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
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
    
    MiraiCodeFunction miraiCodeFunction;
    RepeatFunction repeatFunction;
    
    WeiboFunction weiboFunction;
    ReminderFunction reminderFunction;
    DriveFunction driveFunction;
    
    AllCompositeCommandProxy allCompositeCommandProxy;
    
    public AmiyaBotLogic(JvmPlugin plugin) {
        super(plugin, "阿米娅");
        
        amiyaChatFunction = new AmiyaChatFunction(this, plugin, characterName,
                AmiyaDefaultConfigAndData.listenConfigDefaultDataSupplier()
                );
        functions.add(amiyaChatFunction);
        
        repeatFunction = new RepeatFunction(this, plugin, characterName);
        functions.add(repeatFunction);
        
        weiboFunction = new WeiboFunction(this, plugin, characterName, 
                AmiyaDefaultConfigAndData.weiboConfigDefaultDataSupplier());
        functions.add(weiboFunction);
        
        reminderFunction = new ReminderFunction(this, plugin, characterName, 
                AmiyaDefaultConfigAndData.reminderListDefaultDataSupplier(),
                AmiyaDefaultConfigAndData.hourlyChatConfigDefaultDataSupplier()
                );
        functions.add(reminderFunction);
        
        miraiCodeFunction = new MiraiCodeFunction(this, plugin, characterName);
        functions.add(miraiCodeFunction);
        
        driveFunction = new DriveFunction(this, plugin, characterName);
        functions.add(driveFunction);
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
    }
    
    @Override
    public void onBotLogicEnable() {
        super.onBotLogicEnable();
        
        CommandManager.INSTANCE.registerCommand(allCompositeCommandProxy, false);
    }
    

}
