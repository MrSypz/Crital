package sypztep.crital.common.data;

import java.util.HashMap;
import java.util.Map;

public class CritOverhaulData {
    private final Map<String, CritOverhaulEntry> items = new HashMap<>();

    public Map<String, CritOverhaulEntry> getItems() {
        return items;
    }
}

