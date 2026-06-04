package net.pocket.mod.item;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.pocket.mod.component.ModDataComponentTypes;
import net.pocket.mod.component.PhaseDaggerData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class PhaseDaggerItem extends SwordItem{
public PhaseDaggerItem(Settings settings){
super(ToolMaterials.IRON, settings.attributeModifiers(createAttributeModifiers(ToolMaterials.IRON, 4, -1.5f)));
}
@Override
public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker){
World world = attacker.getWorld();
if(!world.isClient && attacker instanceof PlayerEntity player){
PhaseDaggerData data = stack.getOrDefault(ModDataComponentTypes.PHASE_DAGGER_DATA, PhaseDaggerData.EMPTY);
if(!data.executing()){
double skyTargetY = target.getY() + 45.0;
target.requestTeleport(target.getX(), skyTargetY, target.getZ());
target.setVelocity(0, -0.5, 0);
target.velocityModified = true;
if(world instanceof ServerWorld serverWorld){
serverWorld.spawnParticles(ParticleTypes.SOUL, target.getX(), target.getY() + 1.0, target.getZ(), 20, 0.3, 0.5, 0.3, 0.15);
serverWorld.spawnParticles(ParticleTypes.POOF, target.getX(), target.getY(), target.getZ(), 10, 0.2, 0.2, 0.2, 0.1);
 }
world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 0.6f);
world.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENTITY_WIND_CHARGE_THROW, SoundCategory.PLAYERS, 1.2f, 0.5f);
   }
}
return super.postHit(stack, target, attacker);
}

@Override
public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
ItemStack stack = user.getStackInHand(hand);
if(world.isClient)
return TypedActionResult.success(stack);
PhaseDaggerData data = stack.getOrDefault(ModDataComponentTypes.PHASE_DAGGER_DATA, PhaseDaggerData.EMPTY);
if(!data.active() && !data.executing()){
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(true, new ArrayList<>(), false, 0, world.getTime()));
world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 0.5f);
user.sendMessage(Text.literal("§b§l[QUANTUM STANCE] §7Mark up to 4 targets via Right-Click!"), true);
} 
else if(data.active() && !data.executing()){
LivingEntity target = getTargetedEntity(user, world, 25.0);
List<Integer> currentTargets = new ArrayList<>(data.targetIds());
if(target != null && !currentTargets.contains(target.getId()) && currentTargets.size() < 4){
currentTargets.add(target.getId());
target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 120, 0, false, false));
world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.PLAYERS, 1.0f, 1.4f + (currentTargets.size() * 0.1f));
user.sendMessage(Text.literal("§3§lLocked §7[" + currentTargets.size() + "/4]"), true);
if(currentTargets.size() >= 4){
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(false, currentTargets, true, 0, world.getTime()));
} 
else{
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(true, currentTargets, false, 0, data.startTick()));
 }
} 

else{
if(!currentTargets.isEmpty()){
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(false, currentTargets, true, 0, world.getTime()));
} 
else{
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, PhaseDaggerData.EMPTY);
user.sendMessage(Text.literal("§cStance Cancelled."), true);
   }
 }
  }
return TypedActionResult.success(stack);
}
@Override
public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected){
if(world.isClient || !(entity instanceof PlayerEntity player))
return;
PhaseDaggerData data = stack.get(ModDataComponentTypes.PHASE_DAGGER_DATA);
if(data == null || (!data.active() && !data.executing()))
return;
long elapsedTicks = world.getTime() - data.startTick();
if(data.active()){
if(elapsedTicks > 100){
if(!data.targetIds().isEmpty()){
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(false, data.targetIds(), true, 0, world.getTime()));
}
else{
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, PhaseDaggerData.EMPTY);
player.sendMessage(Text.literal("§cStance timed out."), true);
  }
return;
 }
if(world instanceof ServerWorld serverWorld && world.getTime() % 5 == 0){
serverWorld.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getY() + 0.5, player.getZ(), 4, 0.5, 0.5, 0.5, 0.0);
 }
