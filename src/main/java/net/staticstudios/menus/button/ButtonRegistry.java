package net.staticstudios.menus.button;

import java.util.HashMap;
import java.util.Map;

public class ButtonRegistry {
    private static final Map<String, Button> REGISTRY = new HashMap<>();

    public static void register(String namespace, String id, Button button) {
        REGISTRY.put(namespace + ":" + id, button);
    }

    public static Button getButton(String namespaceId) {
        return REGISTRY.get(namespaceId);
    }

    public static boolean contains(String namespace, String id) {
        return REGISTRY.containsKey(namespace + ":" + id);
    }
}
