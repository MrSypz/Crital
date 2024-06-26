package sypztep.crital.common.init;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import sypztep.crital.client.payload.CritSyncPayload;
import sypztep.crital.client.payload.GrinderPayloadS2C;
import sypztep.crital.client.payload.QualityGrinderPayloadS2C;
import sypztep.crital.common.payload.GrindQualityPayloadC2S;
import sypztep.crital.common.payload.GrinderPayloadC2S;

public class ModPayload {
    public static void init() {
        PayloadTypeRegistry.playS2C().register(CritSyncPayload.ID, CritSyncPayload.CODEC); // Server to Client
        PayloadTypeRegistry.playS2C().register(GrinderPayloadS2C.ID, GrinderPayloadS2C.CODEC); // Server to Client
        PayloadTypeRegistry.playS2C().register(QualityGrinderPayloadS2C.ID, QualityGrinderPayloadS2C.CODEC); // Server to Client
        initClient();
    }
    public static void initClient() {
        PayloadTypeRegistry.playC2S().register(GrinderPayloadC2S.ID, GrinderPayloadC2S.CODEC); // Client to Server
        PayloadTypeRegistry.playC2S().register(GrindQualityPayloadC2S.ID, GrindQualityPayloadC2S.CODEC); // Client to Server
    }
}
