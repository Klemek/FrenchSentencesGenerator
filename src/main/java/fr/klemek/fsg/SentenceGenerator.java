package fr.klemek.fsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceGenerator {

    private static final int[] PROBS_POINT = {8, 1, 1, 0};
    private static final int[] PROBS_QUESTION = {0, 0, 0, 1};

    private static final int[] PROBS_NEG = {12, 3, 2, 1, 1, 1, 2, 1, 1, 1, 1};
    private static final int[] PROBS_AI = {10, 1, 1, 1, 1, 1};
    private static final int[] PROBS_AIL = {8, 2, 1, 2, 2, 1, 1, 1};
    private static final int[] PROBS_AIT = {10, 2, 1};
    private static final int[] PROBS_AIM = {10, 2, 2};

    private static final List<String> NM_VERBS = Arrays.asList("VT", "VN", "VOA", "VOD", "VOI", "VTL", "VAV", "VET", "VOS");
    private static final List<String> M_VERBS = Arrays.asList("VM", "VD", "VA");

    private static final Pattern p = Pattern.compile("(.*)\\{(.*)}");

    private List<String> lastStruct;

    //OPTIONS
    private QuestionMode questionMode = QuestionMode.FREE;
    private String subject;
    private boolean keepStructure;

    public String generate() {
        Utils.loadPools();
        String sentence;
        do {
            sentence = tryGenerate();
        } while (sentence == null);
        return sentence;
    }

    private String tryGenerate() {
        List<String> struct = (lastStruct == null || !keepStructure) ? StructureGenerator.generate() : lastStruct;
        lastStruct = new ArrayList<>(struct);
        String[] words = new String[struct.size()];
        String[] parts = new String[struct.size()];

        int diceQuestion = Utils.dice(100);
        boolean questionInv = diceQuestion > this.questionMode.thrInv;
        boolean questionSimple = !questionInv && diceQuestion > this.questionMode.thrSimple;

        boolean pp = false;
        int gender = 0;

        int posVerb = -1;
        int posVerb2 = -1;
        int posModal = -1;
        int posQue = -1;
        int posPR = -1;

        int pers = 0;
        int time = -1;


        int firstGN = 0;
        boolean advPost = false;
        boolean flagNoNeg = false;

        boolean preAdvCheck = struct.contains("AP");

        for (int i = 0; i < struct.size(); i++) {
            if (struct.get(i).charAt(0) == '§') {
                String litteral = struct.get(i).substring(1);
                if (litteral.contains("/"))
                    litteral = litteral.split("/")[Utils.dice(2) - 1];
                words[i] = litteral;
                if (litteral.equals("sans"))
                    flagNoNeg = true;
            } else {
                String word;
                boolean searchParts = false;
                do {
                    switch (struct.get(i)) {
                        case "GN":
                            if (this.subject != null && firstGN == 0) {
                                word = this.subject;
                                if (word.matches("^(je|tu|il|elle|nous|vous|ils|elles)@\\d$"))
                                    pp = true;
                                gender = word.endsWith("__F") ? 1 : 0;
                            } else {
                                word = GroupGenerator.generateNominalGroup(firstGN > 0);
                                if (word.contains("_PP")) {
                                    word = word.replaceAll("_PP", "");
                                    pp = true;
                                }
                                if (firstGN == 0)
                                    gender = word.contains("__F") ? 1 : 0;
                                word = word.replaceAll("__F", "");
                            }
                            if (firstGN == 0)
                                firstGN = i + 1;
                            break;
                        case "CO":
                            word = GroupGenerator.generateObjectComplement(pers);
                            break;
                        case "VET":
                            word = Utils.parsePool(Utils.dice(8) > 7 ? "VT" : "VET").getOne();
                            break;
                        case "VAV":
                            word = Utils.parsePool(Utils.dice(10) > 9 ? "VT" : "VAV").getOne();
                            break;
                        default:
                            word = Utils.parsePool(struct.get(i)).getOne();
                            break;
                    }

                    Matcher m = p.matcher(word);
                    searchParts = m.find();
                    if (searchParts) {
                        parts[i] = m.group(2);
                        word = m.group(1);
                    }
                } while (preAdvCheck && searchParts);


                int posPers = word.indexOf('@');
                if (posPers > -1) {
                    if (pers == 0)
                        pers = Integer.parseInt(word.split("@")[1]);
                    word = word.substring(0, posPers);
                }

                words[i] = word;
            }

            if (SentenceGenerator.NM_VERBS.contains(struct.get(i)))
                if (posVerb > -1) {
                    if (posModal > -1) {
                        posVerb2 = i;
                    } else {
                        posModal = posVerb;
                        posVerb = i;
                    }
                } else {
                    posVerb = i;
                }
            if (SentenceGenerator.M_VERBS.contains(struct.get(i)))
                posModal = i;

            if (struct.get(i).equals("§que"))
                posQue = i;
            if (struct.get(i).equals("CT")) {
                int posTime = words[i].indexOf('¤');
                if (posTime > -1) {
                    time = Integer.parseInt(words[i].substring(posTime + 1));
                    words[i] = words[i].substring(0, posTime);
                }
                String name;
                while (words[i].contains("$")) {
                    do {
                        name = Pool.GN_NP.getOne().replaceAll("_F", "");
                    } while (words[i].contains(name));
                    words[i] = words[i].replaceFirst("\\$", name);
                }
                words[i] = words[i].replaceAll("(?i) de ([aeiouyhéèâœ])", " d'$1");
            }

            if (struct.get(i).equals("CL") || struct.get(i).equals("AF")) {
                String name;
                while (words[i].contains("$")) {
                    do {
                        name = Pool.GN_NP.getOne().replaceAll("_F", "");
                    } while (words[i].contains(name));
                    words[i] = words[i].replaceFirst("\\$", name);
                }
                words[i] = words[i].replaceAll("(?i) de ([aeiouyhéèâœ])", " d'$1");

                while (words[i].contains("+")) {
                    int posPlus = words[i].indexOf('+');
                    boolean fem = words[i].charAt(posPlus + 1) == 'F';
                    String noun = null;
                    boolean ok;
                    do {
                        ok = true;
                        noun = Pool.GN_NC.getOne();

                        int posSpl = noun.indexOf("_");
                        if (posSpl > -1) {
                            char tmpGender = noun.charAt(posSpl + 1);
                            if (!fem && (tmpGender == 'F') || (fem && tmpGender == 'H')) {
                                ok = false;
                            } else {
                                noun = noun.split("_")[0];
                            }
                        } else {
                            posSpl = noun.indexOf("%");
                            if (posSpl > -1) {
                                if (noun.substring(posSpl + 1).length() == 1)
                                    noun = noun.replaceFirst("(.*)%e", "$1%$1e");
                                noun = noun.split("%")[((fem) ? 1 : 0)];
                            }
                        }
                    } while (!ok || noun == null);
                    noun = Utils.pluralForm(noun, false);
                    words[i] = words[i].replaceFirst("\\+[FH]", noun);
                }
                words[i] = words[i].replaceAll("(?i) de ([aeiouyhéèâœ])", " d'$1");
            }

            if (struct.get(i).equals("CL") || struct.get(i).equals("CT") || struct.get(i).equals("AF")) {
                int num;
                String snum;
                while (words[i].contains("&")) {
                    num = Utils.dice(10) + 1;
                    if (words[i].contains("&0")) {
                        num = (num * 10) - 10;
                    }
                    if (words[i].contains("&00")) {
                        num *= 10;
                    }
                    snum = Utils.num2str(num);
                    words[i] = words[i].replaceFirst("&(0){0,2}", snum);
                }
            }

            if (struct.get(i).split("_")[0].equals("PR"))
                posPR = i;
        }

        if (time == -1)
            time = Utils.dice(2) > 1 ? 2 : Utils.dice(3);

        if (questionInv && words[firstGN - 1].equals("je") && time == 2 && Utils.dice(5) > 2) {
            questionInv = false;
            questionSimple = true;
        }

        if (posQue > -1)
            words[posQue] = Utils.vowel(words[posQue + 1]) ? "qu'" : "que";

        if (posPR > -1) {
            int tPers = 2;
            int tPersPart = 0;
            if (words[posPR].matches("^s(\'|e ).*$"))
                tPersPart = pers;
            String neg1 = "";
            String neg2 = "";
            if (!flagNoNeg && Utils.dice(13) > 12) {
                neg1 = Utils.vowel(words[posPR]) ? "n'" : "ne";
                neg2 = " " + Pool.NEG.getOne(SentenceGenerator.PROBS_NEG);
            }
            words[posPR] = "en " + neg1 + Conjugator.conjugate(words[posPR], 4, tPers, tPersPart) + neg2;
        }

        if (posModal > -1) {
            String verbRoot = Conjugator.conjugate(words[posModal], time, pers);
            if (verbRoot == null)
                return null;
            if (questionInv)
                verbRoot = Conjugator.questionForm(verbRoot, pers, gender);
            if (!flagNoNeg && !advPost && Utils.dice(13) > 12) {
                boolean vowel = Utils.vowel(verbRoot);
                String neg = Pool.NEG.getOne(SentenceGenerator.PROBS_NEG);
                verbRoot = (vowel ? "n'" : "ne ") + verbRoot + " " + neg;
            }

            words[posModal] = verbRoot;

            if (words[posVerb].contains("#"))
                words[posVerb] = words[posVerb].split("#")[0];

            if (pers % 3 != 0 && words[posVerb].matches("^s('|e ).*")) {
                String pr = Linking.PR_REF.get(pers - 1) + " ";
                if (words[posVerb].contains("'") && pers < 3)
                    pr = pr.replaceAll("e ", "'");
                words[posVerb] = words[posVerb].replaceFirst("s('|e )", pr);
            }
            if (!flagNoNeg && Utils.dice(13) > 12) {
                String neg = Pool.NEG.getOne(SentenceGenerator.PROBS_NEG);
                words[posVerb] = "ne " + neg + " " + words[posVerb];
            }
            if (posVerb2 > -1) {
                if (words[posVerb2].contains("#"))
                    words[posVerb2] = words[posVerb2].split("#")[0];

                if (pers % 3 != 0 && words[posVerb2].matches("^s('|e ).*")) {
                    String pr = Linking.PR_REF.get(pers - 1) + " ";
                    if (words[posVerb2].contains("'") && pers < 3)
                        pr = pr.replaceAll("e ", "'");
                    words[posVerb2] = words[posVerb2].replaceFirst("s('|e )", pr);
                }
                if (!flagNoNeg && Utils.dice(13) > 12) {
                    String neg = Pool.NEG.getOne(SentenceGenerator.PROBS_NEG);
                    words[posVerb2] = "ne " + neg + " " + words[posVerb2];
                }
            }
        } else if (posVerb > -1) {
            String verbRoot = Conjugator.conjugate(words[posVerb], time, pers);
            if (verbRoot == null)
                return null;
            if (questionInv)
                verbRoot = Conjugator.questionForm(verbRoot, pers, gender);

            if (!flagNoNeg && !advPost && Utils.dice(13) > 12) {
                boolean vowel = Utils.vowel(verbRoot);
                String neg = Pool.NEG.getOne(SentenceGenerator.PROBS_NEG);
                verbRoot = (vowel ? "n'" : "ne ") + verbRoot + " " + neg;
            }

            words[posVerb] = verbRoot;
        }

        for (int i = 0; i < words.length; i++) {
            if (parts[i] != null)
                words[i] = words[i] + " " + parts[i];
        }

        if (questionInv) {
            if (pp)
                words = Utils.delete(words, firstGN - 1);
            if (Utils.dice(2) > 1) {
                List<String> adjs = new ArrayList<>();
                adjs.addAll(Pool.AI.data);
                int[] probs = SentenceGenerator.PROBS_AI;
                if (!struct.contains("CT")) {
                    adjs.addAll(Pool.AIT.data);
                    probs = Utils.concat(probs, SentenceGenerator.PROBS_AIT);
                }
                if (!struct.contains("CL")) {
                    adjs.addAll(Pool.AIL.data);
                    probs = Utils.concat(probs, SentenceGenerator.PROBS_AIL);
                }
                if (!struct.contains("AP") && !struct.contains("AF")) {
                    adjs.addAll(Pool.AIM.data);
                    probs = Utils.concat(probs, SentenceGenerator.PROBS_AIM);
                }
                words = Utils.insert(words, firstGN - 1,
                        Utils.getByDice(adjs.toArray(new String[0]), probs));
                struct.add(firstGN - 1, "AI");
            }
        }

        if (questionSimple)
            words = Utils.insert(words, firstGN - 1, "est-ce que");

        String sentence = SentenceGenerator.finish(words, questionInv);
        sentence = SentenceGenerator.punctuate(sentence, questionSimple || questionInv);

        return sentence;
    }

    private static String finish(String[] words, boolean questionInv) {
        String res = String.join(" ", words);
        res = res
                .replaceAll(" ,", ",")
                .replaceAll("' ", "'")
                .replaceAll("à [Ll]es ", "aux ")
                .replaceAll("à [Ll]e ", "au ")
                .replaceAll(" en [Ll]e ", " en ")
                .replaceAll("(?i) de ([AEIOUHYaeiouhyéèêàùäëïöüœ])", " d'$1")
                .replaceAll("(?i) que ([AEIOUHYaeiouhyéèêàùäëïöüœ])", " qu'$1")
                .replaceAll("plus des ([aeiouhyéèêàùäëïöüœ])", "plus d'$1")
                .replaceAll(" de [Ll]es ", " des ")
                .replaceAll("(?i) de de(s)? ([aeiouéèêàîïùyhœ])", " d'$2")
                .replaceAll("(?i) de de(s)? ([^aeiouéèêàîïùyhœ])", " de $2")
                .replaceAll("(?i) de de(s)? ([^aeiouéèêàîïùyhœ])", " de $2")
                .replaceAll(" de [Dd]'", " d'")
                .replaceAll(" de ([Ll]e|du) ", " du ");
        if (!questionInv)
            res = res.replaceAll("(?i)je ([aeiouéèêàîïùyhœ])", "j'$1");
        return res
                .replaceAll("£", "h")
                .replaceAll("µ", "Y")
                .replaceAll("¥", "y");
    }

    private static String punctuate(String sentence, boolean isQuestion) {
        return sentence.substring(0, 1).toUpperCase()
                + sentence.substring(1)
                + Pool.PF.getOne(isQuestion ? PROBS_QUESTION : PROBS_POINT);
    }

    public enum QuestionMode {
        FREE(94, 77),
        FORBIDDEN(100, 100),
        FORCED(78, 0);

        final int thrSimple;
        final int thrInv;

        QuestionMode(int thrSimple, int thrInv) {
            this.thrSimple = thrSimple;
            this.thrInv = thrInv;
        }
    }

    public QuestionMode getQuestionMode() {
        return questionMode;
    }

    public void setQuestionMode(QuestionMode questionMode) {
        this.questionMode = questionMode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isKeepingStructure() {
        return keepStructure;
    }

    public void keepStructure(boolean keepStructure) {
        this.keepStructure = keepStructure;
    }
}
