FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Donner les permissions d'exécution à Maven
RUN chmod +x mvnw

# Force l'encodage LF sur l'exécutable mnvw car si encodage CRLF Windows, la commande crash
RUN dos2unix mvnw

# Télécharger les dépendances
RUN ./mvnw dependency:go-offline

# Copier le code source
COPY src ./src

# Compiler et exécuter l'application
CMD ["./mvnw", "spring-boot:run"]