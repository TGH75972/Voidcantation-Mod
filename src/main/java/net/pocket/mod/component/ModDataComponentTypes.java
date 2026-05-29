package net.pocket.mod.component;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
public class ModDataComponentTypes{
public static final ComponentType<BlockPos> BOOKMARKED_POS = Registry.register(Registries.DATA_COMPONENT_TYPE,Identifier.of("voidcantation", "bookmarked_pos"),ComponentType.<BlockPos>builder().codec(BlockPos.CODEC).packetCodec(net.minecraft.network.codec.PacketCodecs.codec(BlockPos.CODEC)).build());
public static final ComponentType<VoidStorageData> VOID_STORAGE = Registry.register(Registries.DATA_COMPONENT_TYPE,Identifier.of("voidcantation", "void_storage"),ComponentType.<VoidStorageData>builder().codec(VoidStorageData.CODEC).packetCodec(net.minecraft.network.codec.PacketCodecs.codec(VoidStorageData.CODEC)).build());
public static void registerComponents(){

 }
}