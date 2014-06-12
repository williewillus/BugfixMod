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
            settings.ItemStairBounceFixEnabled = config.get("COMMON", "ItemStairBounceFixEnabled", true).getBoolean(true);
            settings.ItemHopperBounceFixEnabled = config.get("COMMON", "ItemHopperBounceFixEnabled", false).getBoolean(false);

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
                patchers.put("ArrowFix", new ArrowFixPatcher(
                        "ArrowFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow"),
                        MappingRegistry.getMethodNameFor("EntityArrow.onUpdate"),
                        "()V",
                        MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g")
                ));
            }

            if (settings.ChatOpacityFixEnabled) {
                patchers.put("ChatOpacityFix", new ChatOpacityFixPatcher(
                        "ChatOpacityFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/gui/GuiNewChat"),
                        MappingRegistry.getMethodNameFor("GuiNewChat.drawChat"),
                        "(I)V",
                        ""
                ));
            }

            if (settings.ChickenLureFixEnabled) {
                patchers.put("ChickenLureFix", new ChickenLureFixPatcher(
                        "ChickenLureFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/passive/EntityChicken"),
                        "<init>",
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";)V",
                        ""
                ));
            }

            if (settings.HeartFlashFixEnabled) {
                patchers.put("HeartFlashFix", new HeartFlashFixPatcher(
                        "HeartFlashFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                        MappingRegistry.getMethodNameFor("EntityClientPlayerMP.attackEntityFrom"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z",
                        ""
                ));
            }

            if (settings.SnowballFixEnabled) {
                patchers.put("SnowballFix", new SnowballFixPatcher(
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
                patchers.put("ToolDesyncFix", new ToolDesyncFixPatcher(
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
                patchers.put("VillageAnvilTweak", new VillageAnvilTweakPatcher(
                        "VillageAnvilTweak",
                        MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureVillagePieces$House2"),
                        MappingRegistry.getMethodNameFor("StructureVillagePieces$House2.addComponentParts"),
                        sig2, // break out into separate block above for readability
                        MappingRegistry.getFieldNameFor("Blocks.double_stone_slab")
                ));
            }

            if (settings.XPFixEnabled) {
                patchers.put("XPFix", new XPFixPatcher(
                        "XPFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/network/NetHandlerPlayClient"),
                        MappingRegistry.getMethodNameFor("NetHandlerPlayClient.handleSpawnExperienceOrb"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/network/play/server/S11PacketSpawnExperienceOrb") + ";)V",
                        ""
                ));
            }

            if (settings.ItemStairBounceFixEnabled) {
                patchers.put("ItemStairBounceFix", new ItemStairBounceFixPatcher(
                    "ItemStairBounceFix",
                    MappingRegistry.getClassNameFor("net/minecraft/block/BlockStairs"),
                    MappingRegistry.getMethodNameFor("BlockStairs.addCollisionBoxesToList"),
                    "(L" +
                            MappingRegistry.getClassNameFor("net/minecraft/world/World") +
                            ";IIIL" +
                            MappingRegistry.getClassNameFor("net/minecraft/util/AxisAlignedBB") +
                            ";Ljava/util/List;L" +
                            MappingRegistry.getClassNameFor("net/minecraft/entity/Entity")
                    + ";)V",
                    ""
                ));
            }

            if (settings.ItemHopperBounceFixEnabled) {
                patchers.put("ItemHopperBounceFix", new ItemHopperBounceFixPatcher(
                        "ItemHopperBounceFix",
                        MappingRegistry.getClassNameFor("net/minecraft/block/BlockHopper"),
                        MappingRegistry.getMethodNameFor("BlockHopper.addCollisionBoxesToList"),
                        "(L" +
                                MappingRegistry.getClassNameFor("net/minecraft/world/World") +
                                ";IIIL" +
                                MappingRegistry.getClassNameFor("net/minecraft/util/AxisAlignedBB") +
                                ";Ljava/util/List;L" +
                                MappingRegistry.getClassNameFor("net/minecraft/entity/Entity")
                                + ";)V",
                        ""
                ));
            }
        }
    }
}
