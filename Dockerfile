FROM amazoncorretto:21
EXPOSE 8080
ADD target/hotel-manag-v2.jar hotel-manag-v2.jar
ENTRYPOINT ["java","-jar","/hotel-manag-v2.jar"]