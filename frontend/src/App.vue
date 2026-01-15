<template>
  <div id="app">
    <header class="topbar">
      <div class="left">
        <div class="logo">
          <img src="@/assets/LOGO PATRIMONI.png" alt="Info Patrimoni" class="logo-img" @click="$router.push('/')" />
        </div>
        <nav class="menu">
          <button @click="$router.push('/')">INICI</button>
          <button @click="$router.push('/comarques')">COMARCA</button>
          <button @click="$router.push('/segles')">SEGLE</button>
          <button @click="$router.push('/estils')">ESTIL</button>
        </nav>
      </div>
      <div class="right">
        <div class="actions">
          <div class="search">
            <span class="search-icon">🔍</span>
            <input type="text" placeholder="Cerca..." v-model="search" @keyup.enter="cerca" />
          </div>

          <div class="user-icon" @click="anarPerfil">
            <svg viewBox="0 0 24 24" class="user-svg">
              <path
                d="M12 12c2.7 0 5-2.3 5-5s-2.3-5-5-5-5 2.3-5 5 2.3 5 5 5zm0 2c-3.3 0-10 1.7-10 5v3h20v-3c0-3.3-6.7-5-10-5z" />
            </svg>
          </div>

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
    anarPerfil() {
      if (currentUser.value) {
        this.$router.push("/zona-personal")
      } else {
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
  gap: 20px;
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
  cursor: pointer;
  display: flex;
  align-items: center;
}

.user-svg {
  width: 40px;
  height: 40px;
  fill: #d42828;
  transition: transform 0.2s ease, fill 0.2s ease;
}

.user-icon:hover .user-svg {
  transform: scale(1.1);
  fill: #a12020;
}

/* Content */
.content {
  padding: 20px;
}
</style>
