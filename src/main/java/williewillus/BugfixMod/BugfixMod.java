package williewillus.BugfixMod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

import java.util.Arrays;

/**
 * Created by Vincent on 3/10/14.
 */


public class BugfixMod extends DummyModContainer {

    public BugfixMod() {
        super(new ModMetadata());
        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Arrays.asList(new String[]{"williewillus"});
        myMeta.description = "Combination of all my other bugfix mods.";
        myMeta.modId = "BugfixMod";
        myMeta.version = "0.2.0";
        myMeta.name = "BugfixMod";
        myMeta.url = "https://github.com/williewillus/BugfixMod";
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {
    }



}
