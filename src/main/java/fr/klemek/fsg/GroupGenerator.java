package fr.klemek.fsg;

class GroupGenerator {

    private static final int[] PROBS_PR_PERS = {2, 2, 1, 2, 2, 1};
    private static final int[] PROBS_QUANTIF = {4, 4, 3, 5, 6, 6, 7, 6, 5, 4, 4, 3, 3, 4, 4, 3, 3, 2, 3, 2, 2, 2, 4, 2, 3, 2, 1, 1, 1, 2, 1, 1, 1};

    static String generateNominalGroup(boolean isObject) {
        String variante;
        if (Utils.dice(5) == 0) {
            // proper noun
            if (Utils.dice(2) == 0) {
                // variation
                do {
                    variante = Pool.GN_VNP.getOne();
                } while (isObject && variante.matches("(.*) et (moi|toi|nous|vous)(.*)"));
                int posDollar = variante.indexOf('$');
                while (posDollar > -1) {
                    boolean searchF = variante.charAt(posDollar + 1) == 'F';
                    String nomP;
                    do {
                        boolean fem;
                        do {
                            nomP = Pool.GN_NP.getOne();
                            fem = nomP.contains("_F");
                        } while (searchF ^ fem);
                        nomP = nomP.replace("_F", "");
                    } while (variante.contains(nomP));

                    variante = variante.replaceFirst("\\$[FM]?", nomP);
                    posDollar = variante.indexOf('$');
                }

                return variante
                        .replaceAll("(certain(e)? \\\")(le |la |l')(.*)", "$1$4")
                        .replaceAll("(?i) (d|qu)e ([aeiouhéêèâyœ])", " $1'$2");
            } else {
                return Pool.GN_NC.getOne();
            }
        } else if (!isObject && Utils.dice(10) > 7) {
            //personal pronoun
            String pronP = Linking.PR_PERS.getOne(GroupGenerator.PROBS_PR_PERS);
            if (!pronP.contains("_"))
                return pronP + "_PP";
            String pers = pronP.split("@")[1];
            String[] pronPs = pronP.split("@")[0].split("_");
            pronPs[1] += "__F";
            return pronPs[Utils.dice(2) - 1] + "@" + pers + "_PP";
        } else {
            // common noun
            String noun = Pool.GN_NC.getOne();
            if (noun.contains("%")) {
                String[] genderSplit = noun.split("%");
                if (genderSplit[1].length() == 1)
                    genderSplit[1] = genderSplit[0].replaceAll("².*$", "") + genderSplit[1];
                genderSplit[0] += "_H";
                genderSplit[1] += "_F";
                noun = genderSplit[Utils.dice(2) - 1];
            }

            int gender = 0;

            if (noun.contains("_")) {
                String[] nouns = noun.split("_");
                if (nouns[1].equals("N") && Utils.dice(2) > 1)
                    nouns[1] = "F";
                noun = nouns[0];
                gender = nouns[1].equals("F") ? 1 : 0;
            }

            boolean plural = Utils.dice(2) == 1;
            boolean vowel = Utils.vowel(noun);

            String article;
            int diceArticle = Utils.dice(100);

            if (diceArticle < 38) //définis
                article = Linking.ART_DEF.get(plural ? 1 : 0);
            else if (diceArticle < 78) //indéfinis
                article = Linking.ART_INDEF.get(plural ? 1 : 0);
            else if (diceArticle < 84) //démonstratifs
                article = Linking.ART_DEMO.get(plural ? 1 : 0);
            else if (diceArticle < 96) { //quantifieurs
                article = Pool.QT.getOne(GroupGenerator.PROBS_QUANTIF);
                plural = !article.contains("µ");
                if (article.contains("%%"))
                    article = article.replaceFirst("%%", Utils.num2str(Utils.dice(100)));
            } else {
                plural = true;
                article = Utils.num2str(Utils.niceDice());
                if (gender == 1)
                    article = article.replaceFirst("un$", "une");
            }

            if (article.contains("_"))
                article = article.split("_")[gender];

            boolean pluralNoun = article.contains("µ") || plural;
            article = article.replaceFirst("µ", "");
            noun = Utils.pluralForm(noun, pluralNoun);

            String comp = "";
            if (Utils.dice(4) > 3) {
                comp = Pool.GN_CNC.getOne();
                if (comp.contains("%")) {
                    String[] genderSplit = comp.split("%");
                    if (genderSplit[1].length() == 1)
                        genderSplit[1] = genderSplit[0] + genderSplit[1];
                    comp = genderSplit[gender];
                }
                comp = " " + Utils.pluralForm(comp, pluralNoun);
                String num;
                while (comp.contains("&")) {
                    num = Utils.num2str(Utils.dice(100) + 1);
                    if (gender == 1)
                        num = num.replaceFirst("un$", "une");
                    comp = comp.replaceFirst("&", num);
                }
            }

            String gn = article + " " + noun + comp;
            if (vowel)
                gn = gn
                        .replaceAll(".\\* ", "'")
                        .replaceAll("¤", "t");
            gn = gn
                    .replaceAll("\\*", "")
                    .replaceAll("¤", "");
            String codePers = "";
            if (!isObject)
                codePers = "@" + ((plural) ? 6 : 3);
            return gn + codePers + (gender == 1 ? "__F" : "");
        }
    }

