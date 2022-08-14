package hundun.miraifleet.arknights.amiya.botlogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import hundun.miraifleet.arknights.amiya.botlogic.function.chat.ListenConfig;
import hundun.miraifleet.arknights.amiya.botlogic.function.chat.NudgeConfig;
import hundun.miraifleet.arknights.amiya.botlogic.function.chat.NudgeConfig.NudgeReply;
import hundun.miraifleet.framework.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboConfig;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboPushFilterFlag;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboViewFormat;
import hundun.miraifleet.reminder.share.function.reminder.data.HourlyChatConfigV2;
import hundun.miraifleet.reminder.share.function.reminder.data.ReminderItem;
import hundun.miraifleet.reminder.share.function.reminder.data.ReminderList;


/**
 * @author hundun
 * Created on 2021/12/20
 */
public class AmiyaDefaultConfigAndData {
    
    static <K, V> Map<K, V> mapOf(K k1, V v1) {
        Map<K, V> map = new HashMap<>(1);
        map.put(k1, v1);
        return map;
    }
    
    static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
        Map<K, V> map = mapOf(k1, v1);
        map.put(k2, v2);
        return map;
    }
    
    static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = mapOf(k1, v1, k2, v2);
        map.put(k3, v3);
        return map;
    }
    
    public static <V> Map<String, V> toSingletonMap(V v) {
        return mapOf(SingletonDocumentRepository.THE_SINGLETON_KEY, v);
    }
    
    public static Supplier<WeiboConfig> weiboConfigDefaultDataSupplier() {
        return () -> {
            WeiboConfig weiboConfig = new WeiboConfig(
                    mapOf(
                            "6279793937", WeiboViewFormat.ALL_IMAGE, 
                            "6441489862", WeiboViewFormat.NO_IMAGE,
                            "7499841383", WeiboViewFormat.ALL_IMAGE
                            ), 
                    mapOf(
                            "7499841383", Arrays.asList(WeiboPushFilterFlag.RETWEET))
                    );
            return weiboConfig;
        };
    }
    
    public static Supplier<ReminderList> reminderListDefaultDataSupplier() {
        return () -> {
            ReminderList reminderList = new ReminderList();
            reminderList.setItems(Arrays.asList(
                    ReminderItem.Factory.create("* 0 22 ? * 1", "现在是周日晚上10点。请博士记得完成本周剿灭作战。")
                    ));
            return reminderList;
        };
    }
    
    public static Supplier<ListenConfig> listenConfigDefaultDataSupplier() {
        return () -> {
            ListenConfig listenConfig = new ListenConfig();
            listenConfig.setListens(
                    mapOf(
                            "阿米娅可爱|兔兔可爱", Arrays.asList(
                                    "虽然这可能是我一厢情愿的想法，但我希望罗德岛能成为大家的第二个故乡......",
                                    "嘿嘿，博士，悄悄告诉你一件事——我重新开始练小提琴了。",
                                    "博士，我们的脚下，是一条漫长的道路......也许这是一次没有终点的旅行，但如果是和您一起，我觉得，非常幸福。"
                                    ) 
                    )
            );
            return listenConfig;
        };
    }
    
    public static Supplier<NudgeConfig> nudgeConfigDefaultDataSupplier() {
        return () -> {
            NudgeConfig listenConfig = new NudgeConfig();
            listenConfig.setNudgeReply(NudgeReply.PATPAT);
            return listenConfig;
        };
    }
    
    public static Supplier<HourlyChatConfigV2> hourlyChatConfigDefaultDataSupplier() {
        return () -> {
            HourlyChatConfigV2 hourlyChatConfig = new HourlyChatConfigV2();
            List<ReminderItem> chatTexts = new ArrayList<>();
            chatTexts.add(ReminderItem.Factory.createHourly(0, "呜哇！？正好0点！今天是，由阿米娅来担当助理的工作呢。我不会辜负大家的。"));
            chatTexts.add(ReminderItem.Factory.createHourly(1, "凌晨一点到啦！凯尔希医生教导过我，工作的时候一定要保持全神贯注......嗯，全神贯注。"));
            chatTexts.add(ReminderItem.Factory.createHourly(9, "九点到了。罗德岛全舰正处于通常航行状态。博士，整理下航程信息吧？", "阿米娅_交谈2.amr", "九点.png"));
            chatTexts.add(ReminderItem.Factory.createHourly(10, "十点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(11, "十一点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(12, "十二点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(13, "十三点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(14, "十四点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(15, "十五点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(16, "十六点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(17, "十七点到了。博士，辛苦了！累了的话请休息一会儿吧。"));
            chatTexts.add(ReminderItem.Factory.createHourly(18, "十八点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(19, "十九点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(20, "二十点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(21, "二十一点到了。欸嘿嘿......"));
            chatTexts.add(ReminderItem.Factory.createHourly(22, "完全入夜了呢，二十二点到了。博士，您工作辛苦了。"));
            chatTexts.add(ReminderItem.Factory.createHourly(23, "二十三点到了。有什么想喝的吗，博士？"));
            
            hourlyChatConfig.setItems(chatTexts);
            return hourlyChatConfig;
        };
    }
}
