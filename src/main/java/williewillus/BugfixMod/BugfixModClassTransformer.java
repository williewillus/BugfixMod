package williewillus.BugfixMod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;
import williewillus.BugfixMod.patchers.nextGen.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Vincent on 3/10/14.
 */
public class BugfixModClassTransformer implements IClassTransformer {

    public static BugfixModClassTransformer instance;
    public File settingsFile;
    private boolean hasInit = false;
    private BugFixModSettings settings;
    private boolean isObf;
    private HashMap<String, AbstractPatcher> patchers;

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
            settings.VillageAnvilTweakEnabled = config.get("TWEAKS", "VillageAnvilTweakEnabled", false).getBoolean(false);

            settings.XPFixEnabled = config.get("CLIENT", "XPFixEnabled", true).getBoolean(true);
            settings.ChatOpacityFixEnabled = config.get("CLIENT", "ChatOpacityFixEnabled", true).getBoolean(true);
            settings.ToolDesyncFixEnabled = config.get("CLIENT", "ToolDesyncFixEnabled", false).getBoolean(false);
            settings.HeartFlashFixEnabled = config.get("CLIENT", "HeartFlashFixEnabled", true).getBoolean(true);

            config.save();
            MappingRegistry.init(par1isObf);
            setupPatchers();
            hasInit = true;
        }
    }

    public byte[] transform(String par1, String par2, byte[] bytes) {
        if (hasInit) {
            for (Map.Entry<String, AbstractPatcher> patcherEntry : patchers.entrySet()) {
                bytes = patcherEntry.getValue().patch(bytes);
            }
        }
        return bytes;
    }


    private void setupPatchers() {
        if (patchers != null) {
            System.out.println("Patcher already initialized!!");
        } else {
            patchers = new HashMap<String, AbstractPatcher>();

            if (settings.ArrowFixEnabled) {
                patchers.put("ArrowFix", new NextArrowFixPatcher(
                        "ArrowFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow"),
                        MappingRegistry.getMethodNameFor("EntityArrow.onUpdate"),
                        "()V",
                        MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g")
                ));
            }

            if (settings.ChatOpacityFixEnabled) {
                patchers.put("ChatOpacityFix", new NextChatOpacityFixPatcher(
                        "ChatOpacityFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/gui/GuiNewChat"),
                        MappingRegistry.getMethodNameFor("GuiNewChat.drawChat"),
                        "(I)V",
                        ""
                ));
            }

            if (settings.ChickenLureFixEnabled) {
                patchers.put("ChickenLureFix", new NextChickenLureFixPatcher(
                        "ChickenLureFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/passive/EntityChicken"),
                        "<init>",
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";)V",
                        ""
                ));
            }

            if (settings.HeartFlashFixEnabled) {
                patchers.put("HeartFlashFix", new NextHeartFlashFixPatcher(
                        "HeartFlashFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                        MappingRegistry.getMethodNameFor("EntityClientPlayerMP.attackEntityFrom"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z",
                        ""
                ));
            }

            if (settings.SnowballFixEnabled) {
                patchers.put("SnowballFix", new NextSnowballFixPatcher(
                        "SnowballFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/player/EntityPlayer"),
                        MappingRegistry.getMethodNameFor("EntityPlayer.attackEntityFrom"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z",
                        ""
                ));
            }

            String sig = "(L" + MappingRegistry.getClassNameFor("net/minecraft/item/ItemStack") + ";"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"
                    + "IIIL" + MappingRegistry.getClassNameFor("net/minecraft/entity/EntityLivingBase") + ";)Z";

            if (settings.ToolDesyncFixEnabled) {
                patchers.put("ToolDesyncFix", new NextToolDesyncFixPatcher(
                        "ToolDesyncFix",
                        MappingRegistry.getClassNameFor("net/minecraft/item/ItemTool"),
                        MappingRegistry.getMethodNameFor("ItemTool.onBlockDestroyed"),
                        sig, // break out into separate block above for readability
                        ""
                ));
            }

            String sig2 = "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                    + "Ljava/util/Random;"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureBoundingBox") + ";)Z";

            if (settings.VillageAnvilTweakEnabled) {
                patchers.put("VillageAnvilTweak", new NextVillageAnvilTweakPatcher(
                        "VillageAnvilTweak",
                        MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureVillagePieces$House2"),
                        MappingRegistry.getMethodNameFor("StructureVillagePieces$House2.addComponentParts"),
                        sig2, // break out into separate block above for readability
                        MappingRegistry.getFieldNameFor("Blocks.double_stone_slab")
                ));
            }

            if (settings.XPFixEnabled) {
                patchers.put("XPFix", new NextXPFixPatcher(
                        "XPFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/network/NetHandlerPlayClient"),
                        MappingRegistry.getMethodNameFor("NetHandlerPlayClient.handleSpawnExperienceOrb"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/network/play/server/S11PacketSpawnExperienceOrb") + ";)V",
                        ""
                ));
            }
        }
    }
}
