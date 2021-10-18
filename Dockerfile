FROM openjdk: 12
WORKDIR /project/src/
COPY *.java ./
RUN javac *.java
