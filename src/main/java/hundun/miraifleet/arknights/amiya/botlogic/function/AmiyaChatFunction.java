package hundun.miraifleet.arknights.amiya.botlogic.function;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.arknights.amiya.botlogic.function.NudgeConfig.NudgeReply;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.helper.file.CacheableFileHelper;
import hundun.miraifleet.framework.core.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction.SessionData;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.config.HourlyChatConfig;
import hundun.miraifleet.image.share.function.PatPatCoreKt;
import hundun.miraifleet.image.share.function.UtilsKt;
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
import net.mamoe.mirai.message.data.Message;
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
    
    SingletonDocumentRepository<ListenConfig> listenConfigRepository;
    SingletonDocumentRepository<NudgeConfig> nudgeConfigRepository;
    Random rand = new Random();
    private final PatPatCoreKt patPatCoreKt = PatPatCoreKt.INSTANCE;
    private static final int PATPAT_HANDS_SIZE = 5;
    private final File[] patpatHandFiles = new File[PATPAT_HANDS_SIZE];
    private final CacheableFileHelper cacheableFileHelper;
    
    // ------ 下班 ------
    private String cannotRelaxTalk = "博士，您还有许多事情需要处理。现在还不能休息哦。";
    private String canRelaxTalk = "博士，辛苦了！累了的话请休息一会儿吧。";
    ExternalResource cannotRelaxExternalResource;
    ExternalResource canRelaxExternalResource;
    int forceTodayIsHoliday = -1;
    int forceTodayIsWorkday = -1;
    // ------ damedane ------
    ExternalResource damedaneVoiceExternalResource;
    // ------ nudge ------
    List<ExternalResource> selfNudgeFaces = new ArrayList<>();
    Map<Long, ExternalResource> otherNudgeFaces = new HashMap<>();
    // ------ 海猫all ------
    private ExternalResource oceanCatAll;
    // ------ listen ------
    Map<String, List<String>> listenConfigData = new LinkedHashMap<>();
    
    
    public AmiyaChatFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName, 
            Supplier<Map<String, ListenConfig>> supplier,
            Supplier<Map<String, NudgeConfig>> supplierNudgeConfig
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "AmiyaChatFunction", 
                null
                );
        this.cacheableFileHelper = new CacheableFileHelper(resolveFunctionCacheFileFolder());
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
            
            for (int i = 0; i < PATPAT_HANDS_SIZE; i++) {
                patpatHandFiles[i] = (resolveFunctionDataFile("patpat/img" + i + ".png"));
            }
            oceanCatAll = ExternalResource.create(resolveFunctionDataFile("patpat/all.png"));
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
                    message = receiver.uploadImageOrNotSupportPlaceholder(selfNudgeFaces.get(index));
                } else if (nudgeConfig.getNudgeReply() == NudgeReply.PATPAT) {
                    message = nudgeReplyByPatPat(event);
                } else {
                    message = new PlainText("未知的回应方式：" + nudgeConfig.getNudgeReply());
                }
            } else if (otherNudgeFaces.containsKey(targetId)) {
                ExternalResource specialNudgeFace = otherNudgeFaces.get(targetId);
                message = receiver.uploadImageOrNotSupportPlaceholder(specialNudgeFace);
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
        var targetAvatarImage = UtilsKt.getContactOrBotAvatarImage(event.getFrom());
        if (targetAvatarImage != null) {
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(event.getSubject(), log);
            return patpat(receiver, targetAvatarImage, null);
        }
        return new PlainText("获取对象用户头像失败");
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
        } else if (message.contains("海猫") && (message.contains("all") || message.contains("All") || message.contains("ALL"))) {
            var targetAvatarImage = UtilsKt.externalResourceToSkioImage(oceanCatAll);
            patpat(subject, targetAvatarImage, "oceanCatAll");
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
    
    private Message patpat(FunctionReplyReceiver receiver, org.jetbrains.skia.Image targetAvatarImage, String cacheId) {

        ExternalResource externalResource = null;
        if (cacheId != null) {
            Function<String, InputStream> uncachedPatPatFileProvider = it -> calculatePatPatImage(targetAvatarImage);
            File patpatResultFile = cacheableFileHelper.fromCacheOrProvider(cacheId, uncachedPatPatFileProvider);
            externalResource = ExternalResource.create(patpatResultFile).toAutoCloseable();
        } else {
            try (InputStream inputStream = calculatePatPatImage(targetAvatarImage)) {
                externalResource = ExternalResource.create(inputStream).toAutoCloseable();
            } catch (Exception e) {
                log.error("patpat externalResource error: " + e.getMessage());
            }
        }
        
        Message message = receiver.uploadImageOrNotSupportPlaceholder(externalResource);
        return message;

    }

    private InputStream calculatePatPatImage(org.jetbrains.skia.Image targetAvatarImage) {
        var patpatResult = patPatCoreKt.patpat(targetAvatarImage, patpatHandFiles, 0.05);
        return new ByteArrayInputStream(patpatResult.getBytes());
    }

}