Box localChaosArea = player.getBoundingBox().expand(20.0);
for(LivingEntity nearby : world.getEntitiesByClass(LivingEntity.class, localChaosArea, e -> e != player)){
nearby.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 4, false, false, false));
}
}
if(data.executing()){
List<Integer> targets = data.targetIds();
int index = data.currentIndex();
if(index >= targets.size()){
if(world instanceof ServerWorld serverWorld){
boolean hitShulker = false;
for(int targetId : targets){
Entity doomedEntity = serverWorld.getEntityById(targetId);
if(doomedEntity instanceof LivingEntity doomed && doomed.isAlive()){
if(doomed.getType() == EntityType.SHULKER){
hitShulker = true;
doomed.kill();
serverWorld.spawnParticles(ParticleTypes.END_ROD, doomed.getX(), doomed.getY(), doomed.getZ(), 40, 0.5, 0.5, 0.5, 0.2);
}
else{
doomed.damage(serverWorld.getDamageSources().playerAttack(player), 24.0f);
Box blastRadius = doomed.getBoundingBox().expand(4.5);
List<LivingEntity> casualties = serverWorld.getEntitiesByClass(LivingEntity.class, blastRadius, e -> e != player && e != doomed);
for(LivingEntity collateral : casualties){
collateral.damage(serverWorld.getDamageSources().explosion(player, player), 12.0f);
Vec3d pushDir = collateral.getPos().subtract(doomed.getPos()).normalize().multiply(0.8);
collateral.addVelocity(pushDir.x, pushDir.y + 0.3, pushDir.z);
collateral.velocityModified = true;
}
serverWorld.spawnParticles(ParticleTypes.EXPLOSION, doomed.getX(), doomed.getY() + 0.5, doomed.getZ(), 3, 0.2, 0.2, 0.2, 0.0);
serverWorld.spawnParticles(ParticleTypes.FLASH, doomed.getX(), doomed.getY() + 0.5, doomed.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
serverWorld.spawnParticles(ParticleTypes.SCULK_SOUL, doomed.getX(), doomed.getY() + 0.5, doomed.getZ(), 25, 0.4, 0.4, 0.4, 0.15);
 }
  }
}
world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0f, 0.8f);
world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.PLAYERS, 1.5f, 0.5f);
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, PhaseDaggerData.EMPTY);
if(hitShulker){
player.getItemCooldownManager().set(this, 0);
player.setVelocity(player.getVelocity().x * 0.5, 0.85, player.getVelocity().z * 0.5);
player.velocityModified = true;
world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 2.0f);
player.sendMessage(Text.literal("§d§l[AERIAL RESET] §fChain your next strike!"), true);
}
else{
player.getItemCooldownManager().set(this, 300);
player.sendMessage(Text.literal("§b§l§kXX§r §b§lJUDGEMENT COMPLETE §b§l§kXX§r"), true);
  }
}
return;
}
Entity targetEntity = world.getEntityById(targets.get(index));
if(targetEntity == null || !targetEntity.isAlive()){
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(false, targets, true, index + 1, world.getTime()));
return;
}
Vec3d origin = player.getPos();
Vec3d destination = targetEntity.getPos().add(0, targetEntity.getHeight() / 2.0, 0);
Vec3d dashVector = destination.subtract(origin);
double rangeDistance = dashVector.length();
if(rangeDistance > 2.2){
Vec3d step = dashVector.normalize().multiply(4.50);
player.requestTeleport(origin.x + step.x, origin.y + step.y, origin.z + step.z);
player.setVelocity(0, 0, 0);
player.velocityModified = true;
player.fallDistance = 0;
if(world instanceof ServerWorld serverWorld){
serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 0.5, player.getZ(), 6, 0.1, 0.1, 0.1, 0.01);
 }
} 
else{
if(world instanceof ServerWorld serverWorld){
serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, targetEntity.getX(), targetEntity.getY() + 0.5, targetEntity.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
}
world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.5f);
stack.set(ModDataComponentTypes.PHASE_DAGGER_DATA, new PhaseDaggerData(false, targets, true, index + 1, world.getTime()));
   }
 }
}
private LivingEntity getTargetedEntity(PlayerEntity player, World world, double maxDistance){
Vec3d eyePos = player.getCameraPosVec(1.0F);
Vec3d lookDir = player.getRotationVec(1.0F);
Vec3d targetLineEnd = eyePos.add(lookDir.multiply(maxDistance));
Box searchBoundingArea = player.getBoundingBox().stretch(lookDir.multiply(maxDistance)).expand(1.0);
double closestDistance = maxDistance;
LivingEntity targetedActor = null;
for(Entity entity : world.getOtherEntities(player, searchBoundingArea)){
if(entity instanceof LivingEntity living && entity.isAlive()){
Box interactionFrameBox = entity.getBoundingBox().expand(0.45);
Optional<Vec3d> hitIntersectionResult = interactionFrameBox.raycast(eyePos, targetLineEnd);
if(hitIntersectionResult.isPresent()){
double actualRangeDist = eyePos.distanceTo(hitIntersectionResult.get());
if(actualRangeDist < closestDistance){
closestDistance = actualRangeDist;targetedActor = living;
  }
 }
   }
}
return targetedActor;
 }
}
