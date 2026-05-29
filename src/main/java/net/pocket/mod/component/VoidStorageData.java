package net.pocket.mod.component;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
public record VoidStorageData(String itemIdentifier, int count){
public static final Codec<VoidStorageData> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("item").forGetter(VoidStorageData::itemIdentifier),Codec.INT.fieldOf("count").forGetter(VoidStorageData::count)).apply(instance, VoidStorageData::new));
public static final VoidStorageData EMPTY = new VoidStorageData("", 0);
}