package fr.klemek.fsg;

class Conjugator {

    static String conjugate(String verb, int time, int pers) {
        return conjugate(verb, time, pers, 0);
    }

    static String conjugate(String verb, int time, int pers, int persPart) {

        boolean pronominal = verb.matches("^s('|e )(.*)");
        if (pronominal)
            verb = verb.replaceFirst("^s('|e )(.*)", "$2");

        String[][] forms;

        int grpPos = verb.indexOf('#');
        if (grpPos > -1) {
            forms = conjugateRegular(verb, grpPos);
        } else {
            forms = conjugateIrregular(verb);
        }

        if (time <= 0 || time > forms.length || pers <= 0 || pers > forms[time - 1].length)
            return null;

        String res = forms[time - 1][pers - 1];

        if (pronominal) {
            String prefix = Linking.PR_REF.get(persPart > 0 ? persPart - 1 : pers - 1) + " ";
            boolean abb = Utils.vowel(res) && prefix.contains("e");
            res = prefix + res;
            if (abb)
                res = res.replace("e ", "'");
        }

        return res;
    }

    static String questionForm(String verb, int pers, int gender) {
        String part = Linking.PR_PERS.get(pers - 1).split("@")[0];
        if (part.contains("_"))
            part = part.split("_")[gender];
        String t = (pers % 3 == 0 && verb.charAt(verb.length() - 1) != 't') ? "t-" : "";
        String res = verb + "-" + t + part;
        return res
                .replaceFirst("e-je", "é-je")
                .replaceFirst("oié-je", "oie-je");
    }

