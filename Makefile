all:
	javac *.java
test: all
	( echo data/DT; echo data/DR; echo data/L; echo data/TEST; echo test-out.txt ) | java Main

NAME=ai-proj2
tar:
	git archive --prefix ${NAME}/ -o ${NAME}.tgz HEAD
