<template>
  <div id="app">
    <header class="topbar">
      <!-- Esquerra del HUD -->
      <div class="left">
        <div class="logo">
          <img src="@/assets/LOGO PATRIMONI.png" alt="Info Patrimoni" class="logo-img" @click="$router.push('/')"/>
        </div>
        <nav class="menu">
          <button @click="$router.push('/')">INICI</button>
          <button @click="$router.push('/comarques')">COMARCA</button>
          <button @click="$router.push('/segles')">SEGLE</button>
          <button @click="$router.push('/estil')">ESTIL</button>
        </nav>
      </div>
      <!-- Dreta del HUD -->
      <div class="right">
        <div class="actions">
          <div class="search">
            <span class="search-icon">🔍</span>
            <input type="text" placeholder="Cerca..." v-model="search" @keyup.enter="cerca" />
          </div>

          <div class="user-icon" @click="anarPerfil">👤</div>
        </div>
      </div>
    </header>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script>
import { currentUser } from "@/store/userStore";
export default {
  name: "App",
  data() {
    return {
      search: "",
    };
  },
  methods: {
    cerca() {
      if (!this.search) return;
      this.$router.push({
        name: "search",
        query: { q: this.search },
      });
    },
    anarPerfil(){
      //Si tenim a userStore.js el value
      if(currentUser.value){
        this.$router.push("/zona-personal")
      }else{
        this.$router.push("/login")
      }
    }
  },
};
</script>

<style>
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background: #ffe0e0;
}

#app {
  min-height: 100vh;
}

/* ===== TOP BAR ===== */
.topbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ff6f6f;
  padding: 10px 20px;
  gap: 20px;
}

.left {
  display: flex;
  align-items: center;
  gap:20px;
}

.logo-img {
  height: 60px;
  width: auto;
}

.logo {
  display: flex;
  align-items: center;
}

/* Menu */
.menu {
  display: flex;
  gap: 10px;
}

.menu button {
  background: #ff7f7f;
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.menu button:hover {
  background: #ff5c5c;
}

/* Actions */
.actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

/* Search */
.search {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 20px;
  padding: 6px 12px;
  gap: 6px;
}

.search input {
  border: none;
  outline: none;
  font-size: 14px;
}

/* User */
.user-icon {
  font-size: 20px;
  cursor: pointer;
}

/* Content */
.content {
  padding: 20px;
}
</style>
