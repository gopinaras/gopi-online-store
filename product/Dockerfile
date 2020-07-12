FROM amd64/openjdk:8
COPY ./target/product-0.0.1-SNAPSHOT.jar /usr/product/
EXPOSE 8086
CMD ["java","-jar", "/usr/product/product-0.0.1-SNAPSHOT.jar"]