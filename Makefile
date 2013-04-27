all:
	javac *.java
test: all
	java Main < /dev/null

NAME=ai-proj2
tar:
	git archive --prefix ${NAME}/ -o ${NAME}.tgz HEAD
