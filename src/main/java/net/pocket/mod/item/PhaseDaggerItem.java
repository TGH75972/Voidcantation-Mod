package net.pocket.mod.item;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
public class PhaseDaggerItem extends SwordItem{
public PhaseDaggerItem(Settings settings){
super(ToolMaterials.IRON, settings.attributeModifiers(createAttributeModifiers(ToolMaterials.IRON, 2, -2.0f)));
 }
@Override
public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker){
World world = attacker.getWorld();
if(!world.isClient){
Vec3d attackerPos = attacker.getPos();
Vec3d targetPos = target.getPos();
attacker.requestTeleport(targetPos.getX(), targetPos.getY(), targetPos.getZ());
target.requestTeleport(attackerPos.getX(), attackerPos.getY(), attackerPos.getZ());
if(world instanceof ServerWorld serverWorld){
serverWorld.spawnParticles(ParticleTypes.PORTAL, attackerPos.x, attackerPos.y + 1, attackerPos.z, 20, 0.5, 0.5, 0.5, 0.1);
serverWorld.spawnParticles(ParticleTypes.PORTAL, targetPos.x, targetPos.y + 1, targetPos.z, 20, 0.5, 0.5, 0.5, 0.1);
}
world.playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.2f);
world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 0.8f);
if(attacker instanceof PlayerEntity player){
player.getItemCooldownManager().set(this, 100);
  }
}
return super.postHit(stack, target, attacker);
}
  }

