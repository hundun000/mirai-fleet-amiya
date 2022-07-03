package hundun.miraifleet.arknights.amiya.botlogic;

import hundun.miraifleet.arknights.amiya.botlogic.function.chat.AmiyaChatFunction;
import hundun.miraifleet.arknights.amiya.botlogic.function.image.AmiyaImageFunction;
import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.CharacterAdminHelperFunction;
import hundun.miraifleet.framework.starter.botlogic.function.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.ReminderFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import hundun.miraifleet.music.share.function.music.MusicMidiFunction;
import hundun.miraifleet.music.share.function.music.search.MusicSearchCompositeFunction;
import hundun.miraifleet.music.share.function.music.search.MusicSearchSimpleFunction;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class AmiyaBotLogic extends BaseJavaBotLogic {
    
    public AmiyaBotLogic(JavaPlugin plugin) {
        super(plugin, "阿米娅");
        
        
    }

    @Override
    protected void onFunctionsEnable() {
        SharedPetFunction sharedPetFunction = new SharedPetFunction(this, plugin, characterName);
        
        registerFunction(new AmiyaChatFunction(this, plugin, characterName,
                AmiyaDefaultConfigAndData.listenConfigDefaultDataSupplier(),
                AmiyaDefaultConfigAndData.nudgeConfigDefaultDataSupplier()
                )
                .lazyInitSharedFunction(sharedPetFunction)
                );
        
        registerFunction(new AmiyaImageFunction(this, plugin, characterName)
                .lazyInitSharedFunction(sharedPetFunction)
                );
        
        
        registerFunction(new RepeatFunction(this, plugin, characterName));
        
        registerFunction(new WeiboFunction(this, plugin, characterName,          
                AmiyaDefaultConfigAndData.weiboConfigDefaultDataSupplier())
                );
        
        registerFunction(new ReminderFunction(this, plugin, characterName, 
                AmiyaDefaultConfigAndData.reminderListDefaultDataSupplier(),
                AmiyaDefaultConfigAndData.hourlyChatConfigDefaultDataSupplier()
                ));
        
        registerFunction(new DriveFunction(this, plugin, characterName)
                );
        
        registerFunction(new CharacterHelpFunction(this, plugin, characterName)
                );
        
        registerFunction(new MusicMidiFunction(this, plugin, characterName));
        
        registerFunction(new CharacterAdminHelperFunction(this, plugin, characterName));
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
        
    }


}
