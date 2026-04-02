package gregicadditions.capabilities.impl;

import gregicadditions.GAValues;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.RecipeMap;

public abstract class GAAbstractRecipeLogic extends AbstractRecipeLogic {
    public GAAbstractRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap) {
        super(tileEntity, recipeMap);
    }

    @Override
    protected long[] calculateOverclock(long EUt, long voltage, int duration) {
        if (!allowOverclocking) {
            return new long[]{EUt, duration};
        }
        boolean negativeEU = EUt < 0;
        int tier = getOverclockingTier(voltage);
        if (GAValues.VOC[tier] <= EUt || tier == 0)
            return new long[]{EUt, duration};
        if (negativeEU)
            EUt = -EUt;
            long resultEUt = EUt;
            double resultDuration = duration;
            //do not overclock further if duration is already too small
            while (resultDuration >= 1 && resultEUt <= GAValues.VOC[tier - 1]) {
                resultEUt *= 4;
                resultDuration /= 2.8;
            }
            return new long[]{negativeEU ? -resultEUt : resultEUt, (int) Math.ceil(resultDuration)};
    }
}
