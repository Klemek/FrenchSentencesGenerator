package fr.klemek.fsg;

enum Linking {

    ART_DEF("le*_la*", "les"),
    ART_INDEF("un_une", "des"),
    ART_DEMO("ce¤_cette", "ces"),
    ART_POSS_S("mon_ma", "ton_ta", "son_sa", "notre", "votre", "leur"),
    ART_POSS_P("mes", "tes", "ses", "nos", "vos", "leurs"),

    PR_PERS("je@1", "tu@2", "il_elle@3", "nous@4", "vous@5", "ils_elles@6"), //pronoms pesonnels
    PR_REF("me", "te", "se", "nous", "vous", "se") //pronoms reflexifs
    ;


    final String[] data;

    Linking(String... data) {
        this.data = data;
    }

    String get(int i) {
        return this.data[i];
    }

    String getOne(int[] weights) {
        return Utils.getByDice(this.data, weights);
    }
}
