package net.pocket.mod.component;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
public record PhaseDaggerData(boolean active, List<Integer> targetIds, boolean executing, int currentIndex, long startTick){
public static final Codec<PhaseDaggerData> CODEC = RecordCodecBuilder.create(instance-> instance.group(Codec.BOOL.fieldOf("active").forGetter(PhaseDaggerData::active),Codec.INT.listOf().fieldOf("target_ids").forGetter(PhaseDaggerData::targetIds),Codec.BOOL.fieldOf("executing").forGetter(PhaseDaggerData::executing),Codec.INT.fieldOf("current_index").forGetter(PhaseDaggerData::currentIndex),Codec.LONG.fieldOf("start_tick").forGetter(PhaseDaggerData::startTick)).apply(instance, PhaseDaggerData::new));
public static final PhaseDaggerData EMPTY = new PhaseDaggerData(false, new ArrayList<>(), false, 0, 0L);
 } 