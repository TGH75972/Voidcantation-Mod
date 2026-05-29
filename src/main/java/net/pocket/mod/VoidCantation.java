package net.pocket.mod;
import net.fabricmc.api.ModInitializer;
import net.pocket.mod.component.ModDataComponentTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class VoidCantation implements ModInitializer{
public static final Logger LOGGER = LoggerFactory.getLogger("voidcantation");
@Override
public void onInitialize(){
LOGGER.info("Loading components: {}", ModDataComponentTypes.VOID_STORAGE);
 }
}

