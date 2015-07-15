package williewillus.BugfixMod.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Created by Vincent on 7/9/2015.
 */
public class CommonEvents {
    @SubscribeEvent
    public void mobDing(LivingHurtEvent evt) {
        if (evt.entityLiving instanceof IMob && "arrow".equals(evt.source.getDamageType())) {
            EntityDamageSourceIndirect src = ((EntityDamageSourceIndirect) evt.source);
            if (src.getEntity() instanceof EntityPlayerMP) {
                ((EntityPlayerMP) src.getEntity()).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
            }
        }
    }
}
