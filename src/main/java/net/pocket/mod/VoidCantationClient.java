package net.pocket.mod;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.pocket.mod.component.ModDataComponentTypes;
import net.pocket.mod.item.ModItems;
public class VoidCantationClient implements ClientModInitializer{
@Override
public void onInitializeClient(){
ModelPredicateProviderRegistry.register(ModItems.VOID_POCKET, Identifier.of("voidcantation", "filled"), (stack, world, entity, seed)->{
if(stack == null || ModDataComponentTypes.VOID_STORAGE == null){
return 0.0F;
}
try{
var storage = stack.get(ModDataComponentTypes.VOID_STORAGE);
if(storage != null && storage.count() > 0 && storage.itemIdentifier() != null && !storage.itemIdentifier().isEmpty()){
return 1.0F;
 }
} 
catch(Exception e){
return 0.0F;
 }
return 0.0F;
 }
);
  }
}

