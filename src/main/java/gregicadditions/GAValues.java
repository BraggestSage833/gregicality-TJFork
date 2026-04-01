package gregicadditions;

import static net.minecraft.util.text.TextFormatting.*;

public class GAValues {

    public static final int[] QUBIT = new int[]{1, 16, 256, 4096, 65536, 1048576, 16777216, 268435456};
    public static final int[] V = new int[]{8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, 2097152, 8388608, 33554432, 134217728, 536870912, Integer.MAX_VALUE};
    public static final long[] VOC = { 8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, 2097152, 8388608, 33554432,
            134217728, 536870912, Integer.MAX_VALUE, 8589934592L, 34359738368L, 137438953472L, 549755813888L,
            2199023255552L,
            8796093022208L, 35184372088832L, 140737488355328L, 562949953421312L, 2251799813685248L, 9007199254740992L,
            36028797018963968L, 144115188075855872L, 576460752303423488L, 2305843009213693952L, Long.MAX_VALUE };
    public static final int[] VA = new int[]{7, 30, 120, 480, 1920, 7680, 30720, 122880, 491520, 1966080, 7864320, 31457280, 125829120, 503316480, 2013265920};
    public static final int ULV = 0;
    public static final int LV = 1;
    public static final int MV = 2;
    public static final int HV = 3;
    public static final int EV = 4;
    public static final int IV = 5;
    public static final int LuV = 6;
    public static final int ZPM = 7;
    public static final int UV = 8;
    public static final int UHV = 9;
    public static final int UEV = 10;
    public static final int UIV = 11;
    public static final int UMV = 12;
    public static final int UXV = 13;
    public static final int MAX = 14;
    public static final int MAX_TRUE = 30;
    public static final String MAX_PLUS = RED.toString() + BOLD + "M" + YELLOW + BOLD + "A" + GREEN + BOLD + "X" +
            AQUA + BOLD + "+" + LIGHT_PURPLE + BOLD;

    public static final String[] VN = new String[] {"ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV", "UIV", "UMV", "UXV", "MAX","MAX+",
            MAX_PLUS + "1", MAX_PLUS + "2", MAX_PLUS + "3", MAX_PLUS + "4",
            MAX_PLUS + "5", MAX_PLUS + "6", MAX_PLUS + "7", MAX_PLUS + "8",
            MAX_PLUS + "9", MAX_PLUS + "10", MAX_PLUS + "11", MAX_PLUS + "12",
            MAX_PLUS + "13", MAX_PLUS + "14", MAX_PLUS + "15", MAX_PLUS + "16", };
    public static final String[] VOCN = new String[] { "ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV",
            "UEV", "UIV", "UXV", "OpV", "MAX", "MAX+1", "MAX+2", "MAX+3", "MAX+4", "MAX+5", "MAX+6", "MAX+7", "MAX+8",
            "MAX+9", "MAX+10", "MAX+11", "MAX+12", "MAX+13", "MAX+14", "MAX+15", "MAX+16",
    };
    public static final int[] VC = new int[] {0xDCDCDC, 0xDCDCDC, 0xFF6400, 0xFFFF1E, 0x808080, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5, 0xF0F0F5};
    public static final String[] VOLTAGE_NAMES = new String[]{"Ultra Low Voltage", "Low Voltage", "Medium Voltage",
            "High Voltage", "Extreme Voltage", "Insane Voltage", "Ludicrous Voltage", "ZPM Voltage", "Ultimate Voltage",
            "Highly Ultimate Voltage", "Extremely Ultimate Voltage", "Insanely Ultimate Voltage", "Mega Ultimate Voltage",
            "Extended Mega Ultimate Voltage", "Maximum Voltage"};



    public static final String MODID_FR = "forestry";
    public static final String MODID_BEES = "extrabees";
    public static final String MODID_IC2 = "ic2";
    public static final String MODID_BINNIE = "binniecore";
    public static final String MODID_XU2 = "extrautils2";
    public static final String MODID_TR = "techreborn";
    public static final String MODID_TCON = "tconstruct";
    public static final String MODID_EXNI = "exnihilocreatio";
    public static final String MODID_OC = "opencomputers";
    public static final String MODID_GTOC = "gtce2oc";
    public static final String MODID_MYSTAGGRA = "mysticalagradditions";

}
