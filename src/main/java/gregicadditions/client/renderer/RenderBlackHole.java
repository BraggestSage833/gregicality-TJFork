package gregicadditions.client.renderer;


import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;


/*
Code originally made by aeddddd for the mod AE2Enhanced
 */

public class RenderBlackHole extends TileEntitySpecialRenderer<RenderingTileEntityBlackhole> {

    @Override
    public boolean isGlobalRenderer(RenderingTileEntityBlackhole te) {
        return true;
    }

    private static final double EVENT_HORIZON_RADIUS = 3.5;
    private static final double INNER_HALO_BASE = 3.2;
    private static final double MID_HALO_BASE = 5.6;
    private static final double OUTER_HALO_BASE = 7.0;

    private static final int LATITUDE_SEGMENTS = 24;
    private static final int LONGITUDE_SEGMENTS = 24;
    private static final int GRID_LAT = 8;
    private static final int GRID_LON = 12;

    private static final float ROTATION_SPEED = 0.25f;

    @Override
    public void render(RenderingTileEntityBlackhole te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        float time = (te.tickCount + partialTicks) * ROTATION_SPEED;

        float expand = 0.5f + 0.5f * (float) Math.sin(time * 0.5);

        float brightness = 0.35f + 0.65f * (0.5f + 0.5f * (float) Math.sin(time * 0.35));
        //for wireframe
        float gridEnergy = 0.5f + 0.5f * (float) Math.sin(time * 1.4);

        //radius of layers
        double innerR = INNER_HALO_BASE * (0.82 + 0.36 * expand);
        double midR = MID_HALO_BASE * (0.88 + 0.24 * (1.0f - expand * 0.5f));
        double outerR = OUTER_HALO_BASE * (0.92 + 0.16 * expand);

        float innerAlpha = 0.10f + 0.28f * brightness;
        float midAlpha = 0.06f + 0.14f * (1.0f - expand * 0.3f);
        float outerAlpha = 0.04f + 0.10f * brightness;

        GlStateManager.pushMatrix();
        GlStateManager.translate((x + .5), (y + .5 ), ( z + .5 ));

        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
        );
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        try {

            drawSphere(EVENT_HORIZON_RADIUS, 0x000000, 0.99f);

            GlStateManager.pushMatrix();
            GlStateManager.rotate(time * 0.5f, 0, 1, 0);
            GlStateManager.rotate(18.0f, 1, 0, 0.3f);
            drawSphere(innerR, 0x140029, innerAlpha);

           // drawWireframeSphere(innerR, 0x7700DD, 0.28f * (0.5f + 0.5f * gridEnergy));
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.rotate(-time * 0.3f, 0, 1, 0);
            GlStateManager.rotate(12.0f, 0.5f, 0, 1.0f);
            drawSphere(midR, 0x05000D, midAlpha);
      //wirerame sphere leaving in case we need it elsewere
            //drawWireframeSphere(midR, 0x110022, 0.08f * (0.5f + 0.5f * gridEnergy));
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.rotate(time * 0.12f, 0, 1, 0);
            GlStateManager.rotate(8.0f, 1, 0.2f, 0);
            drawSphere(outerR, 0x020005, outerAlpha);
            GlStateManager.popMatrix();

        } finally {
            GlStateManager.shadeModel(GL11.GL_FLAT);
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            GlStateManager.popMatrix();
        }



    }

    private void drawSphere(double radius, int color, float alpha) {
        if (alpha <= 0.01f) return;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_COLOR);

        double[] v00 = new double[3];
        double[] v01 = new double[3];
        double[] v10 = new double[3];
        double[] v11 = new double[3];

        for (int lat = 0; lat < LATITUDE_SEGMENTS; lat++) {
            double theta0 = Math.PI * lat / LATITUDE_SEGMENTS;
            double theta1 = Math.PI * (lat + 1) / LATITUDE_SEGMENTS;

            for (int lon = 0; lon < LONGITUDE_SEGMENTS; lon++) {
                double phi0 = 2 * Math.PI * lon / LONGITUDE_SEGMENTS;
                double phi1 = 2 * Math.PI * (lon + 1) / LONGITUDE_SEGMENTS;

                sphereVertex(radius, theta0, phi0, v00);
                sphereVertex(radius, theta0, phi1, v01);
                sphereVertex(radius, theta1, phi0, v10);
                sphereVertex(radius, theta1, phi1, v11);

                addTriangle(buffer, v00, v10, v01, r, g, b, alpha);
                addTriangle(buffer, v01, v10, v11, r, g, b, alpha);
            }
        }

        tessellator.draw();
    }

    private void drawWireframeSphere(double radius, int color, float alpha) {
        if (alpha <= 0.01f) return;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        GlStateManager.glLineWidth(2.0f);

      //likewise
        for (int lat = 1; lat < GRID_LAT; lat++) {
            double theta = Math.PI * lat / GRID_LAT;
            double y = radius * Math.cos(theta);
            double radH = radius * Math.sin(theta);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);

            for (int lon = 0; lon <= GRID_LON; lon++) {
                double phi = 2 * Math.PI * lon / GRID_LON;
                buffer.pos(radH * Math.cos(phi), y, radH * Math.sin(phi))
                        .color(r, g, b, alpha).endVertex();
            }
            tessellator.draw();
        }

        for (int lon = 0; lon < GRID_LON; lon++) {
            double phi = 2 * Math.PI * lon / GRID_LON;

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

            for (int lat = 0; lat <= GRID_LAT; lat++) {
                double theta = Math.PI * lat / GRID_LAT;
                buffer.pos(
                        radius * Math.sin(theta) * Math.cos(phi),
                        radius * Math.cos(theta),
                        radius * Math.sin(theta) * Math.sin(phi)
                ).color(r, g, b, alpha).endVertex();
            }
            tessellator.draw();
        }

        GlStateManager.glLineWidth(1.0f);
    }

    private void sphereVertex(double radius, double theta, double phi, double[] out) {
        out[0] = radius * Math.sin(theta) * Math.cos(phi);
        out[1] = radius * Math.cos(theta);
        out[2] = radius * Math.sin(theta) * Math.sin(phi);
    }

    private void addTriangle(BufferBuilder buffer, double[] a, double[] b, double[] c,
    float r, float g, float blue, float alpha) {
        buffer.pos(a[0], a[1], a[2]).color(r, g, blue, alpha).endVertex();
        buffer.pos(b[0], b[1], b[2]).color(r, g, blue, alpha).endVertex();
        buffer.pos(c[0], c[1], c[2]).color(r, g, blue, alpha).endVertex();
    }
}