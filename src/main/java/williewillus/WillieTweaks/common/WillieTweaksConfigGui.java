package williewillus.WillieTweaks.common;

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by Vincent on 6/26/2014.
 */
public class WillieTweaksConfigGui extends GuiConfig {
    public WillieTweaksConfigGui(GuiScreen parent) {
        super(parent,
                new ConfigElement(WillieTweaks.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                "TestMod", false, false, GuiConfig.getAbridgedConfigPath(WillieTweaks.config.toString())
        );
    }
}
