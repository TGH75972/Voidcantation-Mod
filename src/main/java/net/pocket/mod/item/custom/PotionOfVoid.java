package net.pocket.mod.item.custom;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.pocket.mod.effect.ModEffects;
public class PotionOfVoid extends Item{
public PotionOfVoid(Settings settings){
super(settings);
}
@Override
public UseAction getUseAction(ItemStack stack){
return UseAction.DRINK;
}
@Override
public int getMaxUseTime(ItemStack stack, LivingEntity user){
return 32;
}
@Override
public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user){
if(user instanceof PlayerEntity player && !world.isClient){
player.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(ModEffects.SON_OF_VOID), 6000, 0));
player.sendMessage(Text.literal("The void screams within you..."), true);
world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.PLAYERS, 1.0f, 0.5f);
}
if(user instanceof PlayerEntity player && !player.isCreative()){
stack.decrement(1);
}
return super.finishUsing(stack, world, user);
 }
}
