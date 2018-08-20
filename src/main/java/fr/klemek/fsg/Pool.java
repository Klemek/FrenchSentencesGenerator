package fr.klemek.fsg;

import java.util.ArrayList;
import java.util.Arrays;

enum Pool {

    GN("sujets"),

    VT("verbes/transitifs"),
    PR_T("verbes/transitifs"),
    VN("verbes/intransitifs"),
    PR_N("verbes/intransitifs"),
    VM("verbes/modaux_simples"),
    VD("verbes/modaux_suivis_de_de"),
    VA("verbes/modaux_suivis_de_a"),
    VOA("verbes/preposes_de_a"),
    VOD("verbes/preposes_de_de"),
    VOI("verbes/proposes_de_cod_coi"),
    VTL("verbes/preposes_de_lieu"),
    VAV("verbes/preposes_de_avec"),
    VET("verbes/preposes_de_et"),
    VOS("verbes/preposes_de_sur"),

    CO("complements/objet_direct"),
    CL("complements/lieu"),
    CT("complements/temps"),

    AP("adverbes/postpose"),
    AF("adverbes/fin"),

    AI(null, "pourquoi", "en quel honneur", "à quel titre", "à quelle fin", "en vertu de quel droit", "mais pourquoi"),
    AIL(null, "où", "à quel endroit", "en quel lieu", "où donc", "où diable", "putain mais où", "dans quel pays", "dans quelle ville"),
    AIT(null, "quand", "à quel moment", "à quelle occasion"),
    AIM(null, "comment", "de quelle manière", "dans quelle mesure"),

    VG(null, ","),
    PP(null, ":"),
    PV(null, ";"),
    PF(null, ".", " !", "...", " ?"),

    NEG(null, "pas", "plus", "pas encore", "presque plus", "point", "guère", "jamais", "presque jamais", "plus jamais", "pas du tout", "pas vraiment"),

    //NOMINAL GROUPS

    GN_NC("groupes_nominaux/noms_communs"),
    GN_CNC("groupes_nominaux/complements_nc"),
    GN_NP("groupes_nominaux/noms_propres"),
    GN_VNP("groupes_nominaux/variantes_np"),

    //OBJECT COMPLEMENT

    CO_APO("complements_dobjets/adjectifs_post"),
    CO_APR("complements_dobjets/adjectifs_pre"),
    CO_NC("complements_dobjets/noms_communs"),

    //OTHER

    QT("quantifieurs"),;

    final String file;
    final ArrayList<String> data;

    Pool(String file) {
        this.file = file;
        this.data = new ArrayList<>(0);
    }

    Pool(String file, String... list) {
        this(file);
        this.data.addAll(Arrays.asList(list));
    }

    String getOne() {
        return this.data.get(Utils.dice(this.data.size()) - 1);
    }

    String getOne(int[] weights) {
        return this.data.get(Utils.dice(weights) - 1);
    }

    String get(int i) {
        return this.data.get(i);
    }
}
