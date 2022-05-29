package hundun.miraifleet.arknights.amiya.botlogic;

import hundun.miraifleet.arknights.amiya.botlogic.function.chat.AmiyaChatFunction;
import hundun.miraifleet.arknights.amiya.botlogic.function.image.AmiyaImageFunction;
import hundun.miraifleet.arknights.amiya.botlogic.function.share.SharedPetFunction;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.ReminderFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.music.share.function.music.MusicCompositeFunction;
import hundun.miraifleet.music.share.function.music.MusicSimpleFunction;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class AmiyaBotLogic extends BaseJavaBotLogic {

    AmiyaChatFunction amiyaChatFunction;
    AmiyaImageFunction amiyaImageFunction;
    
    RepeatFunction repeatFunction;
    WeiboFunction weiboFunction;
    ReminderFunction reminderFunction;
    DriveFunction driveFunction;
    CharacterHelpFunction characterHelpFunction;
    
    MusicCompositeFunction musicCompositeFunction;
    MusicSimpleFunction musicSimpleFunction;
    
    public AmiyaBotLogic(JavaPlugin plugin) {
        super(plugin, "阿米娅");
        
        amiyaChatFunction = new AmiyaChatFunction(this, plugin, characterName,
                AmiyaDefaultConfigAndData.listenConfigDefaultDataSupplier(),
                AmiyaDefaultConfigAndData.nudgeConfigDefaultDataSupplier()
                );
        functions.add(amiyaChatFunction);
        
        amiyaImageFunction = new AmiyaImageFunction(this, plugin, characterName);
        functions.add(amiyaImageFunction);
        
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
        
        driveFunction = new DriveFunction(this, plugin, characterName);
        functions.add(driveFunction);
        
        characterHelpFunction = new CharacterHelpFunction(this, plugin, characterName);
        functions.add(characterHelpFunction);
        
        musicCompositeFunction = new MusicCompositeFunction(this, plugin, characterName);
        functions.add(musicCompositeFunction);
        musicSimpleFunction = new MusicSimpleFunction(this, plugin, characterName);
        functions.add(musicSimpleFunction);
        
        SharedPetFunction petFunction = new SharedPetFunction(this, plugin, characterName);
        amiyaChatFunction.lazyInitSharedFunction(petFunction);
        amiyaImageFunction.lazyInitSharedFunction(petFunction);
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
    }


}
