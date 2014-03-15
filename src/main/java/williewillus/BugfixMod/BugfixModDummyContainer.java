package williewillus.BugfixMod;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.common.ForgeModContainer;

import java.util.Arrays;

/**
 * Created by Vincent on 3/10/14.
 */


public class BugfixModDummyContainer extends DummyModContainer {

    public BugfixModDummyContainer() {
        super(new ModMetadata());
        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Arrays.asList("williewillus");
        myMeta.description = "Combination of all my other bugfix mods.";
        myMeta.modId = "BugfixMod";
        myMeta.version = "0.2.2";
        myMeta.name = "BugfixMod";
        myMeta.url = "https://github.com/williewillus/BugfixMod";
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }



}
