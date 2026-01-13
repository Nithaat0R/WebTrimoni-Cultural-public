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
          <button class="edit-btn">✎</button>
        </div>

        <div class="info-row">
          <span>{{ usuari.email }}</span>
          <button class="edit-btn">✎</button>
        </div>

        <div class="info-row">
          <span class="password">************</span>
          <button class="icon-btn">👁</button>
          <button class="edit-btn">✎</button>
        </div>

        <!-- Botón para cerrar sesión -->
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
      ]
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

.logout-btn {
  background: #e04545;
  border: none;
  color: white;
  border-radius: 6px;
  padding: 6px 12px;
  cursor: pointer;
  font-weight: bold;
}

.logout-btn:hover {
  background: #c83b3b;
}
</style>
