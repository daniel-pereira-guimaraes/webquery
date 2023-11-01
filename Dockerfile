# Run from the webquery project root directory:
#	docker build . -t webquery
#	docker run -p 4567:4567 --rm webquery

FROM maven:3.6.3-jdk-14

# Change BASE_URL!
ENV BASE_URL=https://domain.com/site/
ENV IGNORE_CASE=true
ENV MAX_RESULT=10
ENV SHOW_MESSAGES=true

ADD . /usr/src/webquery
WORKDIR /usr/src/webquery
EXPOSE 4567
ENTRYPOINT ["mvn", "clean", "verify", "exec:java"]