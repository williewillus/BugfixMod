package williewillus.BugfixMod.mod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;

/**
 * For non-ASM bugfixes
 */
@Mod(name = "BugfixMod", modid = "BugfixMod", version = "@VERSION@")
public class BFMProper {
    @Mod.Instance
    public static BFMProper instance;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent evt) {
        FMLCommonHandler.instance().bus().register(new CommonEvents());
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        if (evt.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(new ClientEvents());
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }
    }
}
