package hundun.miraifleet.arknights.amiya.botlogic.function.chat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.arknights.amiya.botlogic.function.chat.NudgeConfig.NudgeReply;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/08/09
 */
@AsListenerHost
public class AmiyaChatFunction extends BaseFunction {
    
    SingletonDocumentRepository<ListenConfig> listenConfigRepository;
    SingletonDocumentRepository<NudgeConfig> nudgeConfigRepository;
    Random rand = new Random();
    
    // ------ 下班 ------
    private String cannotRelaxTalk = "博士，您还有许多事情需要处理。现在还不能休息哦。";
    private String canRelaxTalk = "博士，辛苦了！累了的话请休息一会儿吧。";
    File cannotRelaxExternalResource;
    File canRelaxExternalResource;
    int forceTodayIsHoliday = -1;
    int forceTodayIsWorkday = -1;
    // ------ damedane ------
    File damedaneVoiceExternalResource;
    // ------ nudge ------
    List<File> selfNudgeFaces = new ArrayList<>();
    Map<Long, File> otherNudgeFaces = new HashMap<>();
    // ------ 海猫all ------
    private BufferedImage oceanCatAll;
    // ------ listen ------
    Map<String, List<String>> listenConfigData = new LinkedHashMap<>();
    
    private SharedPetFunction petFunction;
    public AmiyaChatFunction lazyInitSharedFunction(SharedPetFunction petFunction) {
        this.petFunction = petFunction;
        return this;
    }
    
    public AmiyaChatFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName, 
            Supplier<ListenConfig> supplier,
            Supplier<NudgeConfig> supplierNudgeConfig
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "AmiyaChatFunction"
                );
        this.listenConfigRepository = new SingletonDocumentRepository<>(
                plugin, 
                resolveFunctionConfigFile("ListenConfig.json"), 
                ListenConfig.class,
                supplier
                );
        this.nudgeConfigRepository = new SingletonDocumentRepository<>(
                plugin, 
                resolveFunctionConfigFile("NudgeConfig.json"), 
                NudgeConfig.class,
                supplierNudgeConfig
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
//        try {
//            patPatCoreKt = PatPatCoreKt.INSTANCE;
//        } catch (Error e) {
//            patPatCoreKt = null;
//            log.error("cannot init patPatCoreKt:" + e.getMessage());
//        }
        initExternalResource();
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
            cannotRelaxExternalResource = plugin.resolveDataFile(functionName + File.separator + "cannotRelax.png");
            canRelaxExternalResource = plugin.resolveDataFile(functionName + File.separator + "canRelax.png");
            damedaneVoiceExternalResource = plugin.resolveDataFile(functionName + File.separator + "damedane.amr");
            
            File facesFolder = plugin.resolveDataFile(functionName + File.separator + "selfNudgeFaces");
            if (facesFolder.exists() && facesFolder.isDirectory()) {
                for (File faceFile : facesFolder.listFiles()) {
                    if (!validAsImage(faceFile)) {
                        continue;
                    }
                    selfNudgeFaces.add(faceFile);
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
                        otherNudgeFaces.put(id, faceFile);
                        log.info("load specialNudgeFace of id = " + id);
                    } catch (Exception e) {
                        log.warning("无法作为specialNudgeFace：" + faceFile.getAbsolutePath());
                    }
                    
                }
            }
            
//            for (int i = 0; i < PATPAT_HANDS_SIZE; i++) {
//                patpatHandFiles[i] = (resolveFunctionDataFile("patpat/img" + i + ".png"));
//            }
            try (InputStream stream = new FileInputStream(resolveFunctionDataFile("patpat/all.png"))) {
                oceanCatAll = ImageIO.read(stream);
            }
            
        } catch (Exception e) {
            log.error("initExternalResource error: " + e.getMessage());
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
        ListenConfig listenConfig = listenConfigRepository.findSingleton();
        NudgeConfig nudgeConfig = nudgeConfigRepository.findSingleton();
        if (listenConfig != null && nudgeConfig != null) {
            long senderId = event.getFrom().getId();
            long targetId = event.getTarget().getId();
            
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(event.getSubject(), log);
            Message message;
            if (event.getBot().getId() == targetId) {
                if (nudgeConfig.getNudgeReply() == NudgeReply.RANDOM_FACE) {
                    int index = rand.nextInt(selfNudgeFaces.size());
                    log.info("use selfNudgeFaces index = " + index);
                    message = receiver.uploadImageAndCloseOrNotSupportPlaceholder(ExternalResource.create(selfNudgeFaces.get(index)));
                } else if (nudgeConfig.getNudgeReply() == NudgeReply.PATPAT) {
                    message = nudgeReplyByPatPat(event);
                } else {
                    message = new PlainText("未知的回应方式：" + nudgeConfig.getNudgeReply());
                }
            } else if (otherNudgeFaces.containsKey(targetId)) {
                ExternalResource specialNudgeFace = ExternalResource.create(otherNudgeFaces.get(targetId));
                message = receiver.uploadImageAndCloseOrNotSupportPlaceholder(specialNudgeFace);
            } else {
                // nudge-target is normal member, do nothing
                return;
            }
            
            receiver.sendMessage( 
                        new At(senderId)
                        .plus(message)
                        );
        } else {
            log.warning("配置缺失");
        }
    }
    
    private Message nudgeReplyByPatPat(NudgeEvent event) {
        BufferedImage to = petFunction.userAvatarOrDefaultAvatar(event);
        
        if (to != null) {
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(event.getSubject(), log);
            return patpatCore(receiver, to);
        }
        return new PlainText("获取对象用户头像失败");
    }


    private void chat(FunctionReplyReceiver subject, String message) {
        
        if (message.contains("下班")) {
            boolean canRelax = canRelax();
            if (canRelax) {
                Image image = subject.uploadImageAndClose(ExternalResource.create(canRelaxExternalResource));
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
                Image image = subject.uploadImageAndClose(ExternalResource.create(cannotRelaxExternalResource));
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
            Audio voice = subject.uploadVoiceAndClose(ExternalResource.create(damedaneVoiceExternalResource));
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
        } else if (message.contains("海猫") && (message.contains("all") || message.contains("All") || message.contains("ALL"))) {
            var to = oceanCatAll;
            var reply = patpatCore(subject, to);
            subject.sendMessage(
                    reply
                    );
        } else {
            for (String pattern : listenConfigData.keySet()) {
                if (message.contains(pattern)) {
                    List<String> candidates = listenConfigData.get(pattern);
                    int index = (int) (Math.random() * candidates.size());
                    String reply = candidates.get(index);
                    log.info("use listenConfig candidates index = " + index);
                    subject.sendMessage(
                            reply
                            );
                    break;
                }
            }
        } 
    }


    @Override
    public AbstractCommand provideCommand() {
        return null;
    }
    
    private Message patpatCore(FunctionReplyReceiver receiver, BufferedImage to) {
        var resultFile = petFunction.petService(null, to, "patpat");
        if (resultFile != null) {
            ExternalResource externalResource = ExternalResource.create(resultFile).toAutoCloseable();
            Message message = receiver.uploadImageAndCloseOrNotSupportPlaceholder(externalResource);
            return message;
        } else {
            return new PlainText("patpatCore失败");
        }
    }

}
