package net.pocket.mod.item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.pocket.mod.component.ModDataComponentTypes;
public class ArchitectsCompassItem extends Item{
public ArchitectsCompassItem(Settings settings){
super(settings);
 }
@Override
public ActionResult useOnBlock(ItemUsageContext context){
World world = context.getWorld();
if(!world.isClient){
PlayerEntity player = context.getPlayer();
ItemStack stack = context.getStack();
BlockPos targetPos = context.getBlockPos();
if(player != null && player.isSneaking()){
stack.set(ModDataComponentTypes.BOOKMARKED_POS, targetPos);
player.sendMessage(Text.literal("§aLocation Bookmarked! §7[" + targetPos.getX() + ", " + targetPos.getY() + ", " + targetPos.getZ() + "]"), true);
return ActionResult.SUCCESS;
  }
}
return super.useOnBlock(context);
}
@Override
public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected){
BlockPos target = stack.get(ModDataComponentTypes.BOOKMARKED_POS);
if(target != null){
if(!world.isClient && entity instanceof PlayerEntity player){
if(selected || player.getOffHandStack() == stack){
double distance = Math.sqrt(entity.getBlockPos().getSquaredDistance(target));
player.sendMessage(Text.literal(String.format("§bVoid Compass §7➔ §eX: %d Y: %d Z: %d §7(§a%.1fm away§7)", target.getX(), target.getY(), target.getZ(), distance)), true);
 }
} 
else if(world.isClient && world.getTime() % 10 == 0){
world.addParticle(ParticleTypes.PORTAL, target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, 0, 0, 0);
   }
 }
super.inventoryTick(stack, world, entity, slot, selected);
}
 }
