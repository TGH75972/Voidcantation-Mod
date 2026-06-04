package net.pocket.mod.effect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
public class ModEffects{
public static final StatusEffect SON_OF_VOID = new SonOfVoidEffect();
public static void registerEffects(){
Registry.register(Registries.STATUS_EFFECT, Identifier.of("voidcantation", "son_of_void"), SON_OF_VOID);
  }
}