FROM maven:3-jdk-11

WORKDIR /payment-api

COPY . .

RUN mvn clean install

CMD mvn spring-boot:run