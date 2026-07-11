package gregicadditions.integrations.advancedrocketry;

import net.minecraftforge.fml.common.Optional;
import zmaster587.advancedRocketry.dimension.DimensionManager;

public class DimensionHandler {

    @Optional.Method(modid = "advancedrocketry")
    public static String getDimensionName(int id) {
        return DimensionManager.getInstance().getDimensionProperties(id).getName();

    }
}