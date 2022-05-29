package hundun.miraifleet.arknights.amiya;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.arknights.amiya.botlogic.AmiyaBotLogic;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class AmiyaPlugin extends JavaPlugin {
public static final AmiyaPlugin INSTANCE = new AmiyaPlugin(); 
    
    AmiyaBotLogic botLogic;
    
    public AmiyaPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "hundun.fleet.amiya",
                "0.5.1"
            )
            .build());
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        
    }
    
    @Override
    public void onEnable() {
        botLogic = new AmiyaBotLogic(this);
        botLogic.onBotLogicEnable();
    }
    
    @Override
    public void onDisable() {
        botLogic.onDisable();
        // 由GC回收即可
        botLogic = null;
    }
    
    
    private static void foo(String arg) {
        
    }
    
    private static void test() {
        String myArg1 = "hi";
        String myArg2 = "hi";
        foo(myArg1);
        foo(myArg2);
        foo(myArg1);

        if (true) {
            foo(myArg1);
            foo(myArg2);
        }
    }
}
