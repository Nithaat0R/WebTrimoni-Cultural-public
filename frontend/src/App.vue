<template>
  <div id="app">
    <header class="header">
      <button class="logo-btn">LOGO</button>
      <nav>
        <button @click="go('inici')">Inici</button>
        <button @click="go('comarca')">Comarca</button>
        <button @click="go('segle')">Segle</button>
        <button @click="go('estil')">Estil</button>
      </nav>
    </header>

    <div class="search-bar">
      <input type="text" v-model="search" placeholder="Cerca..." @keyup.enter="cerca" />
    </div>

    <main class="main">
      <h1>{{ current.toUpperCase() }}</h1>
      <p>Aquí pots començar a construir la teva web amb Vue.</p>
    </main>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "App",
  data() {
    return {
      current: "inici",
      search: "",
      resultats: [],
    };
  },
  methods: {
    go(section) {
      this.current = section;
    },
    async cerca() {
      try {
        console.log("Buscando:", this.search); // depuración
        const response = await axios.get(`http://localhost:8080/api/search`, {params: {
          search: this.search
        }});
        console.log("Respuesta del backend:", response.data);
        this.resultats = response.data;
      } catch (error) {
        console.error("Error al cercar", error);
  }
}
  },
};
</script>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: #f5f5f5;
}

.header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px 20px;
  background: #ffffff;
  border-bottom: 1px solid #ddd;
}

.logo-btn {
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  background: none;
  border: none;
}

nav button {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 8px 12px;
}

nav button:hover {
  background: #e0e0e0;
  border-radius: 5px;
}

.main {
  padding: 20px;
}
.search-bar {
  padding: 20px;
  background: #fff;
  border-bottom: 1px solid #ddd;
}

.search-bar input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

</style>
