all:
	javac *.java

NAME=ai-proj2
tar:
	git archive --prefix ${NAME}/ -o ${NAME}.tgz HEAD
