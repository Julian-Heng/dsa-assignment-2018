JFLAGS = -Xlint
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = DSALinkedList.java \
		  DSAStack.java \
		  DSAHashTable.java \
		  DSAMaxHeap.java \
		  DSAMinHeap.java \
		  DSAGraph.java \
		  DSAMergeSort.java \
		  FileIO.java \
		  Menu.java \
		  Input.java \
		  Nominee.java \
		  HousePreference.java \
		  Location.java \
		  Trip.java \
		  VoteStats.java \
		  Commons.java \
		  ElectionManagerInit.java \
		  ElectionManager.java \
		  Driver.java

TEST_CLASSES = testCommons.java \
			   testDSAGraph.java \
			   testDSAHashTable.java \
			   testDSALinkedList.java \
			   testDSAMaxHeap.java \
			   testDSAMergeSort.java \
			   testDSAMinHeap.java \
			   testDSAStack.java \
			   testHarnessCommons.java \
			   testHousePreference.java \
			   testLocation.java \
			   testNominee.java \
			   testTrip.java \
			   testVoteStats.java

default: classes

classes: $(CLASSES:.java=.class)
test_classes: $(TEST_CLASSES:.java=.class)

test: classes test_classes

run_test: test
	@for i in test*.class; do \
		[ "$$i" != "testHarnessCommons.class" ] && \
			java $${i%.*}; \
	done

clean:
	@$(RM) -v *.class *.csv
