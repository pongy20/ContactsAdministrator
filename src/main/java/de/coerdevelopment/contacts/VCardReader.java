package de.coerdevelopment.contacts;

import ezvcard.Ezvcard;
import ezvcard.VCard;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VCardReader {

    public static List<VCard> readContacts(File file) {
        if (!file.getName().endsWith(".vcf")) {
            return null;
        }
        try {
            return Ezvcard.parse(file).all();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
