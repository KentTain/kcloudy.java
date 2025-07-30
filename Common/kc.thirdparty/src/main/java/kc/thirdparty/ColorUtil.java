package kc.thirdparty;

import java.util.regex.Pattern;

public class ColorUtil {
    private static String s = "0123456789ABCDEF";

    /**
     * RGB转换（240,248,255）成十六进制（#F0F8FF）
     *
     * @param r
     * @param g
     * @param b
     * @return #F0F8FF
     */
    public static String ConvertRGBToHex(int r, int g, int b) {
        String hex = "";
        if (r >= 0 && r < 256 && g >= 0 && g < 256 && b >= 0 && b < 256) {
            int x, y, z;
            x = r % 16;
            r = (r - x) / 16;
            y = g % 16;
            g = (g - y) / 16;
            z = b % 16;
            b = (b - z) / 16;
            hex = "#" + s.substring(r, r + 1) + s.substring(x, x + 1) + s.substring(g, g + 1) + s.substring(y, y + 1) + s.substring(b, b + 1) + s.substring(z, z + 1);
        }
        return hex;
    }

    /**
     * 十六进制（#F0F8FF）转换成RGB（240,248,255)
     *
     * @param hex
     * @return 240,248,255
     */
    public static String ConvertHexToRGB(String hex) {
        String rgb = "";
        String regex = "^[0-9A-F]{3}|[0-9A-F]{6}$";
        if (hex != null) {
            hex = hex.toUpperCase();
            if (hex.substring(0, 1).equals("#")) {
                hex = hex.substring(1);
            }
            if (Pattern.compile(regex).matcher(hex).matches()) {
                String a, c, d;
                String[] str = new String[3];
                for (int i = 0; i < 3; i++) {
                    a = hex.length() == 6 ? hex.substring(i * 2, i * 2 + 2) : hex.substring(i, i + 1) + hex.substring(i, i + 1);
                    c = a.substring(0, 1);
                    d = a.substring(1, 2);
                    str[i] = String.valueOf(s.indexOf(c) * 16 + s.indexOf(d));
                }
                rgb = str[0] + "," + str[1] + "," + str[2];
            }
        }
        return rgb;
    }
}
