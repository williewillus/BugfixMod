package williewillus.BugfixMod;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

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
        myMeta.version = "@VERSION@";
        myMeta.name = "BugfixMod";
        myMeta.url = "https://github.com/williewillus/BugfixMod";
    }

    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }



}
