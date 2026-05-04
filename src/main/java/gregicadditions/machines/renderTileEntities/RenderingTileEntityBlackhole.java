package gregicadditions.machines.renderTileEntities;

import gregicadditions.GTSounds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;



public class RenderingTileEntityBlackhole extends TileEntity implements ITickable {
    public RenderingTileEntityBlackhole() {
    }

    public int tickCount = 1;
    @Override
    public void update() {
        tickCount += 1;
        if (!world.isRemote) return;
        if (world.getTotalWorldTime() % 580 == 0) { // every 4 seconds
            world.playSound(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    GTSounds.BLACK_HOLE_AMBIENT,
                    SoundCategory.AMBIENT,
                    1.0f,
                    1.0f,
                    false
            );
        }
    }

}