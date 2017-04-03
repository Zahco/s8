package tp2;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<ArrayList<byte[]>> images = Utils.loadImages("/home/m1gil/spaurgeo/s8/AP/tp2/classe");
        ArrayList<ArrayList<LabelledData>> glyphes = Utils.convertToLabelledData(images);
        LabelledData glyphe = glyphes.get(6).get(1);

        Utils.viewGlyph(images.get(6).get(1));
        ArrayList<LabelledData> k_pp_voisins = Utils.k_pp_voisins(glyphe, Utils.randomGlyphe(glyphes, 100), 21);
        System.out.println("Classe: " + Utils.classe_principal(k_pp_voisins));


//        ArrayList<ArrayList<LabelledData>> multiRandomGlyphes = Utils.multiRandomGlyphe(images, 100, 20);
    }
}