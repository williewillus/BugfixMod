package williewillus.WillieTweaks.common.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;
import williewillus.WillieTweaks.common.WillieTweaksSettings;
import williewillus.WillieTweaks.common.asm.patchers.AbstractPatcher;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Vincent on 3/10/14.
 */
public class ASMClassTransformer implements IClassTransformer {

    public static ASMClassTransformer instance;
    public File settingsFile;
    private boolean hasInit = false;
    public WillieTweaksSettings settings;
    private ArrayList<AbstractPatcher> patchers;
    public Configuration config;

    public ASMClassTransformer() {
        if (instance != null) {
            throw new RuntimeException("Only one transformer may exist!");
        } else {
            instance = this;
        }
    }

    public void initialize(Boolean isObf) {
        if (!hasInit) {
            config = new Configuration(settingsFile);
            config.load();
            settings = new WillieTweaksSettings();

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
            System.out.println("Patcher already initialized!!");
        } else {
            patchers = new ArrayList<williewillus.WillieTweaks.common.asm.patchers.AbstractPatcher>();

            //if (settings.ArrowFixEnabled) {
            //    patchers.add(new ArrowFixPatcher(
            //            "ArrowFix",
            //            MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow"),
            //            MappingRegistry.getMethodNameFor("EntityArrow.onUpdate"),
            //            "()V",
            //            MappingRegistry.getFieldNameFor("EntityArrow.field_145790_g")
            //    ));
            //}

			// ArrowFix's bug has been FIXED by Mojang as of Minecraft 1.7.6. YAY!

            if (settings.ArrowDingTweakEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.ArrowDingTweakPatcher(
                        "ArrowDingTweak",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/projectile/EntityArrow"),
                        MappingRegistry.getMethodNameFor("EntityArrow.onUpdate"),
                        "()V",
                        ""
                ));
            }

            if (settings.ChatOpacityFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.ChatOpacityFixPatcher(
                        "ChatOpacityFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/gui/GuiNewChat"),
                        MappingRegistry.getMethodNameFor("GuiNewChat.drawChat"),
                        "(I)V",
                        ""
                ));
            }

            if (settings.ChickenLureFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.ChickenLureFixPatcher(
                        "ChickenLureFix",
                        MappingRegistry.getClassNameFor("net/minecraft/entity/passive/EntityChicken"),
                        "<init>",
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/world/World") + ";)V",
                        ""
                ));
            }

            if (settings.HeartFlashFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.HeartFlashFixPatcher(
                        "HeartFlashFix",
                        MappingRegistry.getClassNameFor("net/minecraft/client/entity/EntityClientPlayerMP"),
                        MappingRegistry.getMethodNameFor("EntityClientPlayerMP.attackEntityFrom"),
                        "(L" + MappingRegistry.getClassNameFor("net/minecraft/util/DamageSource") + ";F)Z",
                        ""
                ));
            }

            if (settings.ItemHopperBounceFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.ItemHopperBounceFixPatcher(
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

            if (settings.ItemStairBounceFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.ItemStairBounceFixPatcher(
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

            if (settings.SnowballFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.SnowballFixPatcher(
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
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.ToolDesyncFixPatcher(
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
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.VillageAnvilTweakPatcher(
                        "VillageAnvilTweak",
                        MappingRegistry.getClassNameFor("net/minecraft/world/gen/structure/StructureVillagePieces$House2"),
                        MappingRegistry.getMethodNameFor("StructureVillagePieces$House2.addComponentParts"),
                        sig2, // break out into separate block above for readability
                        MappingRegistry.getFieldNameFor("Blocks.double_stone_slab")
                ));
            }

            if (settings.XPFixEnabled) {
                patchers.add(new williewillus.WillieTweaks.common.asm.patchers.XPFixPatcher(
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
