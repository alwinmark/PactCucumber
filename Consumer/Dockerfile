# Use an official Open Liberty image as the base image
FROM open-liberty:23.0.0.10-full-java11-openj9

# Copy the application WAR file to the dropins folder
COPY --chown=1001:0 target/open-liberty-rest.war /config/dropins/

# Copy the server configuration file to the config folder
COPY --chown=1001:0 src/main/liberty/config/server.xml /config/

# Run the server script
ENTRYPOINT ["/opt/ol/wlp/bin/server", "run"]
CMD ["defaultServer"]
