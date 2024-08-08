package gregicadditions.blocks;

import com.google.common.collect.UnmodifiableIterator;
import gregicadditions.item.GAMetaBlocks;
import gregtech.api.unification.material.type.DustMaterial;
import gregtech.api.unification.ore.OrePrefix;
import gregtech.api.unification.ore.StoneType;
import gregtech.api.unification.ore.StoneTypes;
import gregtech.api.worldgen.config.OreConfigUtils;
import gregtech.common.blocks.BlockOre;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.*;
import java.util.stream.Collectors;

public class GABlockOre extends BlockOre {

    public DustMaterial material;

    public GABlockOre(DustMaterial material, OrePrefix orePrefix, StoneType[] allowedValues) {
        super(material, orePrefix, allowedValues);
        this.material = material;
    }

    public OrePrefix getOrePrefix() {
        return this.orePrefix;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        StoneType type = state.getValue(STONE_TYPE);


        if(type == StoneTypes.ENDSTONE || type == StoneTypes.NETHERRACK) {
            return super.getItemDropped(state, rand, fortune);
        }

        OrePrefix prefix = this.orePrefix;

        IBlockState originalOre = getOreForMaterial(this.material, prefix).get(StoneTypes.STONE);
        Item oreItem = Item.getItemFromBlock(originalOre.getBlock());
//
//        if(prefix.equals(OrePrefix.ore)) {
//            originalOre = getOreForMaterial(this.material, prefix).get(StoneTypes.STONE);
//            oreItem = Item.getItemFromBlock(originalOre.getBlock());
//        }

        return oreItem;
    }

    private static Map<StoneType, IBlockState> getOreForMaterial(DustMaterial material, OrePrefix prefix) {
        List<BlockOre> oreBlocks = (List) GAMetaBlocks.GA_ORES.stream().filter((ore) -> {
            return ore.material == material && ore.orePrefix == prefix;
        }).collect(Collectors.toList());
        HashMap<StoneType, IBlockState> stoneTypeMap = new HashMap();
        Iterator var3 = oreBlocks.iterator();

        while(var3.hasNext()) {
            BlockOre blockOre = (BlockOre)var3.next();
            UnmodifiableIterator var5 = blockOre.STONE_TYPE.getAllowedValues().iterator();

            while(var5.hasNext()) {
                StoneType stoneType = (StoneType)var5.next();
                IBlockState blockState = blockOre.getOreBlock(stoneType);
                stoneTypeMap.put(stoneType, blockState);
            }
        }

        if (stoneTypeMap.isEmpty()) {
            throw new IllegalArgumentException("There is no ore generated for material " + material);
        } else {
            return stoneTypeMap;
        }
    }
}