    private static String[][] conjugateRegular(String verb, int grpPos) {
        int grp = Integer.parseInt(verb.substring(grpPos + 1));
        String[][] terms;
        String root;
        String inter = "";
        switch (grp) {
            case 1:// verbes #1 : (modèle: chanter)
                root = verb.substring(0, verb.lastIndexOf("er"));
                terms = new String[][]{
                        {"ais", "ais", "ait", "ions", "iez", "aient"},
                        {"e", "es", "e", "ons", "ez", "ent"},
                        {"erai", "eras", "era", "erons", "erez", "eront"},
                        {"é", "ant"}
                };
                break;
            case 2:// verbes #2 : (modèle: finir)
                root = verb.substring(0, verb.lastIndexOf("ir"));
                terms = new String[][]{
                        {"issais", "issais", "issait", "issions", "issiez", "issaient"},
                        {"is", "is", "it", "issons", "issez", "issent"},
                        {"irai", "iras", "ira", "irons", "irez", "iront"},
                        {"i", "issant"}
                };
                break;
            case 3:// verbes #3 : (modèle: sentir)
                root = verb.substring(0, verb.lastIndexOf("tir"));
                terms = new String[][]{
                        {"tais", "tais", "tait", "tions", "tiez", "taient"},
                        {"s", "s", "t", "tons", "tez", "tent"},
                        {"tirai", "tiras", "tira", "tirons", "tirez", "tiront"},
                        {"ti", "tant"}
                };
                break;
            case 4:// verbes #4 : (modèle: vendre/répondre)
                root = verb.substring(0, verb.lastIndexOf("re"));
                terms = new String[][]{
                        {"ais", "ais", "ait", "ions", "iez", "aient"},
                        {"s", "s", "", "ons", "ez", "ent"},
                        {"rai", "ras", "ra", "rons", "rez", "ront"},
                        {"u", "ant"}
                };
                break;
            case 5:// verbes #5 : (modèle: paraître)
                root = verb.substring(0, verb.lastIndexOf("aître"));
                terms = new String[][]{
                        {"aissais", "aissais", "aissait", "aissions", "aissiez", "aissaient"},
                        {"ais", "ais", "aît", "aissons", "aissez", "aissent"},
                        {"aîtrai", "aîtras", "aîtra", "aîtrons", "aîtrez", "aîtront"},
                        {"u", "aissant"}
                };
                break;
            case 6:// verbes #6 : (modèle: construire)
                root = verb.substring(0, verb.lastIndexOf("re"));
                terms = new String[][]{
                        {"sais", "sais", "sait", "sions", "siez", "saient"},
                        {"s", "s", "t", "sons", "sez", "sent"},
                        {"rai", "ras", "ra", "rons", "rez", "ront"},
                        {"t", "sant"}
                };
                break;
            case 7:// verbes #7 : (modèle: peindre/joindre/craindre)
                root = verb.substring(0, verb.lastIndexOf("ndre"));
                terms = new String[][]{
                        {"gnais", "gnais", "gnait", "gnions", "gniez", "gnaient"},
                        {"ns", "ns", "nt", "gnons", "gnez", "gnent"},
                        {"ndrai", "ndras", "ndra", "ndrons", "ndrez", "ndront"},
                        {"nt", "gnant"}
                };
                break;
            case 8:// verbes #8 : (modèle: tenir)
                root = verb.substring(0, verb.lastIndexOf("enir"));
                terms = new String[][]{
                        {"enais", "enais", "enait", "enions", "eniez", "enaient"},
                        {"iens", "iens", "ient", "enons", "enez", "iennent"},
                        {"iendrai", "iendras", "iendra", "iendrons", "iendrez", "iendront"},
                        {"enu", "enant"}
                };
                break;
            case 9:// verbes #9 : (modèle: placer)
                root = verb.substring(0, verb.lastIndexOf("cer"));
                terms = new String[][]{
                        {"çais", "çais", "çait", "cions", "ciez", "çaient"},
                        {"ce", "ces", "ce", "çons", "cez", "cent"},
                        {"cerai", "ceras", "cera", "cerons", "cerez", "ceront"},
                        {"cé", "çant"}
                };
                break;
            case 10:// verbes #10 : (modèle: manger)
                root = verb.substring(0, verb.lastIndexOf("er"));
                terms = new String[][]{
                        {"eais", "eais", "eait", "ions", "iez", "eaient"},
                        {"e", "es", "e", "eons", "ez", "ent"},
                        {"erai", "eras", "era", "erons", "erez", "eront"},
                        {"é", "eant"}
                };
                break;
            case 11:// verbes #11 : (modèle: récupérer/accéder)
                int posEaigu = verb.lastIndexOf("é");
                root = verb.substring(0, posEaigu);
                inter = verb.replaceFirst("^(.*)é([^é]*)er#11$", "$2");
                terms = new String[][]{
                        {"é_ais", "é_ais", "é_ait", "é_ions", "é_iez", "é_aient"},
                        {"è_e", "è_es", "è_e", "é_ons", "é_ez", "è_ent"},
                        {"é_erai", "é_eras", "é_era", "é_erons", "é_erez", "é_eront"},
                        {"é_é", "é_ant"}
                };
                break;
            case 12:// verbes #12 : (modèle: mener/lever/peser)
                int posEfaible = verb.lastIndexOf("e");
                posEfaible = verb.substring(0, posEfaible).lastIndexOf("e");
                root = verb.substring(0, posEfaible);
                inter = verb.replaceFirst("^(.*)e([^e]*)er#12$", "$2");
                terms = new String[][]{
                        {"e_ais", "e_ais", "e_ait", "e_ions", "e_iez", "e_aient"},
                        {"è_e", "è_es", "è_e", "e_ons", "e_ez", "è_ent"},
                        {"è_erai", "è_eras", "è_era", "è_erons", "è_erez", "è_eront"},
                        {"e_é", "e_ant"}
                };
                break;
            case 13:// verbes #13 : (modèle: prendre)
                root = verb.substring(0, verb.lastIndexOf("endre"));
                terms = new String[][]{
                        {"enais", "enais", "enait", "enions", "eniez", "enaient"},
                        {"ends", "ends", "end", "enons", "enez", "ennent"},
                        {"endrai", "endras", "endra", "endrons", "endrez", "endront"},
                        {"is", "enant"}
                };
                break;
            case 14:// verbes #14 : (modèle: mettre)
                root = verb.substring(0, verb.lastIndexOf("ettre"));
                terms = new String[][]{
                        {"ettais", "ettais", "ettait", "ettions", "ettiez", "ettaient"},
                        {"ets", "ets", "et", "ettons", "ettez", "ettent"},
                        {"ettrai", "ettras", "ettra", "ettrons", "ettrez", "ettront"},
                        {"is", "ettant"}
                };
                break;
            case 15:// verbes #15 : (modèle: essuyer/employer)
                root = verb.substring(0, verb.lastIndexOf("yer"));
                terms = new String[][]{
                        {"yais", "yais", "yait", "yions", "yiez", "yaient"},
                        {"ie", "ies", "ie", "yons", "yez", "ient"},
                        {"ierai", "ieras", "iera", "ierons", "ierez", "ieront"},
                        {"yé", "yant"}
                };
                break;
            case 16:// verbes #16 : (modèle: ouvrir)
                root = verb.substring(0, verb.lastIndexOf("rir"));
                terms = new String[][]{
                        {"rais", "rais", "rait", "rions", "riez", "raient"},
                        {"re", "res", "re", "rons", "rez", "rent"},
                        {"rirai", "riras", "rira", "rirons", "rirez", "riront"},
                        {"ert", "rant"}
                };
                break;
            case 17:// verbes #17 : (modèle: battre)
                root = verb.substring(0, verb.lastIndexOf("tre"));
                terms = new String[][]{
                        {"tais", "tais", "tait", "tions", "tiez", "taient"},
                        {"s", "s", "", "tons", "tez", "tent"},
                        {"trai", "tras", "tra", "trons", "trez", "tront"},
                        {"tu", "tant"}
                };
                break;
            case 18:// verbes #18 : (modèle: écrire)
                root = verb.substring(0, verb.lastIndexOf("re"));
                terms = new String[][]{
                        {"vais", "vais", "vait", "vions", "viez", "vaient"},
                        {"s", "s", "t", "vons", "vez", "vent"},
                        {"rai", "ras", "ra", "rons", "rez", "ront"},
                        {"t", "vant"}
                };
                break;
            case 19:// verbes #19 : (modèle: servir)
                root = verb.substring(0, verb.lastIndexOf("vir"));
                terms = new String[][]{
                        {"vais", "vais", "vait", "vions", "viez", "vaient"},
                        {"s", "s", "t", "vons", "vez", "vent"},
                        {"virai", "viras", "vira", "virons", "virez", "viront"},
                        {"vi", "vant"}
                };
                break;
            case 20:// verbes #20 : (modèle: percevoir)
                root = verb.substring(0, verb.lastIndexOf("cevoir"));
                terms = new String[][]{
                        {"cevais", "cevais", "cevait", "cevions", "ceviez", "cevaient"},
                        {"çois", "çois", "çoit", "cevons", "cevez", "çoivent"},
                        {"cevrai", "cevras", "cevra", "cevrons", "cevrez", "cevront"},
                        {"çu", "cevant"}
                };
                break;
            case 21:// verbes #21 : (modèle: jeter)
                root = verb.substring(0, verb.lastIndexOf("er"));
                terms = new String[][]{
                        {"ais", "ais", "ait", "ions", "iez", "aient"},
                        {"te", "tes", "te", "ons", "ez", "tent"},
                        {"terai", "teras", "tera", "terons", "terez", "teront"},
                        {"é", "ant"}
                };
                break;
            default:// verbes #22 : (modèle: vivre)
                root = verb.substring(0, verb.lastIndexOf("ivre"));
                terms = new String[][]{
                        {"ivais", "ivais", "ivait", "ivions", "iviez", "ivaient"},
                        {"is", "is", "it", "ivons", "ivez", "ivent"},
                        {"ivrai", "ivras", "ivra", "ivrons", "ivrez", "ivront"},
                        {"écu", "vant"}
                };
                break;
        }

        String[][] forms = new String[terms.length][];
        String term;
        for (int t = 0; t < terms.length; t++) {
            forms[t] = new String[terms[t].length];
            for (int p = 0; p < terms[t].length; p++) {
                term = terms[t][p];
                if (inter.length() > 0)
                    term = term.replace("_", inter);
                forms[t][p] = root + term;
            }
        }
        return forms;
    }

