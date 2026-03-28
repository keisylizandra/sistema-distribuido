# Usando a imagem oficial mais recente do Java 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia a sua pasta src inteira para dentro do container
COPY src/ ./src/

# Procura todos os arquivos .java dentro de src e compila todos de uma vez
RUN find src -name "*.java" > sources.txt && javac -encoding UTF-8 -d . @sources.txt

# Roda o seu servidor
CMD ["java", "-cp", ".", "sd.Servidor"]