JSOUP = lib/jsoup-1.14.3.jar
JUNIT = lib/junit-4.13.1.jar
HAMCR = lib/hamcrest-core-1.3.jar
CLASS = com.jamesreaver.Main
TESTS = com.jamesreaver.Tests
JUNITCORE = org.junit.runner.JUnitCore

all :
	javac -d ./build/ -cp $(JSOUP):$(JUNIT):$(HAMCR) ./*/*/**/*.java  ./*/*/**/*/*.java

run :
	java -cp build:$(JSOUP) $(CLASS)

test :
	java -cp build:$(JSOUP):$(JUNIT):$(HAMCR) $(JUNITCORE) $(TESTS)

clean :
	rm -rf build/*
