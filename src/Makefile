JFLAGS = -g -d class
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  Main.java \
		  Point.java \
		  Vecteur.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) class/*.class
	
run:
	java -cp class/ Main