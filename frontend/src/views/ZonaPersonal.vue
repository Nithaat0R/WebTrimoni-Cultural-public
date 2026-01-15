<template>
  <div class="perfil-wrapper">

    <!-- CABECERA PERFIL -->
    <div class="perfil-card">
      <div class="avatar">
        <div class="avatar-icon">👤</div>
      </div>

      <div class="perfil-info">
        <div class="info-row">
          <strong>{{ usuari.nom }}</strong>
        </div>

        <div class="info-row">
          <span>{{ usuari.email }}</span>
        </div>

        <!-- ESTADÍSTICAS -->
        <div class="stats">
          <div class="stat">
            <span class="stat-number">{{ numComentaris }}</span>
            <span class="stat-label">Comentaris</span>
          </div>

          <div class="stat">
            <span class="stat-number">{{ patrimonis.length }}</span>
            <span class="stat-label">Favorits</span>
          </div>
        </div>

        <!-- BOTÓN CERRAR SESIÓN -->
        <div class="info-row">
          <button class="logout-btn" @click="logout">Cerrar sesión</button>
        </div>
      </div>
    </div>

    <!-- PATRIMONIS PREFERITS -->
    <h2 class="section-title">PATRIMONIS PREFERITS</h2>

    <div class="favorits">
      <div
        class="favorit-card"
        v-for="(p, i) in patrimonis"
        :key="i"
      >
        <div class="favorit-img">Imatge</div>
        <div class="favorit-title">{{ p }}</div>
      </div>

      <p v-if="!patrimonis.length" class="empty">
        No tens patrimonis preferits encara
      </p>
    </div>

  </div>
</template>

<script>
import { auth } from "@/firebase";
import { currentUser } from "@/store/userStore";

export default {
  name: "ZonaPersonal",
  data() {
    return {
      usuari: {
        nom: "Nom Cognom1 Cognom2",
        email: "mail@mail.com",
      },
      patrimonis: [
        "Sant Pere d'Abrera",
        "Monestir de Ripoll",
        "Castell de Cardona"
      ],
      numComentaris: 5
    };
  },
  methods: {
    async logout() {
      try {
        await auth.signOut();
        currentUser.value = null;
        this.$router.push("/");
      } catch (err) {
        console.error("Error al cerrar sesión:", err);
      }
    }
  }
};
</script>

<style scoped>
.perfil-wrapper {
  padding: 30px;
}

/* PERFIL */
.perfil-card {
  display: flex;
  gap: 20px;
  background: #ff8f8f;
  padding: 20px;
  border-radius: 16px;
  align-items: center;
}

.avatar {
  width: 90px;
  height: 90px;
  background: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-icon {
  font-size: 36px;
}

.perfil-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* STATS */
.stats {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}

.stat {
  background: #ffd6d6;
  padding: 10px 16px;
  border-radius: 12px;
  text-align: center;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
}

.stat-label {
  font-size: 12px;
  color: #555;
}

/* LOGOUT */
.logout-btn {
  margin-top: 10px;
  background: #e04545;
  border: none;
  color: white;
  border-radius: 8px;
  padding: 8px 16px;
  cursor: pointer;
  font-weight: bold;
}

.logout-btn:hover {
  background: #c83b3b;
}

/* FAVORITOS */
.section-title {
  margin: 30px 0 15px;
}

.favorits {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 16px;
}

.favorit-card {
  background: white;
  border-radius: 12px;
  padding: 10px;
  text-align: center;
}

.favorit-img {
  height: 100px;
  background: #eee;
  border-radius: 8px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #777;
}

.favorit-title {
  font-size: 14px;
  font-weight: bold;
}

.empty {
  grid-column: 1 / -1;
  text-align: center;
  color: #666;
}
</style>
