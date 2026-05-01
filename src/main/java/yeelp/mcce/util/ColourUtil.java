package yeelp.mcce.util;

@SuppressWarnings("MagicNumber")
public abstract class ColourUtil {

    public static short[] HSLtoRGB(float h) {
        return HSLtoRGB(h, 1, 0.5f);
    }

    public static short[] HSLtoRGB(float h, float s, float l) {
        float c = (1 - Math.abs(2 * l - 1)) * s;
        float x = c * (1 - Math.abs(((h / 60) % 2) - 1));
        float m = l - c / 2;
        float rPrime, gPrime, bPrime;
        switch((int) h / 60) {
            case 0:
                rPrime = c;
                gPrime = x;
                bPrime = 0;
                break;
            case 1:
                rPrime = x;
                gPrime = c;
                bPrime = 0;
                break;
            case 2:
                rPrime = 0;
                gPrime = c;
                bPrime = x;
                break;
            case 3:
                rPrime = 0;
                gPrime = x;
                bPrime = c;
                break;
            case 4:
                rPrime = x;
                gPrime = 0;
                bPrime = c;
                break;
            case 5:
                rPrime = c;
                gPrime = 0;
                bPrime = x;
                break;
            default:
                rPrime = 0;
                gPrime = 0;
                bPrime = 0;
                break;
        }
        return new short[] {
                (short) ((rPrime + m) * 255),
                (short) ((gPrime + m) * 255),
                (short) ((bPrime + m) * 255)};
    }

    @SuppressWarnings("unused")
    public static float[] RGBtoHSL(byte r, byte g, byte b) {
        float rPrime = r/255.0f;
        float gPrime = g/255.0f;
        float bPrime = b/255.0f;
        float cMax = Math.max(Math.max(rPrime, gPrime), bPrime);
        float cMin = Math.min(Math.min(rPrime, gPrime), bPrime);
        float delta = cMax - cMin;
        float h = 60, l = (cMax + cMin)/2.0f, s = delta/(1 - Math.abs(2 * l - 1));
        if(delta == 0) {
            h *= 0;
        }
        else if(cMax == rPrime) {
            h *= ((gPrime - bPrime)/delta) % 6;
        }
        else if(cMax == gPrime) {
            h *= ((bPrime - rPrime)/delta) + 2;
        }
        else {
            //cMax == bPrime
            h *= ((rPrime - gPrime)/delta) + 4;
        }
        return new float[] {h, s, l};
    }
}
