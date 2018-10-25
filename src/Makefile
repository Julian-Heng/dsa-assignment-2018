all:
	javac -Xlint *.java

test: all
	for i in test*.class; do \
		[ "$$i" != "testHarnessCommons.class" ] && \
			java $${i%.*}; \
	done

clean:
	@rm -fv *.class *.csv
