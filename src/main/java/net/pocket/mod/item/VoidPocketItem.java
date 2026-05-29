package net.pocket.mod.item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.pocket.mod.component.ModDataComponentTypes;
import net.pocket.mod.component.VoidStorageData;
public class VoidPocketItem extends Item{
public VoidPocketItem(Settings settings){
super(settings.maxCount(1));
}
@Override
public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
ItemStack pocketStack = user.getStackInHand(hand);
if(!world.isClient){
VoidStorageData storage = pocketStack.getOrDefault(ModDataComponentTypes.VOID_STORAGE, VoidStorageData.EMPTY);
if(user.isSneaking()){
String currentTargetType = storage.itemIdentifier();
int currentCount = storage.count();
for(int i = 0; i < user.getInventory().size(); i++){
ItemStack invStack = user.getInventory().getStack(i);
if(invStack.isEmpty() || invStack.getItem() == this)
continue;
String invItemName = Registries.ITEM.getId(invStack.getItem()).toString();
if(currentTargetType.isEmpty()){
currentTargetType = invItemName;
currentCount += invStack.getCount();
user.getInventory().setStack(i, ItemStack.EMPTY);
} 
else if(invItemName.equals(currentTargetType)){
currentCount += invStack.getCount();
user.getInventory().setStack(i, ItemStack.EMPTY);
  }
}
if(!currentTargetType.isEmpty()){
pocketStack.set(ModDataComponentTypes.VOID_STORAGE, new VoidStorageData(currentTargetType, currentCount));
user.sendMessage(Text.literal("§dCompressed: " + currentCount + "x " + currentTargetType), true);
 }
} 
else{
if(storage.count() > 0){
Item typeItem = Registries.ITEM.get(Identifier.of(storage.itemIdentifier()));
int releaseAmount = Math.min(64, storage.count());
ItemStack dropStack = new ItemStack(typeItem, releaseAmount);
user.getInventory().offerOrDrop(dropStack);
int remaining = storage.count() - releaseAmount;
if(remaining <= 0){
pocketStack.set(ModDataComponentTypes.VOID_STORAGE, VoidStorageData.EMPTY);
user.sendMessage(Text.literal("§ePocket Dimension is now empty."), true);
} 
else{
pocketStack.set(ModDataComponentTypes.VOID_STORAGE, new VoidStorageData(storage.itemIdentifier(), remaining));
user.sendMessage(Text.literal("§dRemaining in Void: " + remaining), true);
 }
} 
else{
user.sendMessage(Text.literal("§cVoid Pocket is empty. Crouch + Right click to store items."), true);
  }
 }
}
return TypedActionResult.success(pocketStack);
 }
}

