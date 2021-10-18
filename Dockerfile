FROM openjdk:15
COPY ./ /app/
WORKDIR /app/
RUN javac -d ./output ./project/src/*.java
WORKDIR /app/output