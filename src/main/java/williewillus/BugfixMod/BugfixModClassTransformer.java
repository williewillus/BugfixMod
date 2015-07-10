package williewillus.BugfixMod;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import williewillus.BugfixMod.patchers.AbstractPatcher;
import williewillus.BugfixMod.patchers.BoatDesyncFixPatcher_Extra;
import williewillus.BugfixMod.patchers.BoatDesyncFixPatcher_Main;
import williewillus.BugfixMod.patchers.ChatOpacityFixPatcher;
import williewillus.BugfixMod.patchers.ChickenLureTweakPatcher;
import williewillus.BugfixMod.patchers.HeartBlinkFixPatcher;
import williewillus.BugfixMod.patchers.HeartFlashFixCompatPatcher;
import williewillus.BugfixMod.patchers.HeartFlashFixPatcher;
import williewillus.BugfixMod.patchers.ItemHopperBounceFixPatcher;
import williewillus.BugfixMod.patchers.ItemStairBounceFixPatcher;
import williewillus.BugfixMod.patchers.SnowballFixPatcher;
import williewillus.BugfixMod.patchers.ToolDesyncFixPatcher;
import williewillus.BugfixMod.patchers.VillageAnvilTweakPatcher;
import williewillus.BugfixMod.patchers.XPFixPatcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Vincent on 3/10/14.
 */
public class BugfixModClassTransformer implements IClassTransformer {

    public static BugfixModClassTransformer instance;
    public File settingsFile;
    private boolean hasInit = false;
    protected BugfixModSettings settings;
    private ArrayList<AbstractPatcher> patchers;
    public Logger logger = LogManager.getLogger("BugfixMod");

    public BugfixModClassTransformer() {

        if (instance != null) {
            throw new RuntimeException("Only one transformer may exist!");
        } else {
            instance = this;
        }
    }

    public void initialize(Boolean isObf) {
        if (!hasInit) {
            Configuration config = new Configuration(settingsFile);
            config.load();
            settings = new BugfixModSettings();


            settings.ItemHopperBounceFixEnabled = config.get("COMMON", "ItemHopperBounceFixEnabled", false).getBoolean(false);
            settings.ItemStairBounceFixEnabled = config.get("COMMON", "ItemStairBounceFixEnabled", false).getBoolean(false);
            settings.SnowballFixEnabled = config.get("COMMON", "SnowballFixEnabled", true).getBoolean(true);

            settings.ChickenLureTweakEnabled = config.get("TWEAKS", "ChickenLureTweakEnabled", false).getBoolean(false);
            settings.VillageAnvilTweakEnabled = config.get("TWEAKS", "VillageAnvilTweakEnabled", false).getBoolean(false);

            settings.BoatDesyncFixEnabled = config.get("CLIENT", "BoatDesyncFixEnabled", true).getBoolean(true);
            settings.ChatOpacityFixEnabled = config.get("CLIENT", "ChatOpacityFixEnabled", true).getBoolean(true);
            settings.HeartBlinkFixEnabled = config.get("CLIENT", "HeartBlinkFixEnabled", true).getBoolean(true);
            settings.HeartFlashFixEnabled = config.get("CLIENT", "HeartFlashFixEnabled", true).getBoolean(true);
            settings.ToolDesyncFixEnabled = config.get("CLIENT", "ToolDesyncFixEnabled", true).getBoolean(true);
            settings.XPFixEnabled = config.get("CLIENT", "XPFixEnabled", true).getBoolean(true);

            if (ForgeVersion.getBuildVersion() >= 1181) {
                settings.ChatOpacityFixEnabled = false;
                logger.info("ChatOpacityFix disabled as it is now included in Forge!");
            }

            if (ForgeVersion.getBuildVersion() >= 1182) {
                settings.XPFixEnabled = false;
                logger.info("XPFix disabled as it is now included in Forge!");
            }

            if (!Arrays.asList(new File(new File(settingsFile.getParent()).getParent()).list()).contains("saves")) {
                logger.info("You probably are on a dedicated server. Disabling client fixes");
                settings.BoatDesyncFixEnabled = false;
                settings.ChatOpacityFixEnabled = false;
                settings.HeartBlinkFixEnabled = false;
                settings.HeartFlashFixEnabled = false;
                settings.ToolDesyncFixEnabled = false;
                settings.XPFixEnabled = false;
            }

            config.save();
            MappingRegistry.init(isObf);
            setupPatchers();
            hasInit = true;
        }
    }


    public byte[] transform(String par1, String par2, byte[] bytes) {
        if (hasInit) {
            for (AbstractPatcher p : patchers) {
                bytes = p.patch(bytes);
            }
        }
        return bytes;
    }


