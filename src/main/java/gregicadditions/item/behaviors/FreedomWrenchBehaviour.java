package gregicadditions.item.behaviors;

import gregicadditions.client.renderer.WorldRenderEventRenderer;
import gregicadditions.jei.JEIOptional;
import gregicadditions.network.CPacketMultiBlockStructure;
import gregicadditions.utils.BlockPatternChecker;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.items.metaitem.stats.IItemBehaviour;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.net.NetworkHandler;
import gregtech.api.render.scene.WorldSceneRenderer;
import gregtech.common.sound.GTSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FreedomWrenchBehaviour implements IItemBehaviour {
    private static final String TRANSLATION_KEY_SPIN = "metaitem.freedom_wrench.spin";
    private static final String TRANSLATION_KEY_MODE = "metaitem.freedom_wrench.mode.";
    private static final String NBT_TAG = "GT.Detrav";
    private static final String MODE_STR = "mode";
    private static final float EDGE_MIN = 0.25F;
    private static final float EDGE_MAX = 0.75F;
    private static final byte MODE_BUILD = 3;
    private static final byte MODE_COUNT = 4;

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (pos == null) {
            return EnumActionResult.FAIL;
        }

        if (!(world.getTileEntity(pos) instanceof MetaTileEntityHolder)) {
            return EnumActionResult.FAIL;
        }

        MetaTileEntityHolder holder = (MetaTileEntityHolder) world.getTileEntity(pos);
        MetaTileEntity mte = holder.getMetaTileEntity();

        if (!(mte instanceof MultiblockControllerBase)) {
            return EnumActionResult.SUCCESS;
        }

        NBTTagCompound nbt = player.getHeldItem(hand).getOrCreateSubCompound(NBT_TAG);
        byte mode = nbt.getByte(MODE_STR);

        if (player.isSneaking()) {
            rotate(mte, player, world, side, hitX, hitY, hitZ);
        } else {
            handlePreview((MultiblockControllerBase) mte, player, world, pos, mode);
        }

        return EnumActionResult.SUCCESS;
    }

    private void handlePreview(MultiblockControllerBase controller, EntityPlayer player, World world, BlockPos pos, byte mode) {
        if (!world.isRemote) {
            return;
        }

        if (!Loader.isModLoaded("jei")) {
            player.sendMessage(new TextComponentTranslation("metaitem.freedom_wrench.jei_missing"));
            return;
        }

        player.playSound(GTSoundEvents.WRENCH,1F,1F);

        if (mode == MODE_BUILD) {
            build(controller, world);
            return;
        }

        WorldRenderEventRenderer.renderMultiBlockPreview(controller, 60000, mode);
    }

    private void rotate(MetaTileEntity mte, EntityPlayer player, World world, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean rotateSpin = false;
        EnumFacing facing = mte.getFrontFacing();

        boolean centerX = isCenter(hitX);
        boolean centerY = isCenter(hitY);
        boolean centerZ = isCenter(hitZ);

        if (side == EnumFacing.DOWN || side == EnumFacing.UP) {
            if (centerX && centerZ) {
                if (facing == side) {
                    rotateSpin = true;
                } else {
                    facing = side;
                }

            } else if (hitX < EDGE_MIN && centerZ) {
                facing = EnumFacing.WEST;

            } else if (hitX > EDGE_MAX && centerZ) {
                facing = EnumFacing.EAST;

            } else if (hitZ < EDGE_MIN && centerX) {
                facing = EnumFacing.NORTH;

            } else if (hitZ > EDGE_MAX && centerX) {
                facing = EnumFacing.SOUTH;

            } else {
                facing = facing.getOpposite();
            }

        } else {
            boolean centerHorizontal = side.getXOffset() == 0 ? centerX : centerZ;
            if (centerY && centerHorizontal) {
                if (facing == side) {
                    rotateSpin = true;
                } else {
                    facing = side;
                }

            } else if (centerHorizontal && hitY < EDGE_MIN) {

                facing = EnumFacing.DOWN;

            } else if (centerHorizontal && hitY > EDGE_MAX) {

                facing = EnumFacing.UP;

            } else if (centerY && isLeftSide(side, hitX, hitZ)) {

                facing = side.rotateY().getOpposite();

            } else if (centerY && isRightSide(side, hitX, hitZ)) {

                facing = side.rotateY();

            } else {

                facing = side.getOpposite();
            }
        }

        if (rotateSpin) {
            rotateSpin(mte, player, world, facing);
            return;
        }

        if (facing != mte.getFrontFacing()) {
            if (world.isRemote) {
                return;
            }

            world.playSound(null, player.getPosition(),GTSoundEvents.WRENCH,SoundCategory.PLAYERS,1F,1F);
            mte.setFrontFacing(facing);
            player.sendMessage(new TextComponentTranslation("metaitem.freedom_wrench.facing", facing));
        }
    }

    private void rotateSpin(MetaTileEntity mte, EntityPlayer player, World world, EnumFacing facing) {
        if (world.isRemote) {
            return;
        }

        world.playSound(null, player.getPosition(),GTSoundEvents.WRENCH,SoundCategory.PLAYERS,1F,1F);

        EnumFacing next = BlockPatternChecker.getSpin(mte).rotateY();
        BlockPatternChecker.setSpin(mte, next);

        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP) {
            player.sendMessage(new TextComponentTranslation(TRANSLATION_KEY_SPIN,
                    next == EnumFacing.NORTH ? "up" :
                            next == EnumFacing.EAST ? "right" :
                                    next == EnumFacing.SOUTH ? "down" : "left"));

        } else {
            player.sendMessage(new TextComponentTranslation(TRANSLATION_KEY_SPIN, next));
        }
    }

    private void build(MultiblockControllerBase controller, World world) {
        WorldSceneRenderer renderer = JEIOptional.getWorldSceneRenderer(controller);

        if (renderer == null) {
            return;
        }

        List<BlockPos> renderedBlocks = ObfuscationReflectionHelper.getPrivateValue(WorldSceneRenderer.class, renderer, "renderedBlocks");

        if (renderedBlocks == null) {
            return;
        }

        List<ItemStack> map = new ArrayList<>();
        Set<BlockPos> existingPositions = new HashSet<>();
        List<CPacketMultiBlockStructure.BlockInfo> blockInfos = new ArrayList<>();
        EnumFacing referenceFacing = EnumFacing.EAST;
        BlockPos referencePos = BlockPos.ORIGIN;

        for (BlockPos blockPos : renderedBlocks) {
            MetaTileEntity metaTE = BlockMachine.getMetaTileEntity(renderer.world, blockPos);

            if (metaTE instanceof MultiblockControllerBase && metaTE.metaTileEntityId.equals(controller.metaTileEntityId)) {
                referencePos = blockPos;
                referenceFacing = metaTE.getFrontFacing();
                break;
            }
        }


        for (BlockPos blockPos : renderedBlocks) {
            if (blockPos.equals(referencePos)) {
                continue;
            }

            EnumFacing frontFacing = controller.getFrontFacing();
            EnumFacing spin = BlockPatternChecker.getSpin(controller);

            BlockPos realPos = BlockPatternChecker.getActualPos(referenceFacing, frontFacing, spin,
                    blockPos.getX() - referencePos.getX(),
                    blockPos.getY() - referencePos.getY(),
                    blockPos.getZ() - referencePos.getZ()
            ).add(controller.getPos());

            if (!world.isAirBlock(realPos) || renderer.world.isAirBlock(blockPos)) {
                continue;
            }

            IBlockState blockState = renderer.world.getBlockState(blockPos);

            MetaTileEntity metaTileEntity = BlockMachine.getMetaTileEntity(renderer.world, blockPos);
            ItemStack stack = blockState.getBlock().getItem(renderer.world, blockPos, blockState);

            if (metaTileEntity != null) {
                stack = metaTileEntity.getStackForm();
            }

            if (map.stream().noneMatch(stack::isItemEqual)) {
                map.add(stack);
            }

            if (!existingPositions.add(realPos)) {
                continue;
            }

            blockInfos.add(new CPacketMultiBlockStructure.BlockInfo(realPos,
                    getStackIndex(map, stack),
                    metaTileEntity != null ? BlockPatternChecker.getActualFrontFacing(referenceFacing, frontFacing, spin, metaTileEntity.getFrontFacing()) : EnumFacing.SOUTH));
        }

        List<List<CPacketMultiBlockStructure.BlockInfo>> chunks = chunkMultiStructure(blockInfos);

        for (List<CPacketMultiBlockStructure.BlockInfo> chunk : chunks) {
            NetworkHandler.channel.sendToServer(
                    new CPacketMultiBlockStructure(map, chunk, world.provider.getDimension()).toFMLPacket()
            );
        }
    }

    private <T> List<List<T>> chunkMultiStructure(List<T> list) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 30) {
            chunks.add(list.subList(i, Math.min(list.size(), i + 30)));
        }
        return chunks;
    }

    private int getStackIndex(List<ItemStack> stacks, ItemStack target) {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).isItemEqual(target)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isCenter(float value) {
        return EDGE_MIN < value && value < EDGE_MAX;
    }

    private boolean isLeftSide(EnumFacing side, float hitX, float hitZ) {
        if (side.getXOffset() == 0) {
            return side.getZOffset() < 0 ? hitX < EDGE_MIN : hitX > EDGE_MAX;
        }

        return side.getXOffset() > 0 ? hitZ < EDGE_MIN : hitZ > EDGE_MAX;
    }

    private boolean isRightSide(EnumFacing side, float hitX, float hitZ) {
        if (side.getXOffset() == 0) {
            return side.getZOffset() > 0 ? hitX < EDGE_MIN : hitX > EDGE_MAX;
        }

        return side.getXOffset() < 0 ? hitZ < EDGE_MIN : hitZ > EDGE_MAX;
    }

    private byte getMode(ItemStack stack) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound(NBT_TAG);
        return nbt.hasKey(MODE_STR) ? nbt.getByte(MODE_STR) : 0;
    }

    private void setMode(ItemStack stack, byte mode) {
        stack.getOrCreateSubCompound(NBT_TAG).setByte(MODE_STR, mode);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        world.playSound(null,player.getPosition(), GTSoundEvents.WRENCH,SoundCategory.PLAYERS,1F,1F);

        ItemStack stack = player.getHeldItem(hand);
        byte mode = getMode(stack);

        if (player.isSneaking()) {
            mode = (byte) ((mode + 1) % MODE_COUNT);
            setMode(stack, mode);

            if (world.isRemote) {
                player.sendMessage(new TextComponentTranslation(TRANSLATION_KEY_MODE + mode));
            }

        } else {
            if (world.isRemote) {
                WorldRenderEventRenderer.renderMultiBlockPreview(null, 0, mode);
                player.sendMessage(new TextComponentTranslation("metaitem.freedom_wrench.clear"));
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack itemStack, List<String> lines) {
        byte mode = getMode(itemStack);

        lines.add(I18n.format(TRANSLATION_KEY_MODE + mode));
        lines.add(I18n.format("metaitem.freedom_wrench.info.0"));
        lines.add(I18n.format("metaitem.freedom_wrench.info.1"));
        lines.add(I18n.format("metaitem.freedom_wrench.info.2"));
        lines.add(I18n.format("metaitem.freedom_wrench.info.3"));
    }
}