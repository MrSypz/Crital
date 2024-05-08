package sypztep.crital.common.api.network;

import net.minecraft.client.MinecraftClient;

@FunctionalInterface
public interface NetworkClientUtil {
    MinecraftClient client();
    default void context(Runnable r) {
        client().execute(r);
    }
}
