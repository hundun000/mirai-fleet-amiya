package hundun.miraifleet.arknights.amiya.botlogic.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/12/08
 */
@Data
public class ListenConfig {
    NudgeReply nudgeReply = NudgeReply.RANDOM_FACE;
    Map<String, List<String>> listens = new HashMap<>(0);
    
    public enum NudgeReply {
        RANDOM_FACE,
        PATPAT
    }
}
