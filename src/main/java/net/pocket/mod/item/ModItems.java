package net.pocket.mod.item;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
public class ModItems{
public static final Item ARCHITECTS_COMPASS = registerItem("architects_compass", new ArchitectsCompassItem(new Item.Settings().maxCount(1)));
public static final Item VOID_POCKET = registerItem("void_pocket", new VoidPocketItem(new Item.Settings()));
public static final Item PHASE_DAGGER = registerItem("phase_dagger", new PhaseDaggerItem(new Item.Settings()));
private static Item registerItem(String name, Item item){
return Registry.register(Registries.ITEM, Identifier.of("voidcantation", name), item);
}
public static void registerModItems(){
ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries->{
entries.add(ARCHITECTS_COMPASS);
entries.add(VOID_POCKET);
  });
ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries->{
entries.add(PHASE_DAGGER);
  });
 }
}