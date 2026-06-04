package net.pocket.mod.mixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.pocket.mod.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(PlayerEntity.class)
public abstract class ShadowStrikeMixin{
@Inject(method = "attack", at = @At("HEAD"))
private void triggerShadowStrike(Entity target, CallbackInfo ci){
PlayerEntity player = (PlayerEntity) (Object) this;
if(player.hasStatusEffect(Registries.STATUS_EFFECT.getEntry(ModEffects.SON_OF_VOID))){
if(target instanceof LivingEntity livingTarget && !player.getWorld().isClient){
ServerWorld world = (ServerWorld) player.getWorld();
Vec3d targetCenter = target.getBoundingBox().getCenter();
Vec3d playerLook = player.getRotationVector();
Vec3d rightVector = playerLook.crossProduct(new Vec3d(0, 1, 0)).normalize();
Vec3d[] tentacleOrigins={
targetCenter.add(rightVector.multiply(2.5)).add(0, 1.5, 0),
targetCenter.add(rightVector.multiply(-2.5)).add(0, 1.5, 0),
targetCenter.add(rightVector.multiply(2.0)).add(0, -0.5, 0),
targetCenter.add(rightVector.multiply(-2.0)).add(0, -0.5, 0)
};
for(Vec3d startPos : tentacleOrigins){
Vec3d diff = targetCenter.subtract(startPos);
double length = diff.length();
Vec3d dir = diff.normalize();
for(double i = 0; i <= length; i += 0.15){
double progress = i / length;
double wave = Math.sin(progress * Math.PI * 3 + player.age) * (1.0 - progress) * 0.6;
Vec3d point = startPos.add(dir.multiply(i)).add(0, wave, 0);
world.spawnParticles(ParticleTypes.SQUID_INK, point.x, point.y, point.z, 2, 0.05, 0.05, 0.05, 0.0);
world.spawnParticles(ParticleTypes.SCULK_SOUL, point.x, point.y, point.z, 1, 0.02, 0.02, 0.02, 0.0);
 }
}
livingTarget.damage(player.getDamageSources().magic(), 15.0f);
world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_WARDEN_ATTACK_IMPACT, SoundCategory.PLAYERS, 1.2f, 0.5f);
world.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.PLAYERS, 1.0f, 0.7f);
   }
 }
  }
}