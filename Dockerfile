FROM amazoncorretto:21
EXPOSE 8080
ADD target/hotel-manag-v2.jar hotel-manag-v2.jar
ENTRYPOINT ["java","-jar","/hotel-manag-v2.jar"]

ENV DB_PORT=5432
ENV DB_NAME=hotel_v2
ENV DB_PASSWORD=massimo
ENV DB_USER=postgres
ENV DB_URL=hotel-manag-v2