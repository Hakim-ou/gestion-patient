all:
	javac -encoding utf8 -d bin -cp bin:bin/jdatepicker-1.3.4.jar -sourcepath src src/main/*.java src/lib/*.java

AddPatient:
	java -cp bin:bin/jdatepicker-1.3.4.jar main.AddPatient

clean:
	rm -rf bin/*.class
