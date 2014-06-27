package williewillus.WillieTweaks.common;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import williewillus.WillieTweaks.common.asm.ASMClassTransformer;

/**
 * Created by Vincent on 6/26/2014.
 */

@Mod(modid = Reference.WILLIETWEAKSMODID, name = Reference.WILLIETWEAKSMODID, version = Reference.VERSION, guiFactory = Reference.GUIFACTORY)
public class WillieTweaks {
    @Mod.Instance
    public static WillieTweaks instance;

    public static Configuration config = ASMClassTransformer.instance.config; // Hackiness...makes (should be safe) assumption that coremod has already loaded

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        if (config != null) {
            syncConfig();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        FMLCommonHandler.instance().bus().register(instance);
    }

    private void syncConfig() {

        // ASM Configs are only synced here for subsequent startups...there is NO effect on the currently running game!
        WillieTweaksSettings settings = ASMClassTransformer.instance.settings;
        config.addCustomCategoryComment("asm_common", "Common ASM bugfixes");
        settings.ArrowFixEnabled = config.get("asm_common", "ArrowFixEnabled", true).getBoolean(true);
        settings.ChickenLureFixEnabled = config.get("asm_common", "ChickenLureFixEnabled", true).getBoolean(true);
        settings.ItemHopperBounceFixEnabled = config.get("asm_common", "ItemHopperBounceFixEnabled", false).getBoolean(false);
        settings.ItemStairBounceFixEnabled = config.get("asm_common", "ItemStairBounceFixEnabled", false).getBoolean(false);
        settings.SnowballFixEnabled = config.get("asm_common", "SnowballFixEnabled", true).getBoolean(true);

        config.addCustomCategoryComment("asm_tweaks", "ASM tweaks (not bugfixes)");
        settings.VillageAnvilTweakEnabled = config.get("asm_tweaks", "VillageAnvilTweakEnabled", false).getBoolean(false);

        config.addCustomCategoryComment("asm_client", "Clientside ASM bugfixes");
        settings.ArrowDingTweakEnabled = config.get("asm_client", "ArrowDingTweakEnabled", false).getBoolean(false);
        settings.ChatOpacityFixEnabled = config.get("asm_client", "ChatOpacityFixEnabled", true).getBoolean(true);
        settings.HeartFlashFixEnabled = config.get("asm_client", "HeartFlashFixEnabled", true).getBoolean(true);
        settings.ToolDesyncFixEnabled = config.get("asm_client", "ToolDesyncFixEnabled", false).getBoolean(false);
        settings.XPFixEnabled = config.get("asm_client", "XPFixEnabled", true).getBoolean(true);

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent evtArgs) {
        if ("WillieTweaks".equals(evtArgs.modID)) {
            syncConfig();
        }
    }
}
