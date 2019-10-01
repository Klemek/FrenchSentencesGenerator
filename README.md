[![Scc Count Badge](https://sloc.xyz/github/klemek/frenchsentencesgenerator/?category=code)](https://github.com/boyter/scc/#badges-beta)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Klemek/FrenchSentencesGenerator.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Klemek/FrenchSentencesGenerator/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/Klemek/FrenchSentencesGenerator.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Klemek/FrenchSentencesGenerator/alerts/)

# French Sentences Generator (by Klemek)

A Java port of Romain Valeri's [generator](http://romainvaleri.online.fr/) from Javascript;

Current version v1.0

Examples :

```
En ville, un artisan dispensé de sport part.
Yasser Arafat admet bloquer sur un cheval...
Je parviendrai à me masser au pôle nord.
Elles tablent sur des inspirations.
Un penseur végétera-t-il pour la science ?
Le gondolier table sur la direction à Disneyland.
```

How to use :
```Java
SentenceGenerator gen = new SentenceGenerator();
gen.generate(); //returns a String sentence
```
Parameters :
```Java
SentenceGenerator gen = new SentenceGenerator();

gen.setSubject("Une voiture__F"); //set subject (end with "__F" to set feminine gender)
gen.keepStructure(true); //set to lock a structure for next sentences
gen.setQuestionMode(QuestionMode.FORBIDDEN); //choose to allow or not questions in the sentence

gen.generate();
```

## Download

* [french-sentences-gen-1.0.jar](../../raw/master/download/french-sentences-gen-1.0.jar)

## Maven

You can use this project as a maven dependency with this :
```XML
<repositories>
    ...
    <repository>
        <id>fr.klemek</id>
        <url>https://github.com/klemek/mvn-repo/raw/master</url>
    </repository>
</repositories>
...
<dependencies>
    ...
    <dependency>
        <groupId>fr.klemek</groupId>
        <artifactId>french-sentences-gen</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```
