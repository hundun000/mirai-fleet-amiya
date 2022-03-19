package hundun.miraifleet.arknights.amiya.botlogic.function;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction.SessionData;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.config.HourlyChatConfig;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.BuiltInCommands;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.internal.command.CommandManagerImpl;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/08/09
 */
@AsListenerHost
public class AmiyaChatFunction extends BaseFunction<Void> {

    Random rand = new Random();
    private String cannotRelaxTalk = "博士，您还有许多事情需要处理。现在还不能休息哦。";
    private String canRelaxTalk = "博士，辛苦了！累了的话请休息一会儿吧。";
    ExternalResource cannotRelaxExternalResource;
    ExternalResource canRelaxExternalResource;
    ExternalResource damedaneVoiceExternalResource;
    int forceTodayIsHoliday = -1;
    int forceTodayIsWorkday = -1;
    List<ExternalResource> selfNudgeFaces = new ArrayList<>();
    Map<Long, ExternalResource> otherNudgeFaces = new HashMap<>();
    
    Map<String, List<String>> listenConfigData = new HashMap<>();
    
    public AmiyaChatFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName, 
            Supplier<Map<String, ListenConfig>> supplier
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "AmiyaChatFunction", 
                true,
                null
                );
        initExternalResource();
        SingletonDocumentRepository<ListenConfig> listenConfigRepository = new SingletonDocumentRepository<>(
                plugin, 
                resolveFunctionConfigFile("ListenConfig.json"), 
                ListenConfig.class,
                supplier
                );
        ListenConfig listenConfig = listenConfigRepository.findSingleton();
        if (listenConfig != null && listenConfig.getListens() != null) {
            listenConfig.getListens().entrySet().forEach(entry -> {
                String[] keys = entry.getKey().split("\\|");
                for (String key : keys) {
                    listenConfigData.put(key, entry.getValue());
                }
            });
            
        }
        
    }
    
    List<String> validEnds = Arrays.asList("gif", "png", "bmp", "jpg");
    private boolean validAsImage(File file) {
        boolean valid = false;
        for (String validEnd : validEnds) {
            if (file.getName().endsWith(validEnd) || file.getName().endsWith(validEnd.toUpperCase())) {
                valid = true;
            }
        }
        return valid;
    }
    
    
    private void initExternalResource() {
        try {
            cannotRelaxExternalResource = ExternalResource.create(plugin.resolveDataFile(functionName + File.separator + "cannotRelax.png"));
            canRelaxExternalResource = ExternalResource.create(plugin.resolveDataFile(functionName + File.separator + "canRelax.png"));
            damedaneVoiceExternalResource = ExternalResource.create(plugin.resolveDataFile(functionName + File.separator + "damedane.amr"));
            
            File facesFolder = plugin.resolveDataFile(functionName + File.separator + "selfNudgeFaces");
            if (facesFolder.exists() && facesFolder.isDirectory()) {
                for (File faceFile : facesFolder.listFiles()) {
                    if (!validAsImage(faceFile)) {
                        continue;
                    }
                    selfNudgeFaces.add(ExternalResource.create(faceFile));
                }
                log.info("load faces size = " + selfNudgeFaces.size());
            }
            
            File otherNudgeFacesFolder = plugin.resolveDataFile(functionName + File.separator + "otherNudgeFaces");
            if (otherNudgeFacesFolder.exists() && otherNudgeFacesFolder.isDirectory()) {
                for (File faceFile : otherNudgeFacesFolder.listFiles()) {
                    if (!validAsImage(faceFile)) {
                        continue;
                    }
                    try {
                        String idPart = faceFile.getName().substring(0, faceFile.getName().indexOf("."));
                        Long id = Long.valueOf(idPart);
                        otherNudgeFaces.put(id, ExternalResource.create(faceFile));
                        log.info("load specialNudgeFace of id = " + id);
                    } catch (Exception e) {
                        log.warning("无法作为specialNudgeFace：" + faceFile.getAbsolutePath());
                    }
                    
                }
            }
        } catch (Exception e) {
            log.error("open cannotRelaxImage error: " + e.getMessage());
        }
    }
    
    private boolean canRelax() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int weekDay = now.getDayOfWeek().getValue();
        
        if (now.getDayOfYear() == forceTodayIsHoliday) {
            return true;
        }
        
        if (now.getDayOfYear() == forceTodayIsWorkday || weekDay < 6) {
            if (hour < 9 || hour >= 17) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

        
    }
    
    
