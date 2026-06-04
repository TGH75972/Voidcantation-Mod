package net.pocket.mod.mixin;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.pocket.mod.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.EnumSet;
@Mixin(PlayerEntity.class)
public abstract class VoidImmunityMixin{
@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
private void cancelVoidDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
PlayerEntity player = (PlayerEntity) (Object) this;
if(source.isOf(DamageTypes.OUT_OF_WORLD) && player.hasStatusEffect(Registries.STATUS_EFFECT.getEntry(ModEffects.SON_OF_VOID))){
cir.setReturnValue(false);
if(!player.getWorld().isClient && player instanceof ServerPlayerEntity serverPlayer){
MinecraftServer server = serverPlayer.getServer();
if(server != null){
ServerWorld targetWorld = server.getWorld(serverPlayer.getSpawnPointDimension());
if(targetWorld == null){
targetWorld = server.getWorld(World.OVERWORLD);
}
BlockPos spawnPos = serverPlayer.getSpawnPointPosition();
float spawnAngle = serverPlayer.getSpawnAngle();
if(spawnPos == null && targetWorld != null){
spawnPos = targetWorld.getSpawnPos();
}
if(targetWorld != null && spawnPos != null){
serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 80, 0, false, false, false));
serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 80, 0, false, false, false));
double targetX = spawnPos.getX() + 0.5;
double targetY = spawnPos.getY() + 0.1;
double targetZ = spawnPos.getZ() + 0.5;
serverPlayer.teleport(targetWorld, targetX, targetY, targetZ, EnumSet.noneOf(PositionFlag.class), spawnAngle, 0.0F);
serverPlayer.fallDistance = 0.0F;
targetWorld.playSound(null, BlockPos.ofFloored(targetX, targetY, targetZ), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 0.6F);
targetWorld.playSound(null, BlockPos.ofFloored(targetX, targetY, targetZ), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.PLAYERS, 1.5F, 0.8F);
for(int i = 0; i < 360; i += 10){
double rad = Math.toRadians(i);
double x = Math.cos(rad) * 1.5;
double z = Math.sin(rad) * 1.5;
targetWorld.spawnParticles(ParticleTypes.SCULK_SOUL, targetX + x, targetY + 1.0, targetZ + z, 2, x * 0.2, 0.5, z * 0.2, 0.1);
targetWorld.spawnParticles(ParticleTypes.DRAGON_BREATH, targetX + x, targetY + 1.0, targetZ + z, 3, 0.0, 0.5, 0.0, 0.05);
  }
    }
 }
  }
 }
 }
  }