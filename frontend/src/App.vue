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
      <input
        type="text"
        v-model="search"
        placeholder="Cerca..."
        @keyup.enter="cerca"
      />
    </div>

    <main class="main">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      search: "",
    };
  },
  methods: {
    go(section) {
      if (section == "inici"){
        this.$router.push({
          name: "home"
        })
      }else if (section == "comarca"){
        this.$router.push({
          name: "comarques"
        })
      }
      console.log("Navegación:", section);
    },
    cerca() {
      if (!this.search) return;
      this.$router.push({
        name: "search",
        query: { q: this.search },
      });
    },
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
