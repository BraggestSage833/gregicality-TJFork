package gregicadditions.machines.multi;

import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.multiblock.BlockWorldState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.*;

public class CasingLinks {
    public static final int DATA_ID = 469;

    private static final Map<BlockPos, MultiblockControllerBase> LINKS = new HashMap<>();
    private static final Map<MultiblockControllerBase, Set<BlockPos>> BY_CONTROLLER = new HashMap<>();

    private static final Map<IBlockAccess, WeakReference<MultiblockControllerBase>> PREVIEW = Collections.synchronizedMap(new WeakHashMap<>());

    public static void send(MultiblockControllerBase controller, Collection<BlockPos> positions) {
        if (controller.getWorld() == null || positions == null || controller.getWorld().isRemote) {
            return;
        }

        controller.writeCustomData(DATA_ID, packetBuffer -> write(packetBuffer, positions));
    }

    public static void write(PacketBuffer buffer, Collection<BlockPos> positions) {
        buffer.writeVarInt(positions.size());
        for (BlockPos pos : positions) {
            buffer.writeLong(pos.toLong());
        }
    }

    public static List<BlockPos> read(PacketBuffer buffer) {
        int bufferSize = buffer.readVarInt();
        List<BlockPos> positions = new ArrayList<>(bufferSize);
        for (int i = 0; i< bufferSize; i++) {
            positions.add(BlockPos.fromLong(buffer.readLong()));
        }
        return positions;
    }


    public static void set(MultiblockControllerBase controller, Collection<BlockPos> positions) {
        Set<BlockPos> old = BY_CONTROLLER.remove(controller);
        if (old != null) {
            for (BlockPos pos : old) {
                LINKS.remove(pos, controller);
            }
        }

        if (!positions.isEmpty()) {
            BY_CONTROLLER.put(controller, new HashSet<>(positions));
            for (BlockPos pos : positions) {
                LINKS.put(pos, controller);
            }
        }

        World world = controller.getWorld();
        if (world == null) {
            return;
        }
        if (old != null) {
            for (BlockPos pos : old) {
                world.markBlockRangeForRenderUpdate(pos, pos);
            }
        }

        for (BlockPos pos : positions) {
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    public static MultiblockControllerBase get(BlockPos pos) {
        MultiblockControllerBase controller = LINKS.get(pos);
        return controller != null && controller.isValid() ? controller : null;
    }

    public static void clear() {
        LINKS.clear();
        BY_CONTROLLER.clear();
    }

    public static void registerPreview(IBlockAccess world, MultiblockControllerBase controller) {
        if (world == null) {
            return;
        }

        if (controller == null) {
            PREVIEW.remove(world);
        }
        else {
            PREVIEW.put(world, new WeakReference<>(controller));
        }
    }

    public static MultiblockControllerBase getPreview(IBlockAccess world) {
        WeakReference<MultiblockControllerBase> reference = PREVIEW.get(world);
        return reference == null ? null : reference.get();
    }

    public static void recordCasing(BlockWorldState state) {
        state.getMatchContext().getOrCreate("casingPos", HashSet::new).add(state.getPos().toImmutable());

    }

}