    private static String[][] conjugateIrregular(String verb) {
        switch (verb) {
            default: //être
                return new String[][]{
                        {"étais", "étais", "était", "étions", "étiez", "étaient"},
                        {"suis", "es", "est", "sommes", "êtes", "sont"},
                        {"serai", "seras", "sera", "serons", "serez", "seront"},
                        {"été", "étant"}
                };
            case "avoir":
                return new String[][]{
                        {"avais", "avais", "avait", "avions", "aviez", "avaient"},
                        {"ai", "as", "a", "avons", "avez", "ont"},
                        {"aurai", "auras", "aura", "aurons", "aurez", "auront"},
                        {"eu", "ayant"}
                };
            case "aller":
                return new String[][]{
                        {"allais", "allais", "allait", "allions", "alliez", "allaient"},
                        {"vais", "vas", "va", "allons", "allez", "vont"},
                        {"irai", "iras", "ira", "irons", "irez", "iront"},
                        {"allé", "allant"}
                };
            case "devoir":
                return new String[][]{
                        {"devais", "devais", "devait", "devions", "deviez", "devaient"},
                        {"dois", "dois", "doit", "devons", "devez", "doivent"},
                        {"devrai", "devras", "devra", "devrons", "devrez", "devront"},
                        {"du", "devant"}
                };
            case "voir":
                return new String[][]{
                        {"voyais", "voyais", "voyait", "voyions", "voyiez", "voyaient"},
                        {"vois", "vois", "voit", "voyons", "voyez", "voient"},
                        {"verrai", "verras", "verra", "verrons", "verrez", "verront"},
                        {"vu", "voyant"}
                };
            case "savoir":
                return new String[][]{
                        {"savais", "savais", "savait", "savions", "saviez", "savaient"},
                        {"sais", "sais", "sait", "savons", "savez", "savent"},
                        {"saurai", "sauras", "saura", "saurons", "saurez", "sauront"},
                        {"su", "sachant"}
                };
            case "pouvoir":
                return new String[][]{
                        {"pouvais", "pouvais", "pouvait", "pouvions", "pouviez", "pouvaient"},
                        {"peux", "peux", "peut", "pouvons", "pouvez", "peuvent"},
                        {"pourrai", "pourras", "pourra", "pourrons", "pourrez", "pourront"},
                        {"pu", "pouvant"}
                };
            case "résoudre":
                return new String[][]{
                        {"résolvais", "résolvais", "résolvait", "résolvions", "résolviez", "résolvaient"},
                        {"résous", "résous", "résout", "résolvons", "résolvez", "résolvent"},
                        {"résoudrai", "résoudras", "résoudra", "résoudrons", "résoudrez", "résoudront"},
                        {"résolu", "résolvant"}
                };
            case "mordre":
                return new String[][]{
                        {"mordais", "mordais", "mordait", "mordions", "mordiez", "mordaient"},
                        {"mords", "mords", "mord", "mordons", "mordez", "mordent"},
                        {"mordrai", "mordras", "mordra", "mordrons", "mordrez", "mordront"},
                        {"mordu", "mordant"}
                };
            case "appeler":
                return new String[][]{
                        {"appelais", "appelais", "appelait", "appelions", "appeliez", "appelaient"},
                        {"appelle", "appelles", "appelle", "appelons", "appelez", "appellent"},
                        {"appellerai", "appelleras", "appellera", "appellerons", "appellerez", "appelleront"},
                        {"appelé", "appelant"}
                };
            case "envoyer":
                return new String[][]{
                        {"envoyais", "envoyais", "envoyait", "envoyions", "envoyiez", "envoyaient"},
                        {"envoie", "envoies", "envoie", "envoyons", "envoyez", "envoient"},
                        {"enverrai", "enverras", "enverra", "enverrons", "enverrez", "enverront"},
                        {"envoyé", "envoyant"}
                };
            case "faire":
                return new String[][]{
                        {"faisais", "faisais", "faisait", "faisions", "faisiez", "faisaient"},
                        {"fais", "fais", "fait", "faisons", "faites", "font"},
                        {"ferai", "feras", "fera", "ferons", "ferez", "feront"},
                        {"fait", "faisant"}
                };
            case "vouloir":
                return new String[][]{
                        {"voulais", "voulais", "voulait", "voulions", "vouliez", "voulaient"},
                        {"veux", "veux", "veut", "voulons", "voulez", "veulent"},
                        {"voudrai", "voudras", "voudra", "voudrons", "voudrez", "voudront"},
                        {"voulu", "voulant"}
                };
            case "croire":
                return new String[][]{
                        {"croyais", "croyais", "croyait", "croyions", "croyiez", "croyaient"},
                        {"crois", "crois", "croit", "croyons", "croyez", "croient"},
                        {"croirai", "croiras", "croira", "croirons", "croirez", "croiront"},
                        {"cru", "croyant"}
                };
            case "rire":
                return new String[][]{
                        {"riais", "riais", "riait", "riions", "riiez", "riaient"},
                        {"ris", "ris", "rit", "rions", "riez", "rient"},
                        {"rirai", "riras", "rira", "rirons", "rirez", "riront"},
                        {"ri", "riant"}
                };
            case "lire":
                return new String[][]{
                        {"lisais", "lisais", "lisait", "lisions", "lisiez", "lisaient"},
                        {"lis", "lis", "lit", "lisons", "lisez", "lisent"},
                        {"lirai", "liras", "lira", "lirons", "lirez", "liront"},
                        {"lu", "lisant"}
                };
            case "dire":
                return new String[][]{
                        {"disais", "disais", "disait", "disions", "disiez", "disaient"},
                        {"dis", "dis", "dit", "disons", "dites", "disent"},
                        {"dirai", "diras", "dira", "dirons", "direz", "diront"},
                        {"dit", "disant"}
                };
            case "interdire":
                return new String[][]{
                        {"interdisais", "interdisais", "interdisait", "interdisions", "interdisiez", "interdisaient"},
                        {"interdis", "interdis", "interdit", "interdisons", "interdisez", "interdisent"},
                        {"interdirai", "interdiras", "interdira", "interdirons", "interdirez", "interdiront"},
                        {"interdit", "interdisant"}
                };
            case "suivre":
                return new String[][]{
                        {"suivais", "suivais", "suivait", "suivions", "suiviez", "suivaient"},
                        {"suis", "suis", "suit", "suivons", "suivez", "suivent"},
                        {"suivrai", "suivras", "suivra", "suivrons", "suivrez", "suivront"},
                        {"suivi", "suivant"}
                };
            case "perdre":
                return new String[][]{
                        {"perdais", "perdais", "perdait", "perdions", "perdiez", "perdaient"},
                        {"perds", "perds", "perd", "perdons", "perdez", "perdent"},
                        {"perdrai", "perdras", "perdra", "perdrons", "perdrez", "perdront"},
                        {"perdu", "perdant"}
                };
            case "dormir":
                return new String[][]{
                        {"dormais", "dormais", "dormait", "dormions", "dormiez", "dormaient"},
                        {"dors", "dors", "dort", "dormons", "dormez", "dorment"},
                        {"dormirai", "dormiras", "dormira", "dormirons", "dormirez", "dormiront"},
                        {"dormi", "dormant"}
                };
            case "courir":
                return new String[][]{
                        {"courais", "courais", "courait", "courions", "couriez", "couraient"},
                        {"cours", "cours", "court", "courons", "courez", "courent"},
                        {"courrai", "courras", "courra", "courrons", "courrez", "courront"},
                        {"couru", "courant"}
                };
            case "recourir":
                return new String[][]{
                        {"recourais", "recourais", "recourait", "recourions", "recouriez", "recouraient"},
                        {"recours", "recours", "recourt", "recourons", "recourez", "recourent"},
                        {"recourrai", "recourras", "recourra", "recourrons", "recourrez", "recourront"},
                        {"recouru", "recourant"}
                };
            case "mourir":
                return new String[][]{
                        {"mourais", "mourais", "mourait", "mourions", "mouriez", "mouraient"},
                        {"meurs", "meurs", "meurt", "mourons", "mourez", "meurent"},
                        {"mourrai", "mourras", "mourra", "mourrons", "mourrez", "mourront"},
                        {"mort", "mourant"}
                };
            case "plaire":
                return new String[][]{
                        {"plaisais", "plaisais", "plaisait", "plaisions", "plaisiez", "plaisaient"},
                        {"plais", "plais", "plaît", "plaisons", "plaisez", "plaisent"},
                        {"plairai", "plairas", "plaira", "plairons", "plairez", "plairont"},
                        {"plu", "plaisant"}
                };
            case "nuire":
                return new String[][]{
                        {"nuisais", "nuisais", "nuisait", "nuisions", "nuisiez", "nuisaient"},
                        {"nuis", "nuis", "nuit", "nuisons", "nuisez", "nuisent"},
                        {"nuirai", "nuiras", "nuira", "nuirons", "nuirez", "nuiront"},
                        {"nui", "nuisant"}
                };
            case "fuir":
                return new String[][]{
                        {"fuyais", "fuyais", "fuyait", "fuyions", "fuyiez", "fuyaient"},
                        {"fuis", "fuis", "fuit", "fuyons", "fuyez", "fuient"},
                        {"fuirai", "fuiras", "fuira", "fuirons", "fuirez", "fuiront"},
                        {"fui", "fuyant"}
                };
            case "enfuir":
                return new String[][]{
                        {"enfuyais", "enfuyais", "enfuyait", "enfuyions", "enfuyiez", "enfuyaient"},
                        {"enfuis", "enfuis", "enfuit", "enfuyons", "enfuyez", "enfuient"},
                        {"enfuirai", "enfuiras", "enfuira", "enfuirons", "enfuirez", "enfuiront"},
                        {"enfui", "enfuyant"}
                };
            case "£aïr":
                return new String[][]{
                        {"£aïssais", "£aïssais", "£aïssait", "£aïssions", "£aïssiez", "£aïssaient"},
                        {"£ais", "£ais", "£ait", "£aïssons", "£aïssez", "£aïssent"},
                        {"£aïrai", "£aïras", "£aïra", "£aïrons", "£aïrez", "£aïront"},
                        {"£aï", "£aïssant"}
                };
        }
    }
}
