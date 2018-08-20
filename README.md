# French Sentences Generator (by Klemek)

A Java port of Romain Valeri's [generator](http://romainvaleri.online.fr/) from Javascript;

Current version v1.0

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
	    <id>klemek.french-sentences-gen</id>
	    <url>https://github.com/klemek/frenchsentencesgenerator/raw/mvn-repo</url>
	</repository>
</repositories>
...
<dependencies>
...
	<dependency>
		<groupId>klemek</groupId>
		<artifactId>french-sentences-gen</artifactId>
		<version>1.0</version>
	</dependency>
</dependencies>
```
