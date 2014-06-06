package williewillus.BugfixMod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;
import williewillus.BugfixMod.patchers.*;

import java.io.File;


/**
 * Created by Vincent on 3/10/14.
 */
public class BugfixModClassTransformer implements IClassTransformer {

    private boolean hasInit = false;
    public static BugfixModClassTransformer instance;
    private BugFixModSettings settings;
    public File settingsFile;
    private boolean isObf;

    public BugfixModClassTransformer() {
        if (instance != null) {
            throw new RuntimeException("Only one transformer may exist!");
        } else {
            instance = this;

        }

    }

    public void initialize(Boolean par1isObf) {

        if (!hasInit) {
            isObf = par1isObf;

            Configuration config = new Configuration(settingsFile);
            config.load();
            settings = new BugFixModSettings();

            settings.ArrowFixEnabled = config.get("COMMON", "ArrowFixEnabled", true).getBoolean(true);
            settings.SnowballFixEnabled = config.get("COMMON", "SnowballFixEnabled", true).getBoolean(true);
            settings.ChickenLureFixEnabled = config.get("COMMON", "ChickenLureFixEnabled", true).getBoolean(true);


            settings.LinkCommandEnabled = config.get("TWEAKS", "LinkCommandEnabled", false).getBoolean(false);
            settings.VillageAnvilTweakEnabled = config.get("TWEAKS","VillageAnvilTweakEnabled",false).getBoolean(false);

            settings.XPFixEnabled = config.get("CLIENT", "XPFixEnabled", true).getBoolean(true);
            settings.ChatOpacityFixEnabled = config.get("CLIENT", "ChatOpacityFixEnabled", true).getBoolean(true);
            settings.ToolDesyncFixEnabled = config.get("CLIENT", "ToolDesyncFixEnabled", false).getBoolean(false);
            settings.HeartFlashFixEnabled = config.get("CLIENT", "HeartFlashFixEnabled", true).getBoolean(true);

            config.save();

            MappingRegistry.init(par1isObf);
            hasInit = true;
        }
    }


    public byte[] transform(String par1, String par2, byte[] bytes) {
        if (par1.equals("net.minecraft.entity.projectile.EntityArrow") || par1.equals("xo")) {
            if (settings.ArrowFixEnabled) {
                return ArrowFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ArrowFix disabled, skipping patch.");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.client.network.NetHandlerPlayClient") || par1.equals("biv")) {
            if (settings.XPFixEnabled) {
                return XPFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("XPFix disabled, skipping patch.");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.entity.player.EntityPlayer") || par1.equals("xl")) {
            if (settings.SnowballFixEnabled) {
                return SnowballFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("SnowballFix disabled, skipping patch.");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.client.gui.GuiNewChat") || par1.equals("bao")) {
            if (settings.ChatOpacityFixEnabled) {
                return ChatOpacityFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ChatOpacityFix disabled, skipping patch");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.entity.passive.EntityChicken") || par1.equals("us")) {
            if (settings.ChickenLureFixEnabled) {
                return ChickenLureFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ChickenLureFix disabled, skipping patch");
                return bytes;
            }
        }

        if (par1.equals("net.minecraft.world.gen.structure.StructureVillagePieces$House2") || par1.equals("auk")) {
            if (settings.VillageAnvilTweakEnabled) {
                return VillageAnvilTweakPatcher.patch(bytes, isObf);
            } else {
                System.out.println("VillageAnvilTweak disabled, skipping patch");
            }
        }

        if (par1.equals("net.minecraft.item.ItemTool") || par1.equals("aas")) {
            if (settings.ToolDesyncFixEnabled) {
                return ToolDesyncFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("ToolDesyncFix disabled, skipping patch");
            }
        }

        if (par1.equals("net.minecraft.client.entity.EntityClientPlayerMP") || par1.equals("bje")) {
            if (settings.HeartFlashFixEnabled) {
                return HeartFlashFixPatcher.patch(bytes, isObf);
            } else {
                System.out.println("HeartFlashFix disabled, skipping patch");
            }
        }

        return bytes;
    }

    public BugFixModSettings getSettings() {
        return settings;
    }

}
