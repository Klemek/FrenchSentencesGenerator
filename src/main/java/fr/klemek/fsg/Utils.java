package fr.klemek.fsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Utils {

    private static Random rand = new Random();

    private static boolean poolsLoaded = false;

    //POOLS

    static boolean loadPools() {
        if (!Utils.poolsLoaded) {
            for (Pool p : Pool.values()) {
                if (p.file != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                            Utils.class.getClassLoader().getResourceAsStream(
                                    "french_sentences/" + p.file + ".txt")))) {
                        p.data.clear();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.length() > 0 && !line.startsWith("#"))
                                p.data.add(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            Utils.poolsLoaded = true;
        }
        return true;
    }

    static Pool parsePool(String pool) {
        for (Pool p : Pool.values()) {
            if (p.toString().equals(pool))
                return p;
        }
        System.err.println("Invalid pool : " + pool);
        return null;
    }

    // RANDOM

    static int dice(int faces) {
        return rand.nextInt(faces) + 1;
    }

    static int dice(int[] weights) {
        int max = 0;
        for (int w : weights)
            max += w;
        int sum = Utils.dice(max);
        int res = 0;
        while (res < weights.length && sum >= weights[res]) {
            sum -= weights[res];
            res++;
        }
        if (res == weights.length)
            res--;
        return res + 1;
    }

    static int niceDice() {
        int r = dice(100);
        if (r < 60)
            return dice(4) + 1;
        else if (r < 90)
            return dice(10) + dice(10) + dice(10);
        else if (r < 97)
            return dice(10) * 10;
        else if (r < 99)
            return dice(100) * 100;
        else
            return dice(1000) + 1;
    }

    static <T> T getByDice(T[] list, int[] weights) {
        return list[dice(weights) - 1];
    }

    // STRINGS

    static boolean vowel(String word) {
        return word.matches("^[aeiouyhàâäéèêëîïôöùûüœ].*");
    }

    static String pluralForm(String word, boolean plural) {
        if (plural) {
            if (!word.contains("²"))
                return word + "s";
            else if (!word.contains("²²"))
                return word
                        .replaceAll("²([^\\s])", "$1")
                        .replaceAll("²", "");
            else
                return word.split("²²")[1];
        } else {
            if (word.contains("²²"))
                word = word.split("²²")[0];
            return word.replaceAll("²", "");
        }
    }

    //ARRAYS

    static String[] delete(String[] src, int index) {
        if (src.length == 0)
            return src;
        while (index < 0)
            index += src.length;
        while (index >= src.length)
            index -= src.length;
        ArrayList<String> dst = new ArrayList<>(Arrays.asList(src));
        dst.remove(index);
        return dst.toArray(new String[0]);
    }

    static String[] insert(String[] src, int index, String obj) {
        while (index < 0)
            index += src.length;
        while (index > src.length)
            index -= src.length;
        ArrayList<String> dst = new ArrayList<>(Arrays.asList(src));
        dst.add(index, obj);
        return dst.toArray(new String[0]);
    }

    static int[] concat(int[] a, int[] b) {
        int length = a.length + b.length;
        int[] result = new int[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    //NUMBERS

    static String num2str(int num) {
        if (num < 0)
            return "moins " + num2str(-num);
        switch (num) {
            case 0:
                return "zéro";
            case 1:
                return "un";
            case 2:
                return "deux";
            case 3:
                return "trois";
            case 4:
                return "quatre";
            case 5:
                return "cinq";
            case 6:
                return "six";
            case 7:
                return "sept";
            case 8:
                return "huit";
            case 9:
                return "neuf";
            case 10:
                return "dix";
            case 11:
                return "onze";
            case 12:
                return "douze";
            case 13:
                return "treize";
            case 14:
                return "quatorze";
            case 15:
                return "quinze";
            case 16:
                return "seize";
            case 17:
            case 18:
            case 19:
                return num2str(10) + "-" + num2str(num - 10);
            case 20:
                return "vingt";
            case 30:
                return "trente";
            case 40:
                return "quarante";
            case 50:
                return "cinquante";
            case 60:
                return "soixante";
            case 80:
                return num2str(4) + "-" + num2str(20) + "s";
            case 100:
                return "cent";
            case 1000:
                return "mille";
            case 1000000:
                return "million";
            case 1000000000:
                return "millard";
            default:
                if (num < 100) {
                    if (num < 70) {
                        if (num % 10 == 1)
                            return num2str(10 * (num / 10)) + "-et-" + num2str(1);
                        return num2str(10 * (num / 10)) + "-" + num2str(num % 10);
                    } else if (num < 80) {
                        if (num % 10 == 1)
                            return num2str(60) + "-et-" + num2str(11);
                        return num2str(60) + "-" + num2str(num % 20);
                    }
                    String qv = num2str(80);
                    qv = qv.substring(0, qv.length() - 1);
                    if (num < 90)
                        return qv + "-" + num2str(num % 10);
                    else
                        return qv + "-" + num2str(num % 20);
                } else if (num < 200) {
                    return num2str(100) + (num % 100 == 0 ? "s" : " " + num2str(num % 100));
                } else if (num < 1000) {
                    return num2str(num / 100) + " " + num2str(100) + (num % 100 == 0 ? "s" : " " + num2str(num % 100));
                } else if (num < 2000) {
                    return num2str(1000) + (num % 1000 == 0 ? "" : " " + num2str(num % 1000));
                } else if (num < 1000000) {
                    String tmp = num2str(num / 1000);
                    if (tmp.endsWith(num2str(100) + "s"))
                        tmp.substring(0, tmp.length() - 1);
                    return tmp + " " + num2str(1000) + (num % 1000 == 0 ? "" : " " + num2str(num % 1000));
                } else if (num < 1000000000) {
                    int tmp = num / 1000000;
                    return num2str(num / 1000000) + " " + num2str(1000000) + (tmp > 1 ? "s " : " ") + num2str(num % 1000000);
                } else {
                    int tmp = num / 1000000000;
                    return num2str(num / 1000000000) + " " + num2str(1000000000) + (tmp > 1 ? "s " : " ") + num2str(num % 1000000000);
                }
        }
    }
}
