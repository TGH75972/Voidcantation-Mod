package net.pocket.mod.item;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.pocket.mod.item.custom.PotionOfVoid;
public class ModItems{
public static final Item ARCHITECTS_COMPASS = registerItem("architects_compass", new ArchitectsCompassItem(new Item.Settings().maxCount(1)));
public static final Item VOID_POCKET = registerItem("void_pocket", new VoidPocketItem(new Item.Settings()));
public static final Item PHASE_DAGGER = registerItem("phase_dagger", new PhaseDaggerItem(new Item.Settings()));
public static final Item POTION_OF_VOID = registerItem("potion_of_void", new PotionOfVoid(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().build())));
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
ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries->{
entries.add(POTION_OF_VOID);
 });
    }
}