//    @SubCommand("测试闲聊")
//    public void chatFromCommand(CommandSender sender, String testText) {
//        if (!checkCosPermission(sender)) {
//            return;
//        }
//        chat(new FunctionReplyReceiver(sender, plugin.getLogger()), testText);
//    }
    
    
    @EventHandler
    public void onMessage(@NotNull AbstractMessageEvent event) throws Exception { 
        if (!checkCosPermission(event)) {
            return;
        }
        chat(new FunctionReplyReceiver(event.getSubject(), plugin.getLogger()), event.getMessage().contentToString());
    }
    
    @EventHandler
    public void onMessage(@NotNull NudgeEvent event) throws Exception { 
        if (!checkCosPermission(event)) {
            return;
        }
        long senderId = event.getFrom().getId();
        long targetId = event.getTarget().getId();
        Contact contact = event.getSubject();
        Image image = null;
        if (event.getBot().getId() == targetId) {
            int index = rand.nextInt(selfNudgeFaces.size());
            log.info("use selfNudgeFaces index = " + index);
            image = contact.uploadImage(selfNudgeFaces.get(index));
        } else if (otherNudgeFaces.containsKey(targetId)) {
            ExternalResource specialNudgeFace = otherNudgeFaces.get(targetId);
            image = contact.uploadImage(specialNudgeFace);
        } else {
            // nudge-target is normal member, do nothing
            return;
        }
        
        if (image != null) {
            contact.sendMessage( 
                    new At(senderId)
                    .plus(image)
                    );
        } else {
            contact.sendMessage( 
                    new At(senderId)
                    .plus(NOT_SUPPORT_RESOURCE_PLACEHOLDER)
                    );
        }
    }


    private void chat(FunctionReplyReceiver subject, String message) {
        
        if (message.contains("下班")) {
            boolean canRelax = canRelax();
            if (canRelax) {
                Image image = subject.uploadImage(canRelaxExternalResource);
                if (image != null) {
                    subject.sendMessage(
                            new PlainText(canRelaxTalk)
                            .plus(image)
                            );
                } else {
                    subject.sendMessage(
                            new PlainText(canRelaxTalk)
                            .plus(NOT_SUPPORT_RESOURCE_PLACEHOLDER)
                            );
                }
            } else {
                Image image = subject.uploadImage(cannotRelaxExternalResource);
                if (image != null) {
                    subject.sendMessage(
                            new PlainText(cannotRelaxTalk)
                            .plus(image)
                            );
                } else {
                    subject.sendMessage(
                            new PlainText(cannotRelaxTalk)
                            .plus(NOT_SUPPORT_RESOURCE_PLACEHOLDER)
                            );
                }
            }
        } else if (message.contains("damedane")) {
            Audio voice = subject.uploadVoice(damedaneVoiceExternalResource);
            if (voice != null) {
                subject.sendMessage(
                        new PlainText("")
                        .plus(voice)
                        );
            } else {
                subject.sendMessage(
                        new PlainText(NOT_SUPPORT_RESOURCE_PLACEHOLDER)
                        );
            }
        } else if (listenConfigData.containsKey(message)){
            List<String> candidates = listenConfigData.get(message);
            int index = (int) (Math.random() * candidates.size());
            String reply = candidates.get(index);
            log.info("use listenConfig candidates index = " + index);
            subject.sendMessage(
                    reply
                    );
        }
    }


    @Override
    public AbstractCommand provideCommand() {
        return null;
    }
    

}
