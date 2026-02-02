# 🏛️ Plataforma de Patrimoni Cultural de Catalunya

Aquest projecte centralitza la riquesa patrimonial del territori català utilitzant l'API de la Diputació de Barcelona, oferint una experiència interactiva i personalitzada per a l'usuari.

## 🚀 Com provar-ho localment

Actualment, l'entorn web no està desplegat en producció. Per explorar la plataforma, segueix aquests passos per configurar-la en el teu entorn local.

### 🛠️ Prerequisits

Abans de començar, assegura't de tenir instal·lat:
* **Java JDK 17**: Recomanem [Eclipse Temurin v17](https://adoptium.net/temurin/releases/?version=17).
* **Node.js & npm**: Necessaris per al front-end.
* **Terminal Bash**: Totes les comandes estan pensades per a entorns Bash.

---

### 📦 Pas 1: Clonar el repositori
Descarrega el codi font a la teva màquina local:

Bash:
git clone [https://github.com/](https://github.com/)[el-teu-usuari]/[el-teu-repo].git
cd [el-teu-repo]

### 💻 Pas 2: Configuració del Front-end (Vue.js)
Navega fins a la carpeta del front-end per instal·lar les dependències i aixecar el servei:

Bash:
cd front-end
npm install
npm run dev
Nota: Un cop executat, la terminal et retornarà un enllaç (ex: http://localhost:5173) per accedir a la interfície web.

### ⚙️ Pas 3: Configuració del Back-end (Spring Boot)
En una nova terminal, situa't a la carpeta arrel del projecte i executa la següent comanda per aixecar el servidor:

Bash:
./mvnw spring-boot:run
Això activarà la lògica de negoci i permetrà la comunicació amb l'API.

### 🛠️ Tecnologies principals
Back-end: Java 17 / Spring Boot

Front-end: Vue.js

Font de dades: API de Patrimoni Cultural de la Diputació de Barcelona

### 📝 Notes addicionals
Les funcionalitats d'interactivitat (comptes d'usuari i comentaris) requereixen que tant el front-end com el back-end estiguin executant-se simultàniament.