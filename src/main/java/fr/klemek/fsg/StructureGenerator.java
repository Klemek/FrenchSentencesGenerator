package fr.klemek.fsg;

import java.util.ArrayList;
import java.util.List;

class StructureGenerator {

    private static String[] NM_VERBS = {"VT", "VN", "VOA", "VOD", "VOI", "VTL", "VAV", "VET", "VOS"};
    private static int[] PROBS_NM_VERBS = {5, 5, 3, 3, 2, 2, 1, 1, 2};

    private static String[] END_OPTS = {"CT", "CL", "AF"};
    private static int[] PROBS_END_OPTS = {3, 3, 5};

    static List<String> generate() {
        ArrayList<String> str = new ArrayList<>();
        boolean flagCT = false;
        boolean flagCL = false;
        boolean flagAP = false;

        if (Utils.dice(100) < 7) {
            if (Utils.dice(100) < 50) {
                str.add("CT");
                flagCT = true;
            } else {
                str.add("CL");
                flagCL = true;
            }
            str.add("VG");
        }

        str.add("GN");

        if (Utils.dice(100) < 26) {
            int diceModal = Utils.dice(100);
            if (diceModal < 37) {
                str.add("VM");
            } else if (diceModal < 69) {
                str.add("VD");
                str.add("§de");
            } else {
                str.add("VA");
                str.add("§à");
            }

            if (Utils.dice(100) < 4) {
                str.add("AP");
                flagAP = true;
            }
        }

        String verb;
        do {
            verb = Utils.getByDice(StructureGenerator.NM_VERBS, StructureGenerator.PROBS_NM_VERBS);
        } while (flagCL && verb.equals("VTL"));
        str.add(verb);

        int thrAP = flagAP ? 2 : 5;
        if (Utils.dice(100) < thrAP) {
            str.add("AP");
            flagAP = true;
        }

        switch (verb) {
            case "VT":
                str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                break;
            case "VTL":
                str.add("CL");
                flagCL = true;
                break;
            case "VOA":
                str.add("§à");
                str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                break;
            case "VOD":
                str.add("§de");
                str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                break;
            case "VOS":
                str.add("§sur");
                str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                break;
            case "VOI":
                str.add((Utils.dice(11) > 1) ? "CO" : "GN");
                str.add("§à");
                str.add((Utils.dice(11) > 1) ? "GN" : "CO");
                break;
            case "VAV":
                str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                str.add("§avec");
                str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                break;
            case "VET":
                str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                str.add("§et");
                str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                break;
        }

        if (Utils.dice(100) < 20) {
            int diceSuite = Utils.dice(100);
            if (diceSuite < 25) {
                str.add("PR_N");
                thrAP = flagAP ? 2 : 5;
                if (Utils.dice(100) < thrAP) {
                    str.add("AP");
                }
            } else if (diceSuite < 50) {
                str.add("PR_T");
                thrAP = flagAP ? 2 : 5;
                if (Utils.dice(100) < thrAP) {
                    str.add("AP");
                }
                str.add(Utils.dice(4) > 1 ? "CO" : "GN");
            } else {
                str.add(diceSuite < 75 ? "§sans" : "§pour");
                do {
                    verb = Utils.getByDice(StructureGenerator.NM_VERBS, StructureGenerator.PROBS_NM_VERBS);
                } while (flagCL && verb.equals("VTL"));
                str.add(verb);
                thrAP = flagAP ? 2 : 5;
                if (Utils.dice(100) < thrAP) {
                    str.add("AP");
                }

                switch (verb) {
                    case "VT":
                        str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                        break;
                    case "VTL":
                        str.add("CL");
                        flagCL = true;
                        break;
                    case "VOA":
                        str.add("§à");
                        str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                        break;
                    case "VOD":
                        str.add("§de");
                        str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                        break;
                    case "VOS":
                        str.add("§sur");
                        str.add((Utils.dice(4) > 1) ? "CO" : "GN");
                        break;
                    case "VOI":
                        str.add((Utils.dice(11) > 1) ? "CO" : "GN");
                        str.add("§à");
                        str.add((Utils.dice(11) > 1) ? "GN" : "CO");
                        break;
                    case "VAV":
                        str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                        str.add("§avec");
                        str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                        break;
                    case "VET":
                        str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                        str.add("§et");
                        str.add((Utils.dice(5) > 1) ? "CO" : "GN");
                        break;
                }
            }
        }

        if (Utils.dice(100) < 12) {
            String end;
            do {
                end = Utils.getByDice(StructureGenerator.END_OPTS, StructureGenerator.PROBS_END_OPTS);
            } while (((flagCT) && (end.equals("CT"))) || ((flagCL) && (end.equals("CL"))));
            str.add(end);
        }

        for (int i = 1; i < str.size(); i++) {
            if (str.get(i).equals("AP") && str.get(i - 1).charAt(0) == '§') {
                str.set(i, str.get(i - 1));
                str.set(i - 1, "AP");
            }
        }

        return str;
    }


}