    static String generateObjectComplement(int persSubj) {
        String noun = Pool.CO_NC.getOne();
        boolean indef = false;
        String[] nouns = noun.split("_");
        if (nouns[1].equals("N") && Utils.dice(2) > 1)
            nouns[1] = "F";
        int gender = nouns[1].equals("F") ? 1 : 0;
        noun = nouns[0];

        boolean plural = Utils.dice(2) == 1;
        boolean vowel = Utils.vowel(noun);

        String adj = "";
        boolean prePose = false;
        int diceAdj = Utils.dice(100);
        if (diceAdj < 20)
            adj = Pool.CO_APO.getOne();
        else if (diceAdj < 24) {
            adj = Pool.CO_APR.getOne();
            prePose = true;
        }
        boolean noAdj = adj.length() == 0;

        String num;
        while (adj.contains("&")) {
            num = Utils.num2str(Utils.niceDice());
            adj = adj.replaceFirst("&", num);
        }

        if (adj.contains("%")) {
            String[] genderSplit = adj.split("%");
            if (genderSplit[1].length() == 1)
                genderSplit[1] = genderSplit[0] + genderSplit[1];
            adj = genderSplit[gender];
        }

        boolean vowelAdj = Utils.vowel(adj);

        String article;
        int diceArticle = Utils.dice(100);

        if (diceArticle < 35) //définis
            article = Linking.ART_DEF.get(plural ? 1 : 0);
        else if (diceArticle < 70) { //indéfinis
            article = Linking.ART_INDEF.get(plural ? 1 : 0);
            indef = true;
        } else if (diceArticle < 78) //démonstratifs
            article = Linking.ART_DEMO.get(plural ? 1 : 0);
        else if (diceArticle < 90) { //quantifieurs
            article = Pool.QT.getOne(GroupGenerator.PROBS_QUANTIF);
            article = article.replaceFirst("µ", "");
            plural = true;
            if (article.contains("%%"))
                article = article.replaceFirst("%%", Utils.num2str(Utils.dice(100)));
        } else if (diceArticle < 94) {
            plural = true;
            article = Utils.num2str(Utils.niceDice());
            if (gender == 1)
                article = article.replaceFirst("un$", "une");
        } else { //possessifs
            int pers;
            do {
                pers = Utils.dice(6);
            } while (Math.abs(pers - persSubj) == 3);
            article = plural ? Linking.ART_POSS_P.get(pers - 1) : Linking.ART_POSS_S.get(pers - 1);
            if (article.contains("_")) {
                int articleGender = gender;
                if (((vowel && articleGender == 1) || (prePose && vowelAdj))
                        && (noAdj || !prePose || vowelAdj)) {
                    articleGender = 0;
                }
                article = article.split("_")[articleGender];
            }
        }
        if (article.contains("_"))
            article = article.split("_")[gender];

        noun = Utils.pluralForm(noun, plural);
        adj = Utils.pluralForm(adj, plural);

        if (prePose && adj.contains("°"))
            adj = adj.split("°")[vowel ? 1 : 0];

        if (indef && plural && prePose)
            article = vowelAdj ? "d'" : "de";

        if ((vowel && noAdj) || (vowel && !noAdj && !prePose) || (!noAdj && prePose && vowelAdj))
            article = article
                    .replaceAll(".\\* ", "'")
                    .replaceAll("¤", "t");

        String co;

        if (noAdj)
            co = article + " " + noun;
        else {
            co = article;
            if (prePose)
                co += !article.contains("'") ? (" " + adj) : adj;
            co += " " + noun;
            if (!prePose)
                co += " " + adj;
        }

        if ((vowel && noAdj) || (vowel && !noAdj && !prePose) || (!noAdj && prePose && vowelAdj))
            co = co
                    .replaceAll(".\\* ", "'")
                    .replaceAll("¤", "t");

        co = co.replaceAll("\\*", "").replaceAll("¤", "");

        return co;
    }


}
