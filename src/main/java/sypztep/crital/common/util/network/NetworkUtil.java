package sypztep.crital.common.util.network;

import net.minecraft.client.MinecraftClient;

public interface NetworkUtil {
    MinecraftClient client();
    default void context(Runnable r) {
        client().execute(r);
    }
}
