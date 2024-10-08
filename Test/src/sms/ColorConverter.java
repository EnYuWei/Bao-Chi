package sms;

public class ColorConverter 
{

	public static int[] LCHtoRGB(double L, double C, double H) {
	    // LCH 轉換為 Lab
	    double a1 = C * Math.cos(Math.toRadians(H));
	    double b1 = C * Math.sin(Math.toRadians(H));

	    // L* (明度) 被用來計算 XYZ 值
	    double Y = (L + 16) / 116;
	    double X = a1 / 500 + Y;
	    double Z = Y - b1 / 200;

	    // 確保 X, Y, Z 落在合理範圍內
	    if (Math.pow(Y, 3) > 0.008856) {
	        Y = Math.pow(Y, 3);
	    } else {
	        Y = (Y - 16.0 / 116.0) / 7.787;
	    }

	    if (Math.pow(X, 3) > 0.008856) {
	        X = Math.pow(X, 3);
	    } else {
	        X = (X - 16.0 / 116.0) / 7.787;
	    }

	    if (Math.pow(Z, 3) > 0.008856) {
	        Z = Math.pow(Z, 3);
	    } else {
	        Z = (Z - 16.0 / 116.0) / 7.787;
	    }

	    // XYZ 轉換為 sRGB
	    double r = X * 3.2406 + Y * -1.5372 + Z * -0.4986;
	    double g = X * -0.9689 + Y * 1.8758 + Z * 0.0415;
	    double b = X * 0.0557 + Y * -0.2040 + Z * 1.0570;

	    // 校正並限制在 0 - 255 範圍內
	    r = (r > 0.0031308) ? (1.055 * Math.pow(r, 1 / 2.4) - 0.055) : 12.92 * r;
	    g = (g > 0.0031308) ? (1.055 * Math.pow(g, 1 / 2.4) - 0.055) : 12.92 * g;
	    b = (b > 0.0031308) ? (1.055 * Math.pow(b, 1 / 2.4) - 0.055) : 12.92 * b;

	    // 將值限制在 [0, 255] 之間
	    int rInt = Math.max(0, Math.min(255, (int) Math.round(r * 255)));
	    int gInt = Math.max(0, Math.min(255, (int) Math.round(g * 255)));
	    int bInt = Math.max(0, Math.min(255, (int) Math.round(b * 255)));

	    return new int[]{rInt, gInt, bInt};
	}



}
