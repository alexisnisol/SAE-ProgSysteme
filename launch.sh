# Compilation des sources principales
javac --module-path ./lib --add-modules javafx.controls,javafx.fxml -cp "lib/*" -d bin src/model/*.java src/controller/*.java src/model/exception/*.java src/model/**/*.java src/view/*.java src/network/*.java src/network/*/*.java src/network/protocols/*.java src/network/protocols/*/*.java src/bdd/*.java

# Exécution de la classe ApplicationJeuxOlympique
# java -cp bin com.model.Main

# java --module-path ./lib --add-modules javafx.controls,javafx.fxml -cp "bin:/usr/share/java/mariadb-java-client.jar:lib/*" com.view.Appli #2>/dev/null

java --module-path ./lib --add-modules javafx.controls,javafx.fxml -cp "bin:/usr/share/java/mariadb-java-client.jar:lib/*" bdd.ConnexionBD 

