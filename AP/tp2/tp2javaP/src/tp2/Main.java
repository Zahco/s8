package tp2;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<ArrayList<byte[]>> images = Utils.loadImages("/home/geoffrey/git/s8/AP/tp2/classe");
        Utils.viewGlyph(images.get(4).get(1));
    }
}