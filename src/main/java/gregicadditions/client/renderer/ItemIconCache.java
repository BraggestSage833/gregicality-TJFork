package gregicadditions.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class ItemIconCache {
    private static final int SIZE = 32;
    private static final int MAX_ENTRIES = 256;
    private static final int MAX_BUILDS_PER_FRAME = 8;
    private static final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);

    private static final Map<Key, Framebuffer> cache = new LinkedHashMap<>(64, 0.75f, true) {

        @Override
        protected boolean removeEldestEntry(Map.Entry<Key, Framebuffer> eldest) {
            if (size() <= MAX_ENTRIES) {
                return false;
            }

            eldest.getValue().deleteFramebuffer();

            return true;
        }
    };

    private static final Map<Key, ItemStack> pending = new LinkedHashMap<>();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ItemIconCache.class);
    }

    /** Draws a cached icon. Returns false if the caller should render the live item instead. */
    public static boolean tryDrawCached(ItemStack stack, float x, float y, float size, float z) {
        if (stack.isEmpty() || stack.hasEffect() || !OpenGlHelper.isFramebufferEnabled()) {
            return false;
        }

        Framebuffer fb = cache.get(new Key(stack));

        if (fb == null) {
            ItemStack copy = stack.copy();
            Key key = new Key(copy);
            if (!pending.containsKey(key)) {
                pending.put(key, copy);
            }

            return false;
        }

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.bindTexture(fb.framebufferTexture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y + size, z).tex(0, 0).endVertex();
        builder.pos(x + size, y + size, z).tex(1, 0).endVertex();
        builder.pos(x + size, y, z).tex(1, 1).endVertex();
        builder.pos(x, y, z).tex(0, 1).endVertex();
        tessellator.draw();
        GlStateManager.disableAlpha();
        return true;
    }



    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START || pending.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<Key, ItemStack>> it = pending.entrySet().iterator();
        for (int n = 0; n < MAX_BUILDS_PER_FRAME && it.hasNext(); n++) {
            Map.Entry<Key, ItemStack> e = it.next();
            it.remove();
            cache.put(e.getKey(), build(e.getValue()));
        }
    }

    @SubscribeEvent
    public static void onTexturesStitched(TextureStitchEvent.Post event) {
        for (Framebuffer framebuffer : cache.values()) {
            framebuffer.deleteFramebuffer();
        }

        cache.clear();
        pending.clear();
    }

    private static Framebuffer build(ItemStack stack) {
        int prevFrameBufferObject = GL11.glGetInteger(GL30.GL_FRAMEBUFFER_BINDING);
        VIEWPORT.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);

        Framebuffer framebuffer = new Framebuffer(SIZE, SIZE, true);
        framebuffer.setFramebufferColor(0, 0, 0, 0);
        framebuffer.setFramebufferFilter(GL11.GL_LINEAR);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, 16, 16, 0, -300, 300);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        OpenGlHelper.glBindFramebuffer(OpenGlHelper.GL_FRAMEBUFFER, prevFrameBufferObject);
        GlStateManager.viewport(VIEWPORT.get(0), VIEWPORT.get(1), VIEWPORT.get(2), VIEWPORT.get(3));
        return framebuffer;
    }


    private static final class Key {
        final Item item;
        final int meta;
        final NBTTagCompound tag;
        final int hash;


        Key(ItemStack itemStack) {
            item = itemStack.getItem();
            meta = itemStack.getMetadata();
            tag = itemStack.getTagCompound();
            hash = 31 * (31 * item.hashCode() + meta) + (tag == null ? 0 : tag.hashCode());
        }

        @Override public int hashCode() {
            return hash;
        }

        @Override public boolean equals(Object object) {
            if (!(object instanceof Key key)) {
                return false;
            }

            return item == key.item && meta == key.meta && Objects.equals(tag, key.tag);
        }
    }
}
