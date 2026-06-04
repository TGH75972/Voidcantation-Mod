package net.pocket.mod.mixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.pocket.mod.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity{
public ItemEntityMixin(EntityType<?> type, World world){
super(type, world);
}
@Shadow public abstract ItemStack getStack();
@Shadow private UUID owner;
@Inject(method = "tick", at = @At("TAIL"))
private void checkVoidSacrifice(CallbackInfo ci){
if(this.getWorld().isClient)
return;
if(this.getY() < this.getWorld().getBottomY() + 5){
if(this.getStack().isOf(ModItems.VOID_POCKET)){
UUID throwerId = this.owner;
PlayerEntity thrower = (throwerId != null) ? this.getWorld().getPlayerByUuid(throwerId) : this.getWorld().getClosestPlayer(this, 10);
if(thrower instanceof ServerPlayerEntity serverPlayer){
serverPlayer.giveItemStack(new ItemStack(ModItems.POTION_OF_VOID));
this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_STARE, SoundCategory.PLAYERS, 2.0f, 0.5f);
((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.SCULK_SOUL, this.getX(), this.getY(), this.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
 }
this.discard();
  }
   }
}
 }