    private void setupPatchers() {
        if (patchers != null) {
            logger.warn("Patcher already initialized!!");
        } else {
            patchers = new ArrayList<AbstractPatcher>();


            if (settings.BoatDesyncFixEnabled) {
                patchers.add(new BoatDesyncFixPatcher_Main(
                    "BoatDesyncFix",
                    MappingRegistry.getClassNameFor("net/minecraft/entity/item/EntityBoat"),
                    MappingRegistry.getMethodNameFor("EntityBoat.setBoatIsEmpty"),
                    "(Z)V"
                ));
                patchers.add(new BoatDesyncFixPatcher_Extra(
                    "BoatDesyncFix|Extra",
                    MappingRegistry.getClassNameFor("net/minecraft/entity/item/EntityBoat"),
                    MappingRegistry.getMethodNameFor("EntityBoat.setPositionAndRotation2"),
                    "(DDDFFI)V"
                ));
            }

            if (settings.ChatOpacityFixEnabled) {
                patchers.add(new ChatOpacityFixPatcher(
                        "ChatOpacityFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/gui/GuiNewChat"),
                        MappingRegistry.getMethodNameFor("GuiNewChat.drawChat"),
                        "(I)V"
                ));
            }

            if (settings.ChickenLureTweakEnabled) {
                patchers.add(new ChickenLureTweakPatcher(
                        "ChickenLureTweak",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/passive/EntityChicken"),
                        "<init>",
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";)V"
                ));
            }

            if (settings.HeartBlinkFixEnabled) {
                patchers.add(new HeartBlinkFixPatcher(
                    "HeartBlinkFix",
                    MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityPlayerSP"),
                    MappingRegistry.getMethodNameFor("EntityPlayerSP.setPlayerSPHealth"),
                    "(F)V"
                ));
            }

            if (settings.HeartFlashFixEnabled) {
                patchers.add(new HeartFlashFixPatcher(
                        "HeartFlashFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                        MappingRegistry.getMethodNameFor("EntityClientPlayerMP.attackEntityFrom"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z"
                ));

            }

            if (settings.HeartBlinkFixEnabled && settings.HeartFlashFixEnabled) {
                patchers.add(new HeartFlashFixCompatPatcher(
                        "HeartFlashFix|Compat",
                        MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                        MappingRegistry.getMethodNameFor("EntityClientPlayerMP.setPlayerSPHealth"),
                        "(F)V"
                ));
            }

            if (settings.ItemHopperBounceFixEnabled) {
                patchers.add(new ItemHopperBounceFixPatcher(
                        "ItemHopperBounceFix",
                        MappingRegistry.getClassNameFor("net/minecraft/block/BlockHopper"),
                        MappingRegistry.getMethodNameFor("BlockHopper.addCollisionBoxesToList"),
                        "(L" +
                                MappingRegistry.getClassNameFor("net/minecraft/world/World") +
                                ";IIIL" +
                                MappingRegistry.getClassNameFor("net/minecraft/util/AxisAlignedBB") +
                                ";Ljava/util/List;L" +
                                MappingRegistry.getClassNameFor("net/minecraft/entity/Entity")
                                + ";)V"
                ));
            }

            if (settings.ItemStairBounceFixEnabled) {
                patchers.add(new ItemStairBounceFixPatcher(
                        "ItemStairBounceFix",
                        MappingRegistry.getClassNameFor("net/minecraft/block/BlockStairs"),
                        MappingRegistry.getMethodNameFor("BlockStairs.addCollisionBoxesToList"),
                        "(L" +
                                MappingRegistry.getClassNameFor("net/minecraft/world/World") +
                                ";IIIL" +
                                MappingRegistry.getClassNameFor("net/minecraft/util/AxisAlignedBB") +
                                ";Ljava/util/List;L" +
                                MappingRegistry.getClassNameFor("net/minecraft/entity/Entity")
                                + ";)V"
                ));
            }

            if (settings.SnowballFixEnabled) {
                patchers.add(new SnowballFixPatcher(
                        "SnowballFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/player/EntityPlayer"),
                        MappingRegistry.getMethodNameFor("EntityPlayer.attackEntityFrom"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z"
                ));
            }

            String sig = "(L" + MappingRegistry.getClassNameFor("net/minecraft/item/ItemStack") + ";"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/block/Block") + ";"
                    + "IIIL" + MappingRegistry.getClassNameFor("net/minecraft/entity/EntityLivingBase") + ";)Z";

            if (settings.ToolDesyncFixEnabled) {
                patchers.add(new ToolDesyncFixPatcher(
                        "ToolDesyncFix",
                        MappingRegistry.getClassNameFor("net/minecraft/item/ItemTool"),
                        MappingRegistry.getMethodNameFor("ItemTool.onBlockDestroyed"),
                        sig // break out into separate block above for readability
                ));
            }

            String sig2 = "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";"
                    + "Ljava/util/Random;"
                    + "L" + MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureBoundingBox") + ";)Z";

            if (settings.VillageAnvilTweakEnabled) {
                patchers.add(new VillageAnvilTweakPatcher(
                        "VillageAnvilTweak",
                        MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureVillagePieces$House2"),
                        MappingRegistry.getMethodNameFor("StructureVillagePieces$House2.addComponentParts"),
                        sig2 // break out into separate block above for readability
                ));
            }

            if (settings.XPFixEnabled) {
                patchers.add(new XPFixPatcher(
                        "XPFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/network/NetHandlerPlayClient"),
                        MappingRegistry.getMethodNameFor("NetHandlerPlayClient.handleSpawnExperienceOrb"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/network/play/server/S11PacketSpawnExperienceOrb") + ";)V"
                ));
            }

        }
    }
}
