# Compilation des sources principales
javac --module-path ./lib --add-modules javafx.controls,javafx.fxml -cp "lib/*" -d bin src/modele/*.java src/modele/controller/*.java src/modele/exception/*.java src/modele/**/*.java src/vue/*.java

# Exécution de la classe ApplicationJeuxOlympique
# java --module-path ./lib --add-modules javafx.controls,javafx.fxml -cp "bin:/usr/share/java/mariadb-java-client.jar:lib/*:src/ressources" src.vues.ApplicationJeuxOlympique
java --module-path ./lib --add-modules javafx.controls,javafx.fxml -cp "bin:/usr/share/java/mariadb-java-client.jar:lib/*:src/ressources" vue.Appli #2>/dev/